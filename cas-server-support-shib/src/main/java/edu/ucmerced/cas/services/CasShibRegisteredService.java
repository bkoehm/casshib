/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.services;

import org.jasig.cas.services.RegisteredServiceImpl;

/**
 * An extension to the registered service interface to allow a service to have a
 * passcode. This is to provide additional service authentication during CASShib
 * requests.
 * 
 * @author Brian Koehmstedt
 * @version $Revision:$ $Date:$
 * @since 3.3.1a
 */
public class CasShibRegisteredService extends RegisteredServiceImpl {
    /**
     * Unique Id for serialization.
     */
    private static final long serialVersionUID = 4488281655556155879L;

    private String passcode;

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public boolean equals(final Object obj) {
        boolean superEquals = super.equals(obj);
        if (!superEquals)
            return false;

        if (!(obj instanceof CasShibRegisteredService))
            return false;

        final CasShibRegisteredService other = (CasShibRegisteredService) obj;

        if (this.passcode == other.getPasscode()
            || (this.passcode != null && other.getPasscode() != null && this.passcode
                .equals(other.getPasscode())))
            return true;
        else
            return false;
    }
}
