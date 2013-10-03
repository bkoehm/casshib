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
 * @author Brian Koehmstedt
 * @version $Revision$ $Date$
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
