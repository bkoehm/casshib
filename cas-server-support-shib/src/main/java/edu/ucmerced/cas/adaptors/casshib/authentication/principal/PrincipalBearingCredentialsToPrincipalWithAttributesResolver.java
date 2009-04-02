/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
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
 * Dervied from:
 * org.jasig.cas.adaptors.trusted.authentication.principal.PrincipalBearingCredentialsToPrincipalResolver
 * 
 * @author Andrew Petro
 * @author modified by Brian Koehmstedt
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
