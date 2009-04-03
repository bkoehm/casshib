/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.support;

import javax.servlet.http.HttpServletRequest;
import org.jasig.cas.services.UnauthorizedServiceException;
import edu.ucmerced.cas.services.CasShibServiceRegistrar;
import edu.ucmerced.cas.services.CasShibRegisteredService;

/**
 * General web utility methods for CASShib functionality.
 * 
 * @author Brian Koehmstedt
 * @version $Revision:$ $Date:$
 * @since 3.3.1a
 */
public class CasShibUtil {
    /**
     * This parses out the application name from the request URI.
     * 
     * <p/>
     * 
     * Convention is as follows: /<contextPath>/shib/<appName>/...<br/>
     * Registrations are typically in casshib-service-registrations.xml.
     * 
     * @see AbstractShibEnabledArgumentExtractor
     * 
     * @throws UnauthorizedServiceException
     *             Thrown if the application name is not registered.
     * @param request
     *            <code>HttpServletRequest</code> object
     * @param registrar
     *            The CASShib service registrar
     * @return The application name from the request URI.
     */
    public static String getAppNameFromRequestURI(HttpServletRequest request,
        CasShibServiceRegistrar registrar) {
        // If the request URI follows the form of
        // <contextPath>/shib/<appName>/<service> then we can determine
        // appName from the requestURI. This is needed for cookie paths (so
        // we have a cookie-per-app) and also redirecting to Shibboleth
        // logout URLs.

        if (request == null || request.getRequestURI() == null)
            return null;

        String appName = null;
        String prefix = (request.getContextPath() != null ? request
            .getContextPath() : "")
            + "/shib/";
        if (request.getRequestURI().startsWith(prefix)) {
            appName = request.getRequestURI().substring(prefix.length(),
                request.getRequestURI().lastIndexOf('/'));
        }

        // Verify the app is a valid registered app. Note if CAS login URLs
        // are protected by the Shibboleth SP web server module then
        // Shibboleth may pre-emptively error out before we ever get here
        // (it might not error out either if you map to a valid default
        // service in shibboleth2.xml).
        try {
            CasShibRegisteredService service = registrar
                .findServiceByAppName(appName);
            if (service == null) {
                throw new UnauthorizedServiceException(
                    "Application name is not registered.");
            }
        } catch (CasShibServiceRegistrar.CasShibServiceRegistrarException e) {
            throw new UnauthorizedServiceException(
                "Application name is not registered.", e);
        }

        return (appName);
    }
}
