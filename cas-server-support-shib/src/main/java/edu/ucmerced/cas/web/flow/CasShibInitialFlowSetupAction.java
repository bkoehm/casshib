/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.flow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.inspektr.common.ioc.annotation.NotEmpty;
import org.inspektr.common.ioc.annotation.NotNull;
import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucmerced.cas.web.support.CasShibCookieRetrievingCookieGenerator;

/**
 * Class to automatically set the paths for the CookieGenerators.
 * 
 * <p/>
 * 
 * Note: This is technically not threadsafe, but because its overriding with a
 * constant value it doesn't matter.
 * 
 * <p/>
 * 
 * Note: As of CAS 3.1, this is a required class that retrieves and exposes the
 * values in the two cookies for subclasses to use.
 * 
 * <p/>
 * 
 * Modified from original to use the
 * <code>CasShibCookieRetrievingCookieGenerator</code>.
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.flow.InitialFlowSetupAction
 * 
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision:$ $Date:$
 * @since 3.3.1a
 */
public class CasShibInitialFlowSetupAction extends AbstractAction {

    /** CookieGenerator for the Warnings. */
    @NotNull
    private CasShibCookieRetrievingCookieGenerator warnCookieGenerator;

    /** CookieGenerator for the TicketGrantingTickets. */
    @NotNull
    private CasShibCookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    /** Extractors for finding the service. */
    @NotEmpty
    private List<ArgumentExtractor> argumentExtractors;

    /** Boolean to note whether we've set the values on the generators or not. */
    // private boolean pathPopulated = false;
    protected Event doExecute(final RequestContext context) throws Exception {
        final HttpServletRequest request = WebUtils
            .getHttpServletRequest(context);
        /* this is handled by the CasShib cookie generator */
        /**
         * <pre>
         * if (!this.pathPopulated) {
         *     final String contextPath = context.getExternalContext().getContextPath();
         *     final String cookiePath = StringUtils.hasText(contextPath) ? contextPath
         *         : &quot;/&quot;;
         *     logger.info(&quot;Setting path for cookies to: &quot; + cookiePath);
         *     this.warnCookieGenerator.setCookiePath(cookiePath);
         *     this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);
         *     this.pathPopulated = true;
         * }
         * </pre>
         */

        context.getFlowScope().put(
            "ticketGrantingTicketId",
            this.ticketGrantingTicketCookieGenerator
                .retrieveCookieValue(request));
        context.getFlowScope().put(
            "warnCookieValue",
            Boolean.valueOf(this.warnCookieGenerator
                .retrieveCookieValue(request)));

        final Service service = WebUtils.getService(this.argumentExtractors,
            context);

        if (service != null && logger.isDebugEnabled()) {
            logger.debug("Placing service in FlowScope: " + service.getId());
        }

        context.getFlowScope().put("service", service);

        return result("success");
    }

    public void setTicketGrantingTicketCookieGenerator(
        final CasShibCookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setWarnCookieGenerator(
        final CasShibCookieRetrievingCookieGenerator warnCookieGenerator) {
        this.warnCookieGenerator = warnCookieGenerator;
    }

    public void setArgumentExtractors(
        final List<ArgumentExtractor> argumentExtractors) {
        this.argumentExtractors = argumentExtractors;
    }
}
