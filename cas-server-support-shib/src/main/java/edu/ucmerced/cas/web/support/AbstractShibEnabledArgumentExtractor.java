/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.support;

import javax.validation.constraints.NotNull;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.web.support.AbstractSingleSignOutEnabledArgumentExtractor;
import edu.ucmerced.cas.authentication.principal.CasShibService;
import edu.ucmerced.cas.services.CasShibServiceRegistrar;
import edu.ucmerced.cas.services.CasShibRegisteredService;

/**
 * A Shib-enabled argument extractor that will extract the application name or
 * passcode from the request URI and will authorize the request based on whether
 * or not the application name or passcode matches what is registered for the
 * service.
 * 
 * <p/>
 * 
 * This identifies the Shibboleth service by the application identifier embedded
 * in the request URI. Convention is as follows for the request URIs:
 * /[contextPath]/[appNameOrPasscode]/[...]. Application names and passcodes are
 * registered in the shibServiceRegistrations.xml file. Application name is used
 * for requests where the URL is exposed to the user (i.e., login and logout),
 * and the passcode is used for back-channel communication between the service
 * and the CAS server (i.e., serviceValidate, proxyValidate, etc).
 * 
 * @author Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public abstract class AbstractShibEnabledArgumentExtractor extends
    AbstractSingleSignOutEnabledArgumentExtractor {
    /** Log instance for logging events, info, warnings, errors, etc. */
    private final Log log = LogFactory.getLog(this.getClass());

    /** registrations that contain the service ID to passcode mappings */
    @NotNull
    private CasShibServiceRegistrar casShibServiceRegistrar;

    /**
     * Set the CasShibServiceRegistrar bean (typically set via Spring
     * configuration).
     * 
     * @param casShibServiceRegistrar
     *            The bean containing the CasShib service registrations.
     */
    public void setCasShibServiceRegistrar(
        final CasShibServiceRegistrar casShibServiceRegistrar) {
        this.casShibServiceRegistrar = casShibServiceRegistrar;
    }

    /**
     * Get the CasShib serivce registrations.
     * 
     * @return The CasShibServiceRegistrar bean.
     */
    protected CasShibServiceRegistrar getCasShibServiceRegistrar() {
        return this.casShibServiceRegistrar;
    }

    /**
     * Check to see if the service parameter in the query string matches the
     * service id of the registered CasShib service.
     * 
     * @param service
     *            The service object
     * @param usePasscode
     *            True if the service is to be authorized based on the passcode.
     *            Otherwise authorize it based on the service's application
     *            name.
     * @return true if the service parameter matches the id of the registered
     *         CasShib service
     */
    protected boolean isAuthorized(CasShibService service, boolean usePasscode) {
        boolean authorizedService = false;
        if (service != null) {
            try {
                CasShibRegisteredService shibService = getCasShibServiceRegistrar()
                    .findService(service);
                if (shibService != null) {
                    String serviceAppIdToken = service.getAppNameOrPasscode();
                    String registeredAppIdToken = (usePasscode ? shibService
                        .getPasscode() : shibService.getName());

                    // confirm that either the passcode or the appname passed
                    // in the request URI matches what is registered for the
                    // service
                    if (registeredAppIdToken != null
                        && serviceAppIdToken != null
                        && registeredAppIdToken.equals(serviceAppIdToken)) {
                        authorizedService = true;
                    } else {
                        log
                            .info(service.getId()
                                + " is not authorized because of an incorrect application name or passcode in the request uri");
                    }
                } else {
                    log.info(service.getId()
                        + " is not a registered casshib service");
                }
            } catch (CasShibServiceRegistrar.CasShibServiceRegistrarException e) {
                // log but don't let this be a fatal error
                // return false (not authorized) if this error occurs
                e.printStackTrace();
                log
                    .error("Received a CasShibServiceRegistrarException when trying to authorize a service: message="
                        + e.getMessage());
            }
        }

        return authorizedService;
    }
}
