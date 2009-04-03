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
 * Extension to the CASShib SAML argument extractor that will enforce matching
 * between the application name embedded in the request URI and the service id
 * embedded in the query string.
 * 
 * @see CasShibArgumentExtractor
 * @see AbstractShibEnabledArgumentExtractor
 * 
 * @author Brian Koehmstedt
 * @version $Revision:$ $Date:$
 * @since 3.3.1a
 */
public class CasShibSamlRegistrationProtectedArgumentExtractor extends
    CasShibSamlArgumentExtractor {
    @Override
    public WebApplicationService extractServiceInternal(
        final HttpServletRequest request) {
        CasShibSamlService service = (CasShibSamlService) super
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
