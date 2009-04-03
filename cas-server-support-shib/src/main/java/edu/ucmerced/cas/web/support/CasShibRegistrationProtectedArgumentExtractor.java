/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.support;

import javax.servlet.http.HttpServletRequest;
import edu.ucmerced.cas.authentication.principal.CasShibWebApplicationServiceImpl;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.UnauthorizedServiceException;

/**
 * Extension to the CASShib argument extractor that will enforce matching
 * between the application name embedded in the request URI and the service id
 * embedded in the query string.
 * 
 * @see CasShibArgumentExtractor
 * @see AbstractShibEnabledArgumentExtractor
 * 
 * @author Brian Koehmstedt
 */
public class CasShibRegistrationProtectedArgumentExtractor extends
    CasShibArgumentExtractor {
    @Override
    public WebApplicationService extractServiceInternal(
        final HttpServletRequest request) {
        CasShibWebApplicationServiceImpl service = (CasShibWebApplicationServiceImpl) super
            .extractServiceInternal(request);

        boolean authorizedService = isAuthorized(service, false);
        if (!authorizedService) {
            // fortunately this is a RuntimeException
            throw new UnauthorizedServiceException(
                "The passcode provided is invalid.");
        }

        return (service);
    }
}
