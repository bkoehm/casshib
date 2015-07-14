Note that the certificate data has been removed from this metadata.  Replace "CERTDATAHERE" with the certificate data for your own services.

```
<md:EntitiesDescriptor
    xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:ds="http://www.w3.org/2000/09/xmldsig#"
    xmlns:shibmd="urn:mace:shibboleth:metadata:1.0"
    Name="urn:mace:casshib-demo">

<!-- generated from https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/Metadata -->

<md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" entityID="https://casshib-demo.ucmerced.edu/casshib/app1">

  <md:SPSSODescriptor protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol urn:oasis:names:tc:SAML:1.1:protocol urn:oasis:names:tc:SAML:1.0:protocol">
    <md:Extensions>
      <DiscoveryResponse xmlns="urn:oasis:names:tc:SAML:profiles:SSO:idp-discovery-protocol" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/DS" index="1"/>
    </md:Extensions>
    <md:KeyDescriptor use="signing">
      <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
        <ds:KeyName>casshib-demo</ds:KeyName>
        <ds:X509Data>
          <ds:X509SubjectName>CN=casshib-demo</ds:X509SubjectName>
          <ds:X509Certificate>CERTDATAHERE
</ds:X509Certificate>
        </ds:X509Data>
      </ds:KeyInfo>
    </md:KeyDescriptor>
    <md:KeyDescriptor use="encryption">
      <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
        <ds:KeyName>casshib-demo</ds:KeyName>
        <ds:X509Data>
          <ds:X509SubjectName>CN=casshib-demo</ds:X509SubjectName>
          <ds:X509Certificate>CERTDATAHERE
</ds:X509Certificate>
        </ds:X509Data>
      </ds:KeyInfo>
    </md:KeyDescriptor>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SLO/Artifact"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SLO/POST"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SLO/Redirect"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:SOAP" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SLO/SOAP"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/NIM/Artifact"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/NIM/POST"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/NIM/Redirect"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:SOAP" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/NIM/SOAP"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:1.0:profiles:artifact-01" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SAML/Artifact" index="6"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:1.0:profiles:browser-post" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SAML/POST" index="5"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SAML2/Artifact" index="3"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:PAOS" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SAML2/ECP" index="4"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SAML2/POST" index="1"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app1/Shibboleth.sso/SAML2/POST-SimpleSign" index="2"/>
  </md:SPSSODescriptor>

</md:EntityDescriptor>

<!-- generated from https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/Metadata -->

<md:EntityDescriptor xmlns:md="urn:oasis:names:tc:SAML:2.0:metadata" entityID="https://casshib-demo.ucmerced.edu/casshib/app2">

  <md:SPSSODescriptor protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol urn:oasis:names:tc:SAML:1.1:protocol urn:oasis:names:tc:SAML:1.0:protocol">
    <md:Extensions>
      <DiscoveryResponse xmlns="urn:oasis:names:tc:SAML:profiles:SSO:idp-discovery-protocol" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/DS" index="1"/>
    </md:Extensions>
    <md:KeyDescriptor use="signing">
      <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
        <ds:KeyName>casshib-demo</ds:KeyName>
        <ds:X509Data>
          <ds:X509SubjectName>CN=casshib-demo</ds:X509SubjectName>
          <ds:X509Certificate>CERTDATAHERE
</ds:X509Certificate>
        </ds:X509Data>
      </ds:KeyInfo>
    </md:KeyDescriptor>
    <md:KeyDescriptor use="encryption">
      <ds:KeyInfo xmlns:ds="http://www.w3.org/2000/09/xmldsig#">
        <ds:KeyName>casshib-demo</ds:KeyName>
        <ds:X509Data>
          <ds:X509SubjectName>CN=casshib-demo</ds:X509SubjectName>
          <ds:X509Certificate>CERTDATAHERE
</ds:X509Certificate>
        </ds:X509Data>
      </ds:KeyInfo>
    </md:KeyDescriptor>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SLO/Artifact"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SLO/POST"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SLO/Redirect"/>
    <md:SingleLogoutService Binding="urn:oasis:names:tc:SAML:2.0:bindings:SOAP" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SLO/SOAP"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/NIM/Artifact"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/NIM/POST"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/NIM/Redirect"/>
    <md:ManageNameIDService Binding="urn:oasis:names:tc:SAML:2.0:bindings:SOAP" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/NIM/SOAP"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:1.0:profiles:artifact-01" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SAML/Artifact" index="6"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:1.0:profiles:browser-post" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SAML/POST" index="5"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Artifact" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SAML2/Artifact" index="3"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:PAOS" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SAML2/ECP" index="4"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SAML2/POST" index="1"/>
    <md:AssertionConsumerService Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST-SimpleSign" Location="https://casshib-demo.ucmerced.edu:9494/casshib/shib/app2/Shibboleth.sso/SAML2/POST-SimpleSign" index="2"/>
  </md:SPSSODescriptor>

</md:EntityDescriptor>

</md:EntitiesDescriptor>
```