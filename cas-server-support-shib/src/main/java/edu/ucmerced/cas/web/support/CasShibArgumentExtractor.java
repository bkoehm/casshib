/*
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.support.CasArgumentExtractor
 * 
 * @see AbstractShibEnabledArgumentExtractor
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
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
