/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.support;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.authentication.principal.WebApplicationService;

import edu.ucmerced.cas.authentication.principal.CasShibSamlService;

/**
 * Retrieve the ticket and artifact based on the SAML 1.1 profile.
 * 
 * <p/>
 * 
 * This class has been modified from the original to identify the Shibboleth
 * service by the application identifier embedded in the request URI.
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.support.SamlArgumentExtractor
 * 
 * @see AbstractShibEnabledArgumentExtractor
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public abstract class CasShibSamlArgumentExtractor extends
    AbstractShibEnabledArgumentExtractor {
    public WebApplicationService extractServiceInternal(
        final HttpServletRequest request) {
        WebApplicationService service = CasShibSamlService.createServiceFrom(
            request, getHttpClientIfSingleSignOutEnabled());

        return (service);
    }
}
