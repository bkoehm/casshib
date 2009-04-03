/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package edu.ucmerced.cas.authentication.principal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.principal.*;
import org.jasig.cas.authentication.principal.Response.ResponseType;
import org.jasig.cas.util.HttpClient;
import org.springframework.util.StringUtils;

/**
 * Represents a service which wishes to use the CAS protocol.
 * 
 * <p/>
 * 
 * Modified from the original to extract the application name or passcode from
 * the request URI.
 * 
 * <p/>
 * 
 * Derived from:
 * org.jasig.cas.authentication.principal.SimpleWebApplicationServiceImpl
 * 
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision:$
 * @since 3.3.1a
 */
public class CasShibWebApplicationServiceImpl extends
    AbstractWebApplicationService implements CasShibService {

    /** Log instance for logging events, info, warnings, errors, etc. */
    private static final Log log = LogFactory
        .getLog(CasShibWebApplicationServiceImpl.class);

    private static final String CONST_PARAM_SERVICE = "service";

    private static final String CONST_PARAM_TARGET_SERVICE = "targetService";

    private static final String CONST_PARAM_TICKET = "ticket";

    private static final String CONST_PARAM_METHOD = "method";

    private final ResponseType responseType;

    private final String appNameOrPasscode;

    /**
     * Unique Id for Serialization
     */
    private static final long serialVersionUID = -6343351235051683579L;

    public CasShibWebApplicationServiceImpl(final String id) {
        this(id, id, null, null, null, null);
    }

    private CasShibWebApplicationServiceImpl(final String id,
        final String originalUrl, final String artifactId,
        final ResponseType responseType, final HttpClient httpClient,
        final String appNameOrPasscode) {
        super(id, originalUrl, artifactId, httpClient);
        this.responseType = responseType;
        this.appNameOrPasscode = appNameOrPasscode;
    }

    public static CasShibWebApplicationServiceImpl createServiceFrom(
        final HttpServletRequest request) {
        return createServiceFrom(request, null);
    }

    public static CasShibWebApplicationServiceImpl createServiceFrom(
        final HttpServletRequest request, final HttpClient httpClient) {
        final String targetService = request
            .getParameter(CONST_PARAM_TARGET_SERVICE);
        final String method = request.getParameter(CONST_PARAM_METHOD);
        final String serviceToUse = StringUtils.hasText(targetService) ? targetService
            : request.getParameter(CONST_PARAM_SERVICE);

        if (!StringUtils.hasText(serviceToUse)) {
            return null;
        }

        final String id = cleanupUrl(serviceToUse);
        final String artifactId = request.getParameter(CONST_PARAM_TICKET);

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

        return new CasShibWebApplicationServiceImpl(id, serviceToUse,
            artifactId, "POST".equals(method) ? ResponseType.POST
                : ResponseType.REDIRECT, httpClient, appNameOrPasscode);
    }

    public Response getResponse(final String ticketId) {
        final Map<String, String> parameters = new HashMap<String, String>();

        if (StringUtils.hasText(ticketId)) {
            parameters.put(CONST_PARAM_TICKET, ticketId);
        }

        if (ResponseType.POST == this.responseType) {
            return Response.getPostResponse(getOriginalUrl(), parameters);
        }
        return Response.getRedirectResponse(getOriginalUrl(), parameters);
    }

    public String getAppNameOrPasscode() {
        return (this.appNameOrPasscode);
    }
}
