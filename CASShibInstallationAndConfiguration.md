

  1. # CASShib Installation #
    1. [Download CASShib](http://code.google.com/p/casshib/downloads/list).  The releases contain both binaries and source.  The version of CASShib matches with the version of the CAS server plus a letter to indicate the CASShib release.  For example, if you download `casshib-server-3.3.1a-release.zip`, then this means this is CAS 3.3.1 with version A CASShib modifications.
> > The release files are [Maven](http://maven.apache.org/) modules that follow the Maven conventions of the CAS 3 project.
    1. Unzip or untar the downloaded file.
    1. Change directory into `casshib-server-x.x.xx/modules`.  In here is the `casshib-server-webapp-x.x.xx.war` file.  Rename this file to `casshib.war` and copy `casshib.war` into your `$TOMCAT_HOME/webapps directory` where $TOMCAT\_HOME is the location of your installed Tomcat application server (see [Tomcat Configuration](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib#Tomcat_Configuration.md) instructions).  After a few seconds Tomcat should automatically unwar the file and then you should have a `$TOMCAT_HOME/webapps/casshib directory`.  This is where CASShib is installed.
  1. # Methods for editing configuration #
> > There are two ways to change the CAS configuration files.  Pick the method that you prefer.  The documentation on the [CAS Wiki](http://www.ja-sig.org/wiki/display/CAS/Home) favors Method #1 but Method #2 is simpler (with one caveat).
      * Method #1: Repackaging the WAR and redeploying
        * Change into the `casshib-server-x.x.xx/casshib-server-webapp` directory.
        * Edit the files you wish to edit in `src/main/WEB-INF`.  If you don't see the file you wish to edit you need to copy it from the `$TOMCAT_HOME/webapps/casshib/WEB-INF` directory into the `src/main/WEB-INF` directory and edit the file.
        * Repackage the WAR file by issuing the following Maven command (you must have maven installed):
> > > > > `mvn -Dmaven.test.skip=true package`
        * Copy `target/casshib.war` into `$TOMCAT_HOME/webapps`.  Tomcat then should unwar the file again and your changes should be deployed.
      * Method #2: Editing directly
        * You may also edit the files directly in `$TOMCAT_HOME/webapps/casshib/WEB-INF` and restart Tomcat after a configuration change.  The one disadvantage to this method is if you make a configuration change that creates a dependency on a new JAR file from the maven repository, you will have to copy that JAR file manually into `$TOMCAT_HOME/webapps/casshib/WEB-INF/lib`.
  1. # Configuring CASShib #
    * ## Configuring Shibboleth, Apache, and Tomcat first ##
      * The bulk of the configuration effort is not in configuring CASShib but in configuring Shibboleth, Apache, and Tomcat.  Consult the   	 [Shibboleth, Apache, Tomcat Configuration Guide](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib.md).
    * ## Running in default mode or as a primary SSO server ##
      * CASShib comes pre-configured with a set of defaults that makes the CASShib server run just in Shibboleth service mode and disables the stock CAS URLs (i.e, CASShib won't serve up any login forms, instead it will always delegate to Shibboleth for this).  See [How are things different from the regular CAS server](CASShibExplained#CASShib's_placement_in_your_SSO_architecture.md) for a more detailed explanation of this.  If you wish to run your CASShib server as a primary CAS server that also serves up a login form, then you will have to make configuration changes to enable this.  This will mostly involve uncommenting the stock configuration lines in `cas-servlet.xml` and then following the traditional configuration instructions in the [CAS User Manual](http://www.ja-sig.org/wiki/display/CASUM/Home) and on the [CAS Wiki](http://www.ja-sig.org/wiki/display/CAS/Home).  Note that running CASShib in this way (as a primary SSO server) has yet to be tested.
    * ## Registering services: casshib-service-registrations.xml ##
      * The most common configuration change will be to the `WEB-INF/classes/casshib-service-registrations.xml` file.  This file contains a list of services that are registered to use this CASShib server.  In order to use in concert with Shibboleth, services must be registered since CAS needs to know certain information about the service.
        * Here is an example registration in `WEB-INF/classes/casshib-service-registrations.xml`:
```
  <service id="https://casshib.ucmerced.edu/casshib/app1"
           appname="app1"
           passcode="96306" />
```
          * The `id` value must match the service entityID in the Shibboleth metadata file.  Please consult the [Metadata](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib#Metadata.md) section in the [Shibboleth, Apache, Tomcat Configuration Guide](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib.md).  For example, each service is going to have metadata that starts with something like this:
```
<md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" entityID="https://casshib.ucmerced.edu/casshib/app1">
```
> > > > > > Note that the `entityID` value matches the `id` attribute value in `WEB-INF/classes/casshib-service-registrations.xml`.
          * The `appname` attribute value will appear in the CASShib login and logout URLs for this service.  See the note about [URLs](CASShibExplained#URLs.md).  For example, since `appname` is `app1`, the following URLs will be used for this service: For login, `https:///myserver.edu/casshib/shib/app1/login` and for logout, `https://myserver.edu/casshib/shib/app1/logout`.
          * The `passcode` attribute contains a secret passcode that will only be given to the service application developers or administrators.  This passcode will be embeded in the URLs that the application developers will use to validate the CAS tickets.  This validation URL is often set in the CAS client's configuration which is different for each CAS client implementation.  For example, in the [JA-SIG Java Client](http://www.ja-sig.org/wiki/display/CASC/JA-SIG+Java+Client), the login and validation URLs are configured in the `web.xml` file.  The validation URL for the above example would be: `https://myserver.edu/casshib/shib/96306/serviceValidate` or if the client is using SAML validation instead, it would be `https://myserver.edu/casshib/shib/96306/samlValidate`.  The purpose of this passcode is to help ensure that attributes from Shibboleth are only being released to the applications that are authorized to see such information.  See [Service registration and security](http://code.google.com/p/casshib/wiki/CASShibExplained?ts=1239060016&updated=CASShibExplained#Service_registration_and_security) for more explanation and also note the caveats in that section regarding the relative weakness of passcode security.