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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasig.cas.authentication.principal.RememberMeCredentials;
import org.jasig.cas.services.UnauthorizedServiceException;
import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;
import edu.ucmerced.cas.services.CasShibServiceRegistrar;

import javax.validation.constraints.NotNull;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * Extends CookieGenerator to allow you to retrieve a value from a request.
 * 
 * <p/>
 * 
 * Also has support for RememberMe Services
 * 
 * <p/>
 * 
 * This class has been modified from the original to issue cookies with
 * differing names and cookie paths based on the request URI conventions for
 * CasShib. This is necessary so that CAS doesn't cache authentication
 * credentials for different Shibboleth services.
 * 
 * <p/>
 * 
 * Cookies will be issued as such: CASTGC-[appName] with a cookie path of /[contextPath]/[appName].
 * 
 * <p/>
 * 
 * Since the cookie name and cookie path can change upon any given request, the
 * Spring CookieGenerator must be instantiated on a per-request basis rather
 * than instantiated just once upon application initialization.
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.support.CookieRetrievingCookieGenerator
 * 
 * @see AbstractShibEnabledArgumentExtractor
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class CasShibCookieRetrievingCookieGenerator {

    /**
     * The maximum age the cookie should be remembered for. The default is three
     * months (7889231 in seconds, according to Google)
     */
    private int rememberMeMaxAge = 7889231;

    /** should cookie be secure (https) only? */
    private Boolean cookieSecure;

    /** cookie max age */
    private Integer cookieMaxAge;

    /** cookie name prefix */
    @NotNull
    private String cookieNamePrefix;

    /** registrations that contain service ID to passcode mappings */
    @NotNull
    private CasShibServiceRegistrar shibServiceRegistrar;

    /** Override createCookie to make it public instead of protected */
    private static class LocalCookieGenerator extends CookieGenerator {
        @Override
        @SuppressWarnings("PMD.UselessOverridingMethod")
        /** public instead of protected */
        public Cookie createCookie(String cookieValue) {
            return super.createCookie(cookieValue);
        }
    }

    public void addCookie(final HttpServletRequest request,
        final HttpServletResponse response, final String cookieValue) {
        // instantiate a new CookieGenerator upon every request because the
        // cookie name and path are possibly going to be different for each
        // request
        LocalCookieGenerator cookieGenerator = newCookieGeneratorInstance(request);

        if (!StringUtils.hasText(request
            .getParameter(RememberMeCredentials.REQUEST_PARAMETER_REMEMBER_ME))) {
            cookieGenerator.addCookie(response, cookieValue);
        } else {
            final Cookie cookie = cookieGenerator.createCookie(cookieValue);
            cookie.setMaxAge(this.rememberMeMaxAge);
            if (cookieGenerator.isCookieSecure()) {
                cookie.setSecure(true);
            }
            response.addCookie(cookie);
        }
    }

    public String retrieveCookieValue(final HttpServletRequest request) {
        String appName = CasShibUtil.getAppNameFromRequestURI(request,
            shibServiceRegistrar);
        if (appName == null) {
            // can't determine appName so can't determine cookie to retrieve
            return null;
        }

        final Cookie cookie = org.springframework.web.util.WebUtils.getCookie(
            request, getFullCookieName(appName));

        return cookie == null ? null : cookie.getValue();
    }

    public void setRememberMeMaxAge(final int maxAge) {
        this.rememberMeMaxAge = maxAge;
    }

    public void setCookieSecure(final Boolean cookieSecure) {
        this.cookieSecure = cookieSecure;
    }

    public void setCookieMaxAge(final Integer cookieMaxAge) {
        this.cookieMaxAge = cookieMaxAge;
    }

    public void setCookieNamePrefix(final String cookieNamePrefix) {
        this.cookieNamePrefix = cookieNamePrefix;
    }

    protected LocalCookieGenerator newCookieGeneratorInstance(
        final HttpServletRequest request) {
        String appName = CasShibUtil.getAppNameFromRequestURI(request,
            shibServiceRegistrar);
        if (appName == null) {
            throw new UnauthorizedServiceException(
                "Can't issue cookies because application name could not be determined.");
        }

        LocalCookieGenerator cookieGenerator = new LocalCookieGenerator();
        if (cookieSecure != null)
            cookieGenerator.setCookieSecure(cookieSecure.booleanValue());
        if (cookieMaxAge != null)
            cookieGenerator.setCookieMaxAge(cookieMaxAge.intValue());

        // cookie name is cookieNamePrefix + "-" + escaped(appName)
        cookieGenerator.setCookieName(getFullCookieName(appName));

        // determine the cookie path based on the requestURI
        String cookiePath = (request.getContextPath() != null ? request
            .getContextPath() : "")
            + "/shib/" + appName;

        cookieGenerator.setCookiePath(cookiePath);

        return (cookieGenerator);
    }

    public void removeCookie(final HttpServletRequest request,
        final HttpServletResponse response) {
        LocalCookieGenerator cookieGenerator = newCookieGeneratorInstance(request);
        cookieGenerator.removeCookie(response);
    }

    public void setCasShibServiceRegistrar(
        final CasShibServiceRegistrar shibServiceRegistrar) {
        this.shibServiceRegistrar = shibServiceRegistrar;
    }

    protected String getFullCookieName(String appName) {
        try {
            return (cookieNamePrefix + "-" + URLEncoder
                .encode(appName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // shouldn't ever happen, UTF-8 is a supported encoding
            throw new RuntimeException(e);
        }
    }
}
