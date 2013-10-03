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
package edu.ucmerced.cas.adaptors.casshib.web.flow;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.jasig.cas.adaptors.trusted.authentication.principal.PrincipalBearingCredentials;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.SimplePrincipal;
import org.jasig.cas.web.flow.AbstractNonInteractiveCredentialsAction;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.execution.RequestContext;

/**
 * Implementation of the NonInteractiveCredentialsAction that looks for a remote
 * user that is set in the <code>HttpServletRequest</code> and attempts to
 * construct a Principal (and thus a PrincipalBearingCredentials). If it doesn't
 * find one, this class returns and error event which tells the web flow it
 * could not find any credentials.S
 * 
 * <p/>
 * 
 * Modified from original to pull attributes from Shibboleth headers and to add
 * them to the principal. These headers are injected by the Shibboleth service
 * provider module (typically an Apache or IIS module) that proxys requests to
 * the CAS server.
 * 
 * <p/>
 * 
 * In order for this class to identify which headers are Shibboleth attributes
 * and which aren't, it is necessary to employ a header naming convention. Any
 * header name that starts with a prefix string (typically 'shibattr-') will be
 * recognized as an attribute to be embedded in the Principal. You configure the
 * header names in the shibboleth2.xml file in the Shibboleth service provider
 * configuration directory (note that the default Shibboleth SP configuration
 * doesn't prepend the header names with a prefix -- you must change the default
 * names).
 * 
 * <p/>
 * 
 * Derived from:
 * org.jasig.cas.adaptors.trusted.web.flow.PrincipalFromRequestRemoteUserNonInteractiveCredentialsAction
 * 
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class PrincipalFromHttpHeadersNonInteractiveCredentialsAction extends
    AbstractNonInteractiveCredentialsAction {

    protected Credentials constructCredentialsFromRequest(
        final RequestContext context) {
        final HttpServletRequest request = WebUtils
            .getHttpServletRequest(context);
        final String remoteUser = request.getRemoteUser();

        if (StringUtils.hasText(remoteUser)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Remote  User [" + remoteUser
                    + "] found in HttpServletRequest");
            }

            /**
             * Grab the Shibboleth attributes from the HTTP headers, create a
             * map of them, and include the, in the SimplePrincipal.
             */
            HashMap<String, Object> attributes = new HashMap<String, Object>();

            Enumeration en = request.getHeaderNames();
            while (en.hasMoreElements()) {
                String name = (String) en.nextElement();
                if (name.startsWith("shibattr-") || name.startsWith("Shib-")) {
                    ArrayList<String> valueList = new ArrayList<String>();
                    Enumeration en2 = request.getHeaders(name);
                    while (en2.hasMoreElements()) {
                        String value = (String) en2.nextElement();
                        if (value.length() > 0) {
                            valueList.add(value);
                        }
                    }
                    if (valueList.size() > 0) {
                        if (name.startsWith("shibattr-"))
                            attributes.put(
                                name.substring("shibattr-".length()),
                                (valueList.size() == 1 ? valueList.get(0)
                                    : valueList));
                        else
                            attributes.put(name,
                                (valueList.size() == 1 ? valueList.get(0)
                                    : valueList));
                    }
                }
            }

            return new PrincipalBearingCredentials(new SimplePrincipal(
                remoteUser, attributes));
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Remote User not found in HttpServletRequest.");
        }

        return null;
    }
}
