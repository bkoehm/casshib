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
package edu.ucmerced.cas.services;

import org.jasig.cas.services.RegisteredServiceImpl;

/**
 * An extension to the registered service interface to allow a service to have a
 * passcode. This is to provide additional service authentication during CASShib
 * requests.
 * 
 * @author Brian Koehmstedt
 * @version $Revision$ $Date$
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
