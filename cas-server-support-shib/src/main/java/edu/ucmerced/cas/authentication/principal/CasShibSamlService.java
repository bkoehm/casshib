/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package edu.ucmerced.cas.authentication.principal;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.principal.*;
import org.jasig.cas.util.HttpClient;
import org.springframework.util.StringUtils;

/**
 * Class to represent that this service wants to use SAML. We use this in
 * combination with the CentralAuthenticationServiceImpl to choose the right
 * UniqueTicketIdGenerator.
 * 
 * <p/>
 * 
 * Modified from the original to extract the application name or passcode from
 * the request URI.
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.authentication.principal.SamlService
 * 
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class CasShibSamlService extends AbstractWebApplicationService implements
    CasShibService {

    /** Log instance for logging events, info, warnings, errors, etc. */
    private static final Log log = LogFactory.getLog(CasShibSamlService.class);

    /** Constant representing service. */
    private static final String CONST_PARAM_SERVICE = "TARGET";

    /** Constant representing artifact. */
    private static final String CONST_PARAM_TICKET = "SAMLart";
    
    private static final String CONST_START_ARTIFACT_XML_TAG_NO_NAMESPACE = "<AssertionArtifact>";
    
    private static final String CONST_END_ARTIFACT_XML_TAG_NO_NAMESPACE = "</AssertionArtifact>";

    private static final String CONST_START_ARTIFACT_XML_TAG = "<samlp:AssertionArtifact>";

    private static final String CONST_END_ARTIFACT_XML_TAG = "</samlp:AssertionArtifact>";

    private String requestId;

    /**
     * Unique Id for serialization.
     */
    private static final long serialVersionUID = -2584864803966659627L;

    private final String appNameOrPasscode;

    protected CasShibSamlService(final String id) {
        super(id, id, null, new HttpClient());
        this.appNameOrPasscode = null;
    }

    protected CasShibSamlService(final String id, final String originalUrl,
        final String artifactId, final HttpClient httpClient,
        final String requestId, final String appNameOrPasscode) {
        super(id, originalUrl, artifactId, httpClient);
        this.requestId = requestId;
        this.appNameOrPasscode = appNameOrPasscode;
    }

    /**
     * This always returns true because a SAML Service does not receive the
     * TARGET value on validation.
     */
    public boolean matches(final Service service) {
        return true;
    }

    public String getRequestID() {
        return this.requestId;
    }

    public static CasShibSamlService createServiceFrom(
        final HttpServletRequest request, final HttpClient httpClient) {
        final String service = request.getParameter(CONST_PARAM_SERVICE);
        final String artifactId;
        final String requestBody = getRequestBody(request);
        final String requestId;

        if (!StringUtils.hasText(service) && !StringUtils.hasText(requestBody)) {
            return null;
        }

        final String id = cleanupUrl(service);

        if (StringUtils.hasText(requestBody)) {
            final String tagStart;
            final String tagEnd;
            if (requestBody.contains(CONST_START_ARTIFACT_XML_TAG)) {
              tagStart = CONST_START_ARTIFACT_XML_TAG;
              tagEnd = CONST_END_ARTIFACT_XML_TAG;
            } else {
              tagStart = CONST_START_ARTIFACT_XML_TAG_NO_NAMESPACE;
              tagEnd = CONST_END_ARTIFACT_XML_TAG_NO_NAMESPACE;
            }
            final int startTagLocation = requestBody.indexOf(tagStart);
            final int artifactStartLocation = startTagLocation + tagStart.length();
            final int endTagLocation = requestBody.indexOf(tagEnd);
            
            artifactId = requestBody.substring(artifactStartLocation, endTagLocation).trim();

            // is there a request id?
            requestId = extractRequestId(requestBody);
        } else {
            artifactId = null;
            requestId = null;
        }

        // Extract the service passcode from url.
        // URLs should be in the following format:
        // /<contextPath>/shib/<appNameOrPasscode>/?
        String appNameOrPasscode = null;
        if ((request.getContextPath() != null ? request.getRequestURI()
            .startsWith(request.getContextPath() + "/shib") : request
            .getRequestURI().startsWith("/shib"))) {
            String[] components = request.getRequestURI().substring(
                (request.getContextPath() != null ? request.getContextPath()
                    .length() : 0)).split("/");
            // 0 is the empty string before the first slash
            // 1 should be the shibX string
            // 2 should be the app name or passcode
            // 3... should be everything after the app name or passcode
            if (components.length > 3) {
                appNameOrPasscode = components[2];
                log
                    .debug("application name or passcode = "
                        + appNameOrPasscode);
            }
        } else {
            log.debug("no application name or passcode detected in url");
        }

        if (log.isDebugEnabled()) {
            log.debug("Attempted to extract Request from HttpServletRequest.  Results:");
            log.debug(String.format("Request Body: %s", requestBody));
            log.debug(String.format("Extracted ArtifactId: %s", artifactId));
            log.debug(String.format("Extracted Request Id: %s", requestId));
        }

        return new CasShibSamlService(id, service, artifactId, httpClient,
            requestId, appNameOrPasscode);
    }

    public Response getResponse(final String ticketId) {
        final Map<String, String> parameters = new HashMap<String, String>();

        parameters.put(CONST_PARAM_TICKET, ticketId);
        parameters.put(CONST_PARAM_SERVICE, getOriginalUrl());

        return Response.getRedirectResponse(getOriginalUrl(), parameters);
    }

    protected static String extractRequestId(final String requestBody) {
        if (!requestBody.contains("RequestID")) {
            return null;
        }

        try {
            final int position = requestBody.indexOf("RequestID=\"") + 11;
            final int nextPosition = requestBody.indexOf("\"", position);

            return requestBody.substring(position,  nextPosition);
        } catch (final Exception e) {
            log.debug("Exception parsing RequestID from request." ,e);
            return null;
        }
    }

    protected static String getRequestBody(final HttpServletRequest request) {
        final StringBuilder builder = new StringBuilder();
        try {
            final BufferedReader reader = request.getReader();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (final Exception e) {
            return null;
        }
    }

    public String getAppNameOrPasscode() {
        return (this.appNameOrPasscode);
    }
}
