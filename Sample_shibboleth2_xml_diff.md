
```
*** shibboleth-sp/etc/shibboleth/shibboleth2.xml.ORIG	Tue Apr  7 10:42:44 2009
--- shibboleth-sp/etc/shibboleth/shibboleth2.xml	Tue Apr  7 14:24:27 2009
***************
*** 24,30 ****
              The port and scheme can usually be omitted, so the HTTP request's port and
              scheme will be used.
              -->
!             <Site id="1" name="sp.example.org"/>
          </ISAPI>
      </InProcess>
  
--- 24,30 ----
              The port and scheme can usually be omitted, so the HTTP request's port and
              scheme will be used.
              -->
!             <Site id="1" name="casshib-demo.ucmerced.edu"/>
          </ISAPI>
      </InProcess>
  
***************
*** 59,71 ****
--- 59,80 ----
              Apache's ServerName and Port directives or the IIS Site name in the <ISAPI> element
              below.
              -->
+             <!--
              <Host name="sp.example.org">
                  <Path name="secure" authType="shibboleth" requireSession="true"/>
              </Host>
+             -->
              <!-- Example of a second vhost mapped to a different applicationId. -->
              <!--
              <Host name="admin.example.org" applicationId="admin" authType="shibboleth" requireSession="true"/>
              -->
+             
+             <Host name="casshib-demo.ucmerced.edu" port="9494" scheme="https">
+                 <!-- service #1 -->
+                 <PathRegex regex="casshib/shib/app1" applicationId="app1" authType="shibboleth" requireSession="true"/>
+                 <!-- service #2 -->
+                 <PathRegex regex="casshib/shib/app2" applicationId="app2" authType="shibboleth" requireSession="true"/>
+               </Host>
          </RequestMap>
      </RequestMapper>
  
***************
*** 102,109 ****
              -->
  
              <!-- Default example directs to a specific IdP's SSO service (favoring SAML 2 over Shib 1). -->
              <SessionInitiator type="Chaining" Location="/Login" isDefault="true" id="Intranet"
!                     relayState="cookie" entityID="https://idp.example.org/shibboleth">
                  <SessionInitiator type="SAML2" defaultACSIndex="1" acsByIndex="false" template="bindingTemplate.html"/>
                  <SessionInitiator type="Shib1" defaultACSIndex="5"/>
              </SessionInitiator>
--- 111,119 ----
              -->
  
              <!-- Default example directs to a specific IdP's SSO service (favoring SAML 2 over Shib 1). -->
+             <!-- change entityID to your identity provider entity ID -->
              <SessionInitiator type="Chaining" Location="/Login" isDefault="true" id="Intranet"
!                     relayState="cookie" entityID="urn:mace:incommon:ucmerced.edu">
                  <SessionInitiator type="SAML2" defaultACSIndex="1" acsByIndex="false" template="bindingTemplate.html"/>
                  <SessionInitiator type="Shib1" defaultACSIndex="5"/>
              </SessionInitiator>
***************
*** 112,118 ****
              <SessionInitiator type="Chaining" Location="/WAYF" id="WAYF" relayState="cookie">
                  <SessionInitiator type="SAML2" defaultACSIndex="1" acsByIndex="false" template="bindingTemplate.html"/>
                  <SessionInitiator type="Shib1" defaultACSIndex="5"/>
!                 <SessionInitiator type="WAYF" defaultACSIndex="5" URL="https://wayf.example.org/WAYF"/>
              </SessionInitiator>
  
              <!-- An example supporting the new-style of discovery service. -->
--- 122,129 ----
              <SessionInitiator type="Chaining" Location="/WAYF" id="WAYF" relayState="cookie">
                  <SessionInitiator type="SAML2" defaultACSIndex="1" acsByIndex="false" template="bindingTemplate.html"/>
                  <SessionInitiator type="Shib1" defaultACSIndex="5"/>
!                 <!-- change to your WAYF page -->
!                 <SessionInitiator type="WAYF" defaultACSIndex="5" URL="https://wayf.incommonfederation.org/InCommon/WAYF"/>
              </SessionInitiator>
  
              <!-- An example supporting the new-style of discovery service. -->
***************
*** 218,223 ****
--- 229,239 ----
              <!--
              <MetadataProvider type="XML" file="partner-metadata.xml"/>
              -->
+             
+             <!-- this contains the service metadata for app1 and app2 -->
+             <MetadataProvider type="XML" file="casshib-demo-metadata.xml"/>
+             <!-- this contains the metadata for our identity provider -->
+             <MetadataProvider type="XML" file="InCommon-metadata.xml"/>
          </MetadataProvider>
  
          <!-- Chain the two built-in trust engines together. -->
***************
*** 241,246 ****
--- 257,289 ----
          <!-- Example of a second application (using a second vhost) that has a different entityID. -->
          <!-- <ApplicationOverride id="admin" entityID="https://admin.example.org/shibboleth"/> -->
  
+         <ApplicationOverride id="app1"
+                              entityID="https://casshib-demo.ucmerced.edu/casshib/app1"
+                              homeURL="https://casshib-demo.ucmerced.edu:9494/app1/"
+                              REMOTE_USER="shibattr-uid">
+           <!-- NOTE the cookieProps path is different for each service.
+                The handlerURL needs to fall within the path of the cookie. -->
+           <Sessions lifetime="28800" timeout="3600" checkAddress="false"
+               handlerURL="/casshib/shib/app1/Shibboleth.sso" handlerSSL="true"
+               exportLocation="/casshib/shib/app1/Shibboleth.sso/GetAssertion" 
+               idpHistory="false" idpHistoryDays="7"
+               cookieProps="; path=/casshib/shib/app1">
+           </Sessions>
+         </ApplicationOverride>
+ 
+         <!-- service #2 -->
+         <ApplicationOverride id="app2"
+                              entityID="https://casshib-demo.ucmerced.edu/casshib/app2"
+                              homeURL="https://casshib-demo.ucmerced.edu:9494/app2/"
+                              REMOTE_USER="shibattr-uid">
+           <Sessions lifetime="28800" timeout="3600" checkAddress="false"
+               handlerURL="/casshib/shib/app2/Shibboleth.sso" handlerSSL="true"
+               exportLocation="/casshib/shib/app2/Shibboleth.sso/GetAssertion" 
+               idpHistory="false" idpHistoryDays="7"
+               cookieProps="; path=/casshib/shib/app2">
+           </Sessions>
+         </ApplicationOverride>
+     
      </ApplicationDefaults>
      
      <!-- Each policy defines a set of rules to use to secure messages. -->
```