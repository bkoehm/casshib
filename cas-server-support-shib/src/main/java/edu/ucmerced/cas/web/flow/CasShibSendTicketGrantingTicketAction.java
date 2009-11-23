/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package edu.ucmerced.cas.web.flow;

import org.inspektr.common.ioc.annotation.NotNull;
import org.jasig.cas.CentralAuthenticationService;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucmerced.cas.web.support.CasShibCookieRetrievingCookieGenerator;

/**
 * Action that handles the TicketGrantingTicket creation and destruction. If the
 * action is given a TicketGrantingTicket and one also already exists, the old
 * one is destroyed and replaced with the new one. This action always returns
 * "success".
 * 
 * <p/>
 * 
 * Modified from original to use the
 * <code>CasShibCookieRetrievingCookieGenerator</code>.
 * 
 * <p/>
 * 
 * Derived from: org.jasig.cas.web.flow.SendTicketGrantingTicketAction
 * 
 * @author Scott Battaglia
 * @author modified by Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public class CasShibSendTicketGrantingTicketAction extends AbstractAction {

    @NotNull
    private CasShibCookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;

    /** Instance of CentralAuthenticationService. */
    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    protected Event doExecute(final RequestContext context) {
        final String ticketGrantingTicketId = WebUtils
            .getTicketGrantingTicketId(context);
        final String ticketGrantingTicketValueFromCookie = (String) context
            .getFlowScope().get("ticketGrantingTicketId");

        if (ticketGrantingTicketId == null) {
            return success();
        }

        this.ticketGrantingTicketCookieGenerator.addCookie(WebUtils
            .getHttpServletRequest(context), WebUtils
            .getHttpServletResponse(context), ticketGrantingTicketId);

        if (ticketGrantingTicketValueFromCookie != null
            && !ticketGrantingTicketId
                .equals(ticketGrantingTicketValueFromCookie)) {
            this.centralAuthenticationService
                .destroyTicketGrantingTicket(ticketGrantingTicketValueFromCookie);
        }

        return success();
    }

    public void setTicketGrantingTicketCookieGenerator(
        final CasShibCookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator) {
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;
    }

    public void setCentralAuthenticationService(
        final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }
}
