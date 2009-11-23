/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.inspektr.common.ioc.annotation.NotNull;
import org.jasig.cas.CentralAuthenticationService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import edu.ucmerced.cas.services.CasShibServiceRegistrar;
import edu.ucmerced.cas.web.support.CasShibCookieRetrievingCookieGenerator;
import edu.ucmerced.cas.web.support.CasShibUtil;

/**
 * Controller to delete ticket granting ticket cookie in order to log out of
 * single sign on. This controller implements the idea of the ESUP Portail's
 * Logout patch to allow for redirecting to a url on logout. It also exposes a
 * log out link to the view via the WebConstants.LOGOUT constant.
 * 
 * <p/>
 * 
 * Modified from original to handle logging out of the Shibboleth as well as
 * CAS. This handles logging out of the Shibboleth service provider. In
 * addition, if the SSOLogout parameter is present in the request URL, it will
 * also force a logout from the Shibboleth identity provider. Note that this
 * means the user will be presented with the SSO login screen the next time he
 * or she tries to log in and this is all we're trying to do here (after all, it
 * would be bad if the user hits a logout link, comes back, and isn't prompted
 * for a login screen because the Shibboleth side has the user's authentication
 * cached). This is NOT performing any kind of "single logout." The user's
 * session will still be active in other applications previously logged into.
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.LogoutController
 * 
 * @author Scott Battaglia
 * @author Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class CasShibLogoutController extends AbstractController {

    /** Log instance for logging events, info, warnings, errors, etc. */
    private final Log log = LogFactory.getLog(this.getClass());

    /** The CORE to which we delegate for all CAS functionality. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    /** CookieGenerator for TGT Cookie */
    @NotNull
    private CasShibCookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    /** CookieGenerator for Warn Cookie */
    @NotNull
    private CasShibCookieRetrievingCookieGenerator warnCookieGenerator;

    /** Logout view name. */
    @NotNull
    private String logoutView;

    /**
     * Boolean to determine if we will redirect to any url provided in the
     * service request parameter.
     */
    private boolean followServiceRedirects;

    /** registrations that contain service ID to passcode mappings */
    @NotNull
    private CasShibServiceRegistrar casShibServiceRegistrar;

    /** URL that contains the logout URL of the primary CAS server */
    private String primaryCASLogoutURL;

    /** The parameter for the primary CAS logout URL that contains the return URL */
    private String primaryCASLogoutReturnURLParameter;

    /**
     * Optional URL that will clear the user's Shibboleth IdP session so that
     * the user will be forced to log in next time a service tries to log in
     */
    private String shibIdpSessionClearURL;

    /**
     * The parameter for the Shib IdP session clear URL that contains the return
     * URL
     */
    private String shibIdpSessionClearReturnURLParameter;

    public CasShibLogoutController() {
        setCacheSeconds(0);
    }

    protected ModelAndView handleRequestInternal(
        final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        final String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator
            .retrieveCookieValue(request);
        final String service = request.getParameter("service");

        if (ticketGrantingTicketId != null) {
            this.centralAuthenticationService
                .destroyTicketGrantingTicket(ticketGrantingTicketId);

            this.ticketGrantingTicketCookieGenerator.removeCookie(request,
                response);
            this.warnCookieGenerator.removeCookie(request, response);
        }

        // If the appname can be determined from the cookie path then lets
        // redirect to the shib logout URL before we redirect back to the
        // service.

        String appName = CasShibUtil.getAppNameFromRequestURI(request,
            casShibServiceRegistrar);
        String cookiePath = (appName != null ? (request.getContextPath() != null ? request
            .getContextPath()
            : "")
            + "/shib/" + appName
            : "/");
        if (cookiePath != null
            && cookiePath.length() > (request.getContextPath() != null ? request
                .getContextPath().length()
                : 1)) {
            String returnURL = null;
            // if SSOLogout is set, then that indicates the end app wants to
            // sign out of the Single Sign On system altogether (meaning the
            // primary CAS server).
            if (request.getParameter("SSOLogout") != null
                && primaryCASLogoutURL != null) {
                // hit the primary CAS logout URL
                // then hit Shib IdP logout URL if one exists
                // then have either primary CAS or the Shib IDP redirect to the
                // service

                if (shibIdpSessionClearURL != null) {
                    String shibLogoutURL = shibIdpSessionClearURL
                        + "?"
                        + (shibIdpSessionClearReturnURLParameter != null ? shibIdpSessionClearReturnURLParameter
                            : "return") + "="
                        + URLEncoder.encode(service, "UTF-8");

                    returnURL = primaryCASLogoutURL
                        + "?"
                        + (primaryCASLogoutReturnURLParameter != null ? primaryCASLogoutReturnURLParameter
                            : "service") + "="
                        + URLEncoder.encode(shibLogoutURL, "UTF-8");
                } else {
                    returnURL = primaryCASLogoutURL
                        + "?"
                        + (primaryCASLogoutReturnURLParameter != null ? primaryCASLogoutReturnURLParameter
                            : "service") + "="
                        + URLEncoder.encode(service, "UTF-8");
                }
            } else {
                // return back to the service from Shib
                returnURL = service;
            }
            String shibLogoutURL = cookiePath
                + "/Shibboleth.sso/Logout?return="
                + URLEncoder.encode(returnURL, "UTF-8");
            log.debug("Redirecting the user to " + shibLogoutURL);
            return new ModelAndView(new RedirectView(shibLogoutURL));
        } else if (this.followServiceRedirects && service != null) {
            log.debug("couldn't determine appname from cookie path: "
                + cookiePath);
            return new ModelAndView(new RedirectView(service));
        }

        return new ModelAndView(this.logoutView);
    }

    public void setTicketGrantingTicketCookieGenerator(
        final CasShibCookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setWarnCookieGenerator(
        final CasShibCookieRetrievingCookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    /**
     * @param centralAuthenticationService
     *            The centralAuthenticationService to set.
     */
    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    public void setFollowServiceRedirects(final boolean followServiceRedirects) {
        this.followServiceRedirects = followServiceRedirects;
    }

    public void setLogoutView(final String logoutView) {
        this.logoutView = logoutView;
    }

    public void setCasShibServiceRegistrar(
        final CasShibServiceRegistrar casShibServiceRegistrar) {
        this.casShibServiceRegistrar = casShibServiceRegistrar;
    }

    public void setPrimaryCASLogoutURL(final String primaryCASLogoutURL) {
        this.primaryCASLogoutURL = primaryCASLogoutURL;
    }

    public void setPrimaryCASLogoutReturnURLParameter(
        final String primaryCASLogoutReturnURLParameter) {
        this.primaryCASLogoutReturnURLParameter = primaryCASLogoutReturnURLParameter;
    }

    public void setShibIdpSessionClearURL(final String shibIdpSessionClearURL) {
        this.shibIdpSessionClearURL = shibIdpSessionClearURL;
    }

    public void setShibIdpSessionClearReturnURLParameter(
        final String shibIdpSessionClearReturnURLParameter) {
        this.shibIdpSessionClearReturnURLParameter = shibIdpSessionClearReturnURLParameter;
    }
}
