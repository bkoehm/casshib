/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.support;

import javax.servlet.http.HttpServletRequest;
import edu.ucmerced.cas.authentication.principal.CasShibSamlService;
import org.jasig.cas.authentication.principal.WebApplicationService;
import org.jasig.cas.services.UnauthorizedServiceException;

/**
 * Extension to the CASShib SAML argument extractor that will protect service
 * URLs based on a passcode embedded in the request URI.
 * 
 * @see CasShibSamlArgumentExtractor
 * @see AbstractShibEnabledArgumentExtractor
 * 
 * @author Brian Koehmstedt
 */
public class CasShibSamlPasscodeProtectedArgumentExtractor extends
    CasShibSamlArgumentExtractor {
    @Override
    public WebApplicationService extractServiceInternal(
        final HttpServletRequest request) {
        CasShibSamlService service = (CasShibSamlService) super
            .extractServiceInternal(request);

        boolean authorizedService = isAuthorized(service, true);
        if (!authorizedService) {
            // fortunately this is a RuntimeException
            throw new UnauthorizedServiceException(
                "The passcode provided is invalid.");
        }

        return (service);
    }
}
