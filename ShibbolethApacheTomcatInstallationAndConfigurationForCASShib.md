

  1. # Shibboleth, Apache, and Tomcat Installation and Configuration Guide for CASShib #
    1. ## General Shibboleth Service Provider Installation/Configuration ##
      * Installation and configuration of the Shibboleth Service provider is covered by the documentation on the [Shibboleth wiki](https://spaces.internet2.edu/display/SHIB2/Home).  Here are some relevant wiki page links:
        * [Main installation page](https://spaces.internet2.edu/display/SHIB2/Installation)
        * To understand how to configure Apache, look at the _Basic Configuration_ section on the [Windows Installer page](https://spaces.internet2.edu/display/SHIB2/NativeSPWindowsApacheInstaller) or the page relevant to your distribution ([Linux](https://spaces.internet2.edu/display/SHIB2/NativeSPLinuxSourceBuild), [Solaris](https://spaces.internet2.edu/display/SHIB2/NativeSPSolarisSourceBuild), [Mac](https://spaces.internet2.edu/display/SHIB2/NativeSPMacPortInstallation)).
        * [Install Shibboleth to protect Java Servlets](https://spaces.internet2.edu/display/SHIB2/NativeSPJavaInstall)
        * Once installed, you can look at the [main configuration page](https://spaces.internet2.edu/display/SHIB2/Configuration).
    1. ## CASShib Service Provider Configuration ##
      * `shibboleth2.xml`
        * Each service needs to have its own Shibboleth-protected entry point URLs for CAS login and validation.  This URL-to-service mapping is done here.  Here is an example:
```
    <RequestMapper type="Native">
        <RequestMap applicationId="default">
              <!-- NOTE: It's important to get name, port, and scheme right
                   here otherwise the <Host> will never match and the defaults
                   will take over. Modify native.logger
                   (log4j.category.Shibboleth.RequestMapper=DEBUG) to debug
                   matching here.

                   The string that Shibboleth passes to the regular
                   expression matcher is the path after the hostname/port
                   WITHOUT a leading slash (it doesn't include the query
                   string either).  So for example, if the URL is
                   https://myhost.edu/foo/bar?hello=world, the regular
                   expression will try to match against "foo/bar".

                   Shibboleth will check for a partial match instead of a
                   full match, meaning your regular expression doesn't
                   need to be ".*substring.*".  Just "substring" is fine.
              -->
              <Host name="casshib.ucmerced.edu" port="443" scheme="https">
                <!-- service #1 -->
                <PathRegex regex="casshib/shib/app1" applicationId="app1" authType="shibboleth" requireSession="true"/>
                <!-- service #2 -->
                <PathRegex regex="casshib/shib/app2" applicationId="app2" authType="shibboleth" requireSession="true"/>
              </Host>
        </RequestMap>
    </RequestMapper>
```
        * The `<ApplicationDefaults id="default"...>` section defines a default service if **none** of the above hosts and regular expressions match.  It is recommended that you either set this to a completely bogus (nonexistent) service to allow Shibboleth to error out or you set it to a basic service that doesn't release any sensitive attributes.  When Shibboleth errors out this can be useful debugging information because it will serve as a warning to you that the configuration is incorrect (i.e., one of your Host regular expressions isn't matching when it should).  If you don't let it error out then this configuration problem might not be obvious to you.  The other thing you can do is configure a valid default service that allows authentication but doesn't release any attributes.
        * Configure an `<ApplicationOverride>` section for each one of your services .  Here is an example:
```
      <ApplicationDefaults id="default" policyId="default"
        entityID="https://casshib.ucmerced.edu/BOGUS_SERVICE"
        homeURL="https://casshib.ucmerced.edu/BOGUS_SERVICE"
        REMOTE_USER="shibattr-uid"
        signing="false" encryption="false">
        ...existing elements...

        <!-- service #1 -->
        <ApplicationOverride id="app1"
                             entityID="https://casshib.ucmerced.edu/casshib/app1"
                             homeURL="https://app1.ucmerced.edu/"
                             REMOTE_USER="shibattr-uid">
          <!-- NOTE the cookieProps path is different for each service.
               The handlerURL needs to fall within the path of the cookie. -->
          <Sessions lifetime="28800" timeout="3600" checkAddress="false"
              handlerURL="/casshib/shib/app1/Shibboleth.sso" handlerSSL="true"
              exportLocation="/casshib/shib/app1/Shibboleth.sso/GetAssertion" 
              idpHistory="false" idpHistoryDays="7"
              cookieProps="; path=/casshib/shib/app1">
          </Sessions>
        </ApplicationOverride>

        <!-- service #2 -->
        <ApplicationOverride id="app2"
                             entityID="https://casshib.ucmerced.edu/casshib/app2"
                             homeURL="https://app2.ucmerced.edu/"
                             REMOTE_USER="shibattr-uid">
          <Sessions lifetime="28800" timeout="3600" checkAddress="false"
              handlerURL="/casshib/shib/app2/Shibboleth.sso" handlerSSL="true"
              exportLocation="/casshib/shib/app2/Shibboleth.sso/GetAssertion" 
              idpHistory="false" idpHistoryDays="7"
              cookieProps="; path=/casshib/shib/app2">
          </Sessions>
        </ApplicationOverride>    
      </ApplicationDefault>
```
        * You may want to add a local metadata file to your `MetadataProvider` configuration.  See [the section on Metadata](#Metadata.md) for an example.
      * `attribute-map.xml`
        * You must prefix all of your used Shibboleth attribute IDs with a static string so that CASShib can identity the Shibboleth attributes in the HTTP headers.  The Shibboleth Apache module creates the header name based on the `id` attribute in this file.  The prefix string is currently hard-coded in CASShib and must be "`shibattr-`".  For example `eppn` should be changed to `shibattr-eppn` and `uid` changed to `shibattr-uid`.  The `id` attribute in your `<Attribute>` elements should be changed to look like this:
```
    <Attribute name="urn:mace:dir:attribute-def:eduPersonPrincipalName" id="shibattr-eppn">
        <AttributeDecoder xsi:type="ScopedAttributeDecoder"/>
    </Attribute>
    <Attribute name="urn:mace:dir:attribute-def:uid" id="shibattr-uid"/>
```
      * `attribute-policy.xml`
        * Since you changed the attribute IDs in `attribute-map.xml` you must change the IDs in `attribute-policy.xml` as well.  The `attributeID` attributes should be changed to match the `id` attributes in `attribute-map.xml`.  For example, the rules should look like this:
```
        <afp:AttributeRule attributeID="shibattr-eppn">
            <afp:PermitValueRuleReference ref="ScopingRules"/>
        </afp:AttributeRule>
```
      * Once the Shibboleth Service Provider has been configured, you will have to restart the shibd damon running on your CASShib server.
    1. ## Metadata ##
      * To generate the metadata for each service, you can go to the Shibboleth.SSO/Metadata URL for your service.  For example, if in `shibboleth2.xml` you configured the handlerURL for `app1` to be `/casshib/shib/app1/Shibboleth.sso` and your CASShib is running on `casshib.ucmerced.edu`, then you would visit `https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/Metadata`.  This Metadata service will generate something like this:
```

<md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" entityID="https://casshib.ucmerced.edu/casshib/app1">

  <md:SPSSODescriptor protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol urn:oasis:names:tc:SAML:1.1:protocol urn:oasis:names:tc:SAML:1.0:protocol">
    <md:Extensions>
      <DiscoveryResponse xmlns="urn:oasis:names:tc:SAML:profiles:SSO:idp-discovery-protocol" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/DS" index="1"/>
    </md:Extensions>
    <md:KeyDescriptor use="signing">
      <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
        <ds:KeyName>casshib.ucmerced.edu</ds:KeyName>
        <ds:X509Data>
          <ds:X509SubjectName>CN=casshib.ucmerced.edu,O=University of California\, Merced,ST=California,C=US</ds:X509SubjectName>
          <ds:X509Certificate>CERTDATAHERE
</ds:X509Certificate>
        </ds:X509Data>
      </ds:KeyInfo>
    </md:KeyDescriptor>
    <md:KeyDescriptor use="encryption">
      <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
        <ds:KeyName>casshib.ucmerced.edu</ds:KeyName>
        <ds:X509Data>
          <ds:X509SubjectName>CN=casshib.ucmerced.edu,O=University of California\, Merced,ST=California,C=US</ds:X509SubjectName>
          <ds:X509Certificate>CERTDATAHERE
</ds:X509Certificate>
        </ds:X509Data>
      </ds:KeyInfo>
    </md:KeyDescriptor>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SLO/Artifact"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SLO/POST"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SLO/Redirect"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:SOAP" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SLO/SOAP"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/NIM/Artifact"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/NIM/POST"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/NIM/Redirect"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:SOAP" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/NIM/SOAP"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:1.0:profiles:artifact-01" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SAML/Artifact" index="6"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:1.0:profiles:browser-post" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SAML/POST" index="5"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SAML2/Artifact" index="3"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:PAOS" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SAML2/ECP" index="4"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SAML2/POST" index="1"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign" Location="https://casshib.ucmerced.edu/casshib/shib/app1/Shibboleth.sso/SAML2/POST-SimpleSign" index="2"/>
  </md:SPSSODescriptor>

</md:EntityDescriptor>
```
      * If you want this to be a federated service, as as in the [InCommon](http://www.incommonfederation.org/) federation, then you would submit this metadata information to InCommon and have it included in the InCommon metadata file that is distributed to federation members.  If this is a local-only service that you do not wish to federate, then you may add this metadata to a local-only metadata file that you create.  If you do it this way then you would include this local-only metadata file in the list of providers in the `<MetadataProvider>` element in `shibboleth2.xml`.  Here is an example (the metadata files are placed in the same directory as `shibboleth2.xml` or you could specify an absolute path):
```
        <MetadataProvider type="Chaining">
            <MetadataProvider type="XML" file="ucmerced-local-metadata.xml"/>
            <MetadataProvider type="XML" file="InCommon-metadata.xml"/>
        </MetadataProvider>
```
> > > Note that these same metadata files must be configured in your Shibboleth Identity Provider configuration files.  On your Identity Provider server, you can edit your `relying-party.xml` file to look something like this:
```
    <MetadataProvider id="ShibbolethMetadata" xsi:type="ChainingMetadataProvider" xmlns="urn:mace:shibboleth:2.0:metadata">
        <MetadataProvider id="IdPMD" xsi:type="FilesystemMetadataProvider" xmlns="urn:mace:shibboleth:2.0:metadata"
                          metadataFile="[/absolutePath]/InCommon-metadata.xml"/>

        <MetadataProvider id="IdPLocalMD" xsi:type="FilesystemMetadataProvider"
                          metadataFile="[/absolutePath]/ucmerced-local-metadata.xml"/>
    </MetadataProvider>
```
    1. ## CASShib Identity Provider ##
      * You must instruct your identity provider to release attributes for each service.  You edit the `attribute-filter.xml` file on the identity provider server to do this.  Here is an example for the `uid` attributes:
```
    <AttributeFilterPolicy>
        <PolicyRequirementRule xsi:type="basic:OR">
            <!-- CASShib Demo app1 --> <basic:Rule xsi:type="basic:AttributeRequesterString" value="https://casshib-demo.ucmerced.edu/casshib/app1" />
            <!-- CASShib Demo app2 --> <basic:Rule xsi:type="basic:AttributeRequesterString" value="https://casshib-demo.ucmerced.edu/casshib/app2" />
        </PolicyRequirementRule>
        <AttributeRule attributeID="uid">
            <PermitValueRule xsi:type="basic:ANY"/>
        </AttributeRule>
    </AttributeFilterPolicy>
```
    1. ## Apache Configuration ##
      * Install Apache v2.2 on your CASShib server.
      * Edit `conf/httpd.conf` and the last few lines of the file should look like this:
```
# JK Connector for AJP proxying
# Note this has to appear before the Include for httpd-ssl.conf
Include conf/extra/httpd-jk-init.conf
# Only needed if you want to debug CASShib requests on the non-SSL port.
Include conf/extra/httpd-jk-mount.conf

# Secure (SSL/TLS) connections
Include conf/extra/httpd-ssl.conf
#
# Note: The following must must be present to support
#       starting without SSL on platforms with no /dev/random equivalent
#       but a statically compiled-in mod_ssl.
#
<IfModule ssl_module>
SSLRandomSeed startup builtin
SSLRandomSeed connect builtin
</IfModule>

Include [/absolutePathToYourShibbolethServiceProvider]/etc/shibboleth/apache22.config/etc/shibboleth/apache22.config
```
      * (Optonal) Edit `[/absolutePathToYourShibbolethServiceProvider]/etc/shibboleth/apache22.config` and comment out or remove the `<Location>` sections in this file.  This is optional because while we are not using these URLs, it doesn't hurt anything to have it here (other than you will get Shibboleth errors when you go to the /secure or /cgi-bin URLs instead of Apache errors).  For example, it should now look like:
```
#<Location /secure>
#  AuthType shibboleth
#  ShibRequireSession On
#  ShibUseHeaders On
#  require valid-user
#</Location>
#<Location /cgi-bin>
#  AuthType shibboleth
#  ShibRequireSession On
#  ShibUseHeaders On
#  require valid-user
#</Location>
```
      * Edit `conf/extra/httpd-ssl.conf` and at the bottom, just before the `</VirtualHost>`, add the following rules to protect your services:
```
UseCanonicalName On

# JK Connector
# make sure httpd-jk-init.conf is loaded before this conf file
Include conf/extra/httpd-jk-mount.conf

# Protect the login and Shibboleth.sso URLs.
<Location /casshib/shib/*/login>
  AuthType shibboleth
  ShibRequireSession On
  ShibUseHeaders On
  require valid-user
</Location>
<Location /casshib/shib/*/Shibboleth.sso>
  AuthType shibboleth
  ShibRequireSession On
  ShibUseHeaders On
  require valid-user
</Location>
<Location /casshib/shib/*/Shibboleth.sso/*>
  AuthType shibboleth
  ShibRequireSession On
  ShibUseHeaders On
  require valid-user
</Location>
```
      * Create `httpd-jk-init.conf`.
```
# note httpd-jk-init.conf needs to be loaded before httpd-ssl.conf

LoadModule    jk_module  modules/mod_jk.so

<IfModule jk_module>
  JkWorkersFile conf/extra/workers.properties
  JkShmFile     logs/mod_jk.shm
  JkLogFile     logs/mod_jk.log
  JkLogLevel    info
  JkLogStampFormat "[%a %b %d %H:%M:%S %Y] "
</IfModule>
```
      * Create `httpd-jk-mount.conf`.
```
<IfModule jk_module>

  JkMount /casshib/* worker1
  JkUnMount /casshib/shib/*/Shibboleth.sso worker1
  JkUnMount /casshib/shib/*/Shibboleth.sso/* worker1

</IfModule>
```
      * Create `worker.properties`.
```
# Define 1 real worker using ajp13
worker.list=worker1
# Set properties for worker1 (ajp13)
worker.worker1.type=ajp13
worker.worker1.host=localhost
worker.worker1.port=8017 # This is Tomcat's AJP port number which is configured in Tomcat's server.xml file.
```
    1. ## Tomcat Configuration ##
      * Edit `conf/server.xml` (this applies to Tomcat v5.5)
        * Since you're using AJP you can comment out the HTTP and SSL HTTP connectors.  You may want to keep the nonSSL connector since it is useful for debugging purposes, but if you do this make sure you add `address="127.0.0.1"` to the Connector element otherwise you will be opening yourself up to header spoofing attacks.  Here is an example:
```
    <Connector port="8585" protocol="HTTP/1.1"
               connectionTimeout="20000"
               address="127.0.0.1" />
```
        * Configure your AJP connector to look something like this:
```
    <!-- It's important for CASShib to have tomcatAuthentication="false".
         It's also important to restrict connections from localhost only.
         This will prevent outsiders from trying to spoof headers. -->
    <Connector port="8017"
               enableLookups="false" protocol="AJP/1.3"
               tomcatAuthentication="false"
               address="127.0.0.1" />
```
> > > > The port number can be anything but make it match with what you put in `workers.properties`.  Note that, as the comment suggests, it is important to include the `tomcatAuthentication="false"` and `address="127.0.0.1"` attributes.