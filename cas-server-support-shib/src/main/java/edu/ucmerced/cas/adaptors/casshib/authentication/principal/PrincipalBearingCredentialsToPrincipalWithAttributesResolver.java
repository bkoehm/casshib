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
package edu.ucmerced.cas.adaptors.casshib.authentication.principal;

import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.adaptors.trusted.authentication.principal.PrincipalBearingCredentials;
import org.jasig.cas.authentication.principal.CredentialsToPrincipalResolver;
import org.jasig.cas.authentication.principal.Credentials;

/**
 * Extracts the Principal out of PrincipalBearingCredentials. It is very simple
 * to resolve PrincipalBearingCredentials to a Principal since the credentials
 * already bear the ready-to-go Principal.
 * 
 * <p/>
 * 
 * Modified from the original to not extend
 * AbstractPersonDirectoryCredentialsToPrincipalResolver so that we implement
 * our own resolvePrincipal(). The principal is extracted directly from the
 * credentials rather than a new SimplePrincipal being instantiated. This is
 * because the credential's Principal already contains the attributes we need.
 * 
 * <p/>
 * 
 * Derived from:
 * org.jasig.cas.adaptors.trusted.authentication.principal.PrincipalBearingCredentialsToPrincipalResolver
 * 
 * @author Andrew Petro
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class PrincipalBearingCredentialsToPrincipalWithAttributesResolver
    implements CredentialsToPrincipalResolver {

    protected String extractPrincipalId(Credentials credentials) {
        return ((PrincipalBearingCredentials) credentials).getPrincipal()
            .getId();
    }

    public boolean supports(final Credentials credentials) {
        return credentials != null
            && credentials.getClass().equals(PrincipalBearingCredentials.class);
    }

    public Principal resolvePrincipal(final Credentials credentials) {
        return (credentials != null ? ((PrincipalBearingCredentials) credentials)
            .getPrincipal()
            : null);
    }
}
