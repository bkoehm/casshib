

  1. # About CASShib #
    1. ## What is it? ##
> > > CASShib "Shibbolizes" the CAS server and enables end applications to get authentication information from CAS rather than the Shibboleth Service Provider.
    1. ## Why use it? ##
> > > CASShib was designed as an alternative to deploying the Shibboleth service provider for each application.  CASShib's purpose is to:
        * Leverage Shibboleth's sophisticated attribute release policy functionality to enable attribute releasing to services in the local environment.
        * Offer the chance for local applications to easily become federated services.
> > > <p />
> > > Why not just deploy the Shibboleth Service provider software for each application that wishes to consume attributes or to federate?
        * If your organization is already using CAS for local single sign on, then it is beneficial to continue having your local applications use their CAS clients for authentication rather than deploy the Shibboleth service provider software.  If applications are already using CAS they will not have to make major code changes to take advantage of attribute release or federation.
        * Deploying the Shibboleth service provider for end applications can be a cumbersome process.  A key goal of CASShib is to ease the difficulty of deployment for end applications.  You only have to deploy the Shibboleth Service Provider software once on the CASShib server.
> > > > <p />
> > > > For end applications, there is no need to:
          * Install an Apache or IIS web server in front of the application server.
          * Configure the Apache or IIS Shibboleth module.
          * Setup AJP or other proxying agent for communication between Apache/IIS and the end application server.
          * Install the Shibboleth daemon (shibd).
          * Edit the Shibboleth server provider configuration files.
          * Upgrade the Shibboleth service provider software when new versions are released.

> > > Note there is a **security disadvantage** that should be taken into consideration.  See the note about this in the [Service registration and security](#Service_registration_and_security.md) section.
    1. ## What you will need ##
      * [Shibboleth](http://shibboleth.internet2.edu) 2 Service Provider
      * [Apache](http://httpd.apache.org/) or IIS web server.  Operating with Apache v2.2+ is recommended.  IIS is untested and undocumented.
      * An AJP proxying module for your web server.  [JK Connector (mod\_jk)](http://tomcat.apache.org/connectors-doc/) is documented here, but [mod\_proxy\_ajp](http://httpd.apache.org/docs/2.2/mod/mod_proxy_ajp.html) should work as well.
      * An application server.  Tomcat v5.5+ is recommended and is what is documented here.
> > > <p />
  1. # How does it work? #

> The CASShib server is Shibbolized and accepts authentication credentials from the Shibboleth service provider.
    1. ### CASShib's placement in your SSO architecture ###
> > > CASShib is intended to run as a secondary "behind the scenes" CAS server running in trusted mode, meaning it accepts authentication credentials from Shibboleth and never serves up a login page directly. It may be possible to use it as a primary CAS server as well but this hasn't been tested yet.  When operating in this manner, CASShib delegates the login task to Shibboleth which in turn delegates to your local SSO server (CAS or something else).  Instead of getting authentication information from a web form, CASShib is getting the authentication credentials from Shibboleth.
    1. ### Shibboleth's role ###
> > > The Shibboleth Apache or IIS module will embed the authentication credentials in the HTTP headers and then proxy this request to the CASShib server using the AJP protocol or some other proxying mechanism.
    1. ### CASShib's role ###
> > > Once Shibboleth embeds the authentication information in the HTTP headers, CASShib will read in these HTTP headers and extract the principal name (i.e., username) and attributes and generate a ticket.  CASShib will then redirect the user's web browser to the end application with the ticket number.  The end application will then contact the CASShib server to validate the ticket.  As long as the ticket number was issued for the service requesting validation CASShib will present the principal name and attributes to the end application.
    1. ### Using a CAS client ###
> > > If the end application is using a CAS client then the CAS client must be "attribute aware."  The CAS2 Protocol specification does not specify a way to release information in the protocol, but the JA-SIG Java Client and potentially others support an unofficial extension to the CAS2 protocol for attribute information.  Other clients may have to be modified to support this, or the end application may be able to get at the raw XML data via the client and parse out the attribute information on their own.  Also note that CAS3 supports the SAML1.1 protocol which does officially support attribute information in the validation response, but the CAS client being used needs SAML1.1 support to do this (the JA-SIG Java client supports this).
    1. ### How are things different from the regular CAS server? ###
      * #### Service registration and security ####
> > > > Since we don't want people to fake being a service and grabbing attribute information for a user, CASShib mandates that services must be registered with the CAS Services Manager.  This registration is done with a CASShib registration XML file.  In this XML file there is a passcode associated with each service and this service must embed this passcode in the URL when they validate a ticket (this is easily done by configuring the validation URL in the client configuration).
> > > > <p />
> > > > This is weak security compared to just using the Shibboleth Service Provider directly to Shibbolize your applications.  CASShib uses a simple passcode to allow services in while Shibboleth uses digital certificates.  This is a disadvantage to CASShib and should be taken into consideration when you use CASShib to federate an application instead of just the Shibboleth Service Provider.
> > > > <p />
> > > > Additionally, CASShib has not yet been vetted or audited from a security standpoint.  It is plausible that holes may exist (other than stealing the passcode) such that unauthorized applications may figure out a way to fake being an authorized application.
> > > > <p />
> > > > Note that from a security-standpoint when you Shibbolize any application you should firewall or only allow localhost connections to the application server (see [Tomcat Configuration](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib#Tomcat_Configuration.md) for specifics of how to limit connections to only localhost).  All connections should first go through the Apache or IIS web server so that it passes through the Shibboleth module and gets properly proxied to your application server.  **This is important** because otherwise people can make connections directly to the application server (i.e. bypass Shibboleth protection) and spoof the HTTP headers and pretend to be anyone they want to be.  This is one reason to use CASShib: you only have to protect  CASShib in this way and not have to worry about firewalls or localhost-only configurations for all the other application servers in your organization.
      * #### URLs ####
> > > > The way the Shibboleth Apache/IIS module works is it identifies services based on the URL.  URLs are configured in the Shibboleth configuration files.  In order to allow CASShib to authenticate users on a per-service basis each service needs its own login URL.  There are also unique per-service validator URLs.<p />
> > > > For example:
          * Instead of `/cas/login`, the login URL for a serviced named `myservice` would be: `/casshib/shib/myservice/login`.
          * Instead of `/cas/serviceValidate`, the URL would be: `/casshib/shib/PASSCODE/serviceValidate` where `PASSCODE` is a code only given to the application developer who configures the CAS client.  The web browser never sees this passcode because ticket validation happens directly between the CAS client running on the application side and the CAS server.<p />
      * #### Cookies and sessions ####
> > > > Since each service gets a potentially different set of attributes from Shibboleth, CAS must not cache the authentication session across all services.  So CAS needs to issue a different session cookie for each service.
> > > > For example, for `myservice`, the cookie name would be `CASTGC-myservice` and the cookie path would be `/casshib/shib/myservice`.