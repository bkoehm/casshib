/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package edu.ucmerced.cas.authentication.principal;

import org.jasig.cas.authentication.principal.WebApplicationService;

/**
 * An extension to the web application service interface that allows for the
 * configuration of an application name or a passcode.  This is used for
 * additional service authentication during CASShib requests.
 *
 * @author Brian Koehmstedt
 * @version $Revision$ $Date$
 * @since 3.3.1a
 */
public interface CasShibService extends WebApplicationService
{
  public String getAppNameOrPasscode();
}
