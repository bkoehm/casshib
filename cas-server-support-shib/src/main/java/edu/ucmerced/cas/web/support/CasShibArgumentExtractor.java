/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.support;

import javax.servlet.http.HttpServletRequest;
import edu.ucmerced.cas.authentication.principal.CasShibWebApplicationServiceImpl;
import org.jasig.cas.authentication.principal.WebApplicationService;

/**
 * Implements the traditional CAS2 protocol. Accepts an HttpClient reference. A
 * default one is configured that you can override.
 * 
 * <p/>
 * 
 * This class has been modified from the original to identify the Shibboleth
 * service by the application identifier embedded in the request URI.
 * 
 * @see AbstractShibEnabledArgumentExtractor
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.support.CasArgumentExtractor
 * 
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision:$ $Date:$
 * @since 3.3.1a
 */
public abstract class CasShibArgumentExtractor extends
    AbstractShibEnabledArgumentExtractor {
    /**
     * Create a WebApplicationService based on the information in the request
     * URL.
     */
    @Override
    public WebApplicationService extractServiceInternal(
        final HttpServletRequest request) {
        WebApplicationService service = CasShibWebApplicationServiceImpl
            .createServiceFrom(request, getHttpClientIfSingleSignOutEnabled());

        return (service);
    }
}
