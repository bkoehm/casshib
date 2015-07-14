

The diff files show the differences between the original stock configuration file that came with the distribution and the changes that were made for CASShib.
<p />
Platform: Solaris 10 x86, gcc 3.4.5

  * # Apache HTTP Server 2.2.11 #
    * `configure` script ran with the following options:
```
./configure \
--prefix=$APACHE_HOME\
--enable-so \
--enable-mods-shared=all \
--enable-ssl \
--with-ssl=/opt/csw \
--enable-proxy=shared        
```
> > > `/opt/csw` contains the [OpenSSL](http://www.openssl.org/) installation.
    * [$APACHE\_HOME/conf/httpd.conf](Sample_httpd_conf.md) ([diff](Sample_httpd_conf_diff.md))
    * [$APACHE\_HOME/conf/extra/httpd-jk-init.conf](Sample_httpd_jk_init_conf.md)
    * [$APACHE\_HOME/conf/extra/httpd-jk-mount.conf](Sample_httpd_jk_mount_conf.md)
    * [$APACHE\_HOME/conf/extra/workers.properties](Sample_workers_properties.md)
    * [$APACHE\_HOME/conf/extra/httpd-ssl.conf](Sample_httpd_ssl_conf.md) ([diff](Sample_httpd_ssl_conf_diff.md))
> > > <p />
  * # JK Connector 1.2.28 (mod\_jk) #
    * `configure` script ran with the following options:
```
./configure --with-apxs=$APACHE_HOME/bin/apxs
```
> > > <p />
  * # Shibboleth 2.1 #
    * `configure` script ran with the following options:
```
./configure \
--with-log4shib=$LOG4SHIB_LOCATION \
--prefix=$SHIBSP_HOME \
--with-xerces=$XERCES_C_LOCATION \
--with-xmlsec=$XMLSEC_LOCATION \
--with-xmltooling=$XMLTOOLING_LOCATION \
--with-saml=$SAML_LOCATION \
--with-apxs22=$APACHE_HOME/bin/apxs \
--enable-apache-22 \
--disable-doxygen \
--with-openssl=/opt/csw \
-C
```
    * For Solaris 10 x86 gcc, a config.h change was necessary to get Shibboleth 2.1 to compile:
      1. Commented out `#define HAVE_CTIME_R_3 1` so the line now looks like `/* #undef #define HAVE_CTIME_R_3 */`
      1. Changed the line that is `/* #undef HAVE_CTIME_R_2 */` to `#define HAVE_CTIME_R_2 1`
    * [$SHIBSP\_HOME/etc/shibboleth/shibboleth2.xml](Sample_shibboleth2_xml.md) ([diff](Sample_shibboleth2_xml_diff.md))
    * [$SHIBSP\_HOME/etc/shibboleth/attribute-map.xml](Sample_attribute_map_xml.md) ([diff](Sample_attribute_map_xml_diff.md))
    * [$SHIBSP\_HOME/etc/shibboleth/attribute-policy.xml](Sample_attribute_policy_xml.md) ([diff](Sample_attribute_policy_xml_diff.md))
    * [$SHIBSP\_HOME/etc/shibboleth/casshib-demo-metadata.xml](Sample_casshib_demo_metadata_xml.md)
    * Your Identity Provider must be configured to read in this same `casshib-demo-metadata.xml` metadata file.  See this section on  [Metadata](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib#Metadata.md).
    * Don't forget to also configure your Identity Provider to release the attributes to your services.  See this section on the [Identity Provider](ShibbolethApacheTomcatInstallationAndConfigurationForCASShib#CASShib_Identity_Provider.md) configuration.
> > > <p />
  * # Tomcat 6.0.18 #
    * [$TOMCAT\_HOME/conf/server.xml](Sample_server_xml.md) ([diff](Sample_server_xml_diff.md))
> > > <p />
  * # CASShib #
    * [$CASSHIB\_HOME//WEB-INF/classes/casshib-service-registrations.xml](Sample_casshib_service_registrations_xml.md)
> > > <p />
  * # Demo Test Application #
    * [Download casshib-demo-app-1.0.0.war](http://code.google.com/p/casshib/downloads/list) (don't put it in your Tomcat webapps directory yet).
    * Copy the file to something like app1.war.
    * Move app1.war into $TOMCAT\_HOME/webapps
    * Edit $TOMCAT\_HOME/webapps/app1/web.xml and change the URLs to reflect your CASShib server URL, your application name (for login and logout) and your application passcode (for serviceValidate and samlValidate).  Your application name and passcode must match what is in `casshib-service-registrations.xml`.
    * You can repeat these steps if you would like to test with multiple applications running on the same application server.  (i.e., instead of app1.war, copy over an app2.war and edit the web.xml in $TOMCAT\_HOME/webapps/app2/web.xml).
    * Note that by default web.xml is configured to use the Saml Validation Filter.  You can easily switch to using the CAS Validation Filter by changing the `<filter-mapping>` towards the end of the file.