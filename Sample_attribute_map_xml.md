
```
<Attributes xmlns="urn:mace:shibboleth:2.0:attribute-map" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <!-- First some useful eduPerson attributes that many sites might use. -->
    
    <Attribute name="urn:mace:dir:attribute-def:eduPersonPrincipalName" id="shibattr-eppn">
        <AttributeDecoder xsi:type="ScopedAttributeDecoder"/>
    </Attribute>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.6" id="shibattr-eppn">
        <AttributeDecoder xsi:type="ScopedAttributeDecoder"/>
    </Attribute>
    
    <Attribute name="urn:mace:dir:attribute-def:eduPersonScopedAffiliation" id="shibattr-affiliation">
        <AttributeDecoder xsi:type="ScopedAttributeDecoder" caseSensitive="false"/>
    </Attribute>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.9" id="shibattr-affiliation">
        <AttributeDecoder xsi:type="ScopedAttributeDecoder" caseSensitive="false"/>
    </Attribute>
    
    <Attribute name="urn:mace:dir:attribute-def:eduPersonAffiliation" id="shibattr-unscoped-affiliation">
        <AttributeDecoder xsi:type="StringAttributeDecoder" caseSensitive="false"/>
    </Attribute>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.1" id="shibattr-unscoped-affiliation">
        <AttributeDecoder xsi:type="StringAttributeDecoder" caseSensitive="false"/>
    </Attribute>
    
    <Attribute name="urn:mace:dir:attribute-def:eduPersonEntitlement" id="shibattr-entitlement"/>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.7" id="shibattr-entitlement"/>
    
    <!-- A persistent id attribute that supports personalized anonymous access. -->
    
    <!-- First, the deprecated version, decoded as a scoped string: -->
    <Attribute name="urn:mace:dir:attribute-def:eduPersonTargetedID" id="shibattr-targeted-id">
        <AttributeDecoder xsi:type="ScopedAttributeDecoder"/>
        <!-- <AttributeDecoder xsi:type="NameIDFromScopedAttributeDecoder" formatter="$NameQualifier!$SPNameQualifier!$Name"/> -->
    </Attribute>
    
    <!-- Second, an alternate decoder that will turn the deprecated form into the newer form. -->
    <!--
    <Attribute name="urn:mace:dir:attribute-def:eduPersonTargetedID" id="shibattr-persistent-id">
        <AttributeDecoder xsi:type="NameIDFromScopedAttributeDecoder" formatter="$NameQualifier!$SPNameQualifier!$Name"/>
    </Attribute>
    -->
    
    <!-- Third, the new version (note the OID-style name): -->
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.10" id="shibattr-persistent-id">
        <AttributeDecoder xsi:type="NameIDAttributeDecoder" formatter="$NameQualifier!$SPNameQualifier!$Name"/>
    </Attribute>

    <!-- Fourth, the SAML 2.0 NameID Format: -->
    <Attribute name="urn:oasis:names:tc:SAML:2.0:nameid-format:persistent" id="shibattr-persistent-id">
        <AttributeDecoder xsi:type="NameIDAttributeDecoder" formatter="$NameQualifier!$SPNameQualifier!$Name"/>
    </Attribute>
    
    <!-- Some more eduPerson attributes, uncomment these to use them... -->
    <Attribute name="urn:mace:dir:attribute-def:eduPersonPrimaryAffiliation" id="shibattr-primary-affiliation">
        <AttributeDecoder xsi:type="StringAttributeDecoder" caseSensitive="false"/>
    </Attribute>
    <!--
    <Attribute name="urn:mace:dir:attribute-def:eduPersonNickname" id="shibattr-nickname"/>
    <Attribute name="urn:mace:dir:attribute-def:eduPersonPrimaryOrgUnitDN" id="shibattr-primary-orgunit-dn"/>
    <Attribute name="urn:mace:dir:attribute-def:eduPersonOrgUnitDN" id="shibattr-orgunit-dn"/>
    <Attribute name="urn:mace:dir:attribute-def:eduPersonOrgDN" id="shibattr-org-dn"/>

    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.5" id="shibattr-primary-affiliation">
        <AttributeDecoder xsi:type="StringAttributeDecoder" caseSensitive="false"/>
    </Attribute>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.2" id="shibattr-nickname"/>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.8" id="shibattr-primary-orgunit-dn"/>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.4" id="shibattr-orgunit-dn"/>
    <Attribute name="urn:oid:1.3.6.1.4.1.5923.1.1.1.3" id="shibattr-org-dn"/>
    -->

    <!--Examples of LDAP-based attributes, uncomment to use these... -->
    <!--
    <Attribute name="urn:mace:dir:attribute-def:cn" id="shibattr-cn"/>
    <Attribute name="urn:mace:dir:attribute-def:sn" id="shibattr-sn"/>
    <Attribute name="urn:mace:dir:attribute-def:givenName" id="shibattr-givenName"/>
    <Attribute name="urn:mace:dir:attribute-def:mail" id="shibattr-mail"/>
    <Attribute name="urn:mace:dir:attribute-def:telephoneNumber" id="shibattr-telephoneNumber"/>
    <Attribute name="urn:mace:dir:attribute-def:title" id="shibattr-title"/>
    <Attribute name="urn:mace:dir:attribute-def:initials" id="shibattr-initials"/>
    <Attribute name="urn:mace:dir:attribute-def:description" id="shibattr-description"/>
    <Attribute name="urn:mace:dir:attribute-def:carLicense" id="shibattr-carLicense"/>
    <Attribute name="urn:mace:dir:attribute-def:departmentNumber" id="shibattr-departmentNumber"/>
    <Attribute name="urn:mace:dir:attribute-def:displayName" id="shibattr-displayName"/>
    <Attribute name="urn:mace:dir:attribute-def:employeeNumber" id="shibattr-employeeNumber"/>
    <Attribute name="urn:mace:dir:attribute-def:employeeType" id="shibattr-employeeType"/>
    <Attribute name="urn:mace:dir:attribute-def:preferredLanguage" id="shibattr-preferredLanguage"/>
    <Attribute name="urn:mace:dir:attribute-def:manager" id="shibattr-manager"/>
    <Attribute name="urn:mace:dir:attribute-def:seeAlso" id="shibattr-seeAlso"/>
    <Attribute name="urn:mace:dir:attribute-def:facsimileTelephoneNumber" id="shibattr-facsimileTelephoneNumber"/>
    <Attribute name="urn:mace:dir:attribute-def:street" id="shibattr-street"/>
    <Attribute name="urn:mace:dir:attribute-def:postOfficeBox" id="shibattr-postOfficeBox"/>
    <Attribute name="urn:mace:dir:attribute-def:postalCode" id="shibattr-postalCode"/>
    <Attribute name="urn:mace:dir:attribute-def:st" id="shibattr-st"/>
    <Attribute name="urn:mace:dir:attribute-def:l" id="shibattr-l"/>
    <Attribute name="urn:mace:dir:attribute-def:o" id="shibattr-o"/>
    <Attribute name="urn:mace:dir:attribute-def:ou" id="shibattr-ou"/>
    <Attribute name="urn:mace:dir:attribute-def:businessCategory" id="shibattr-businessCategory"/>
    <Attribute name="urn:mace:dir:attribute-def:physicalDeliveryOfficeName" id="shibattr-physicalDeliveryOfficeName"/>

    <Attribute name="urn:oid:2.5.4.3" id="shibattr-cn"/>
    <Attribute name="urn:oid:2.5.4.4" id="shibattr-sn"/>
    <Attribute name="urn:oid:2.5.4.42" id="shibattr-givenName"/>
    <Attribute name="urn:oid:0.9.2342.19200300.100.1.3" id="shibattr-mail"/>
    <Attribute name="urn:oid:2.5.4.20" id="shibattr-telephoneNumber"/>
    <Attribute name="urn:oid:2.5.4.12" id="shibattr-title"/>
    <Attribute name="urn:oid:2.5.4.43" id="shibattr-initials"/>
    <Attribute name="urn:oid:2.5.4.13" id="shibattr-description"/>
    <Attribute name="urn:oid:2.16.840.1.113730.3.1.1" id="shibattr-carLicense"/>
    <Attribute name="urn:oid:2.16.840.1.113730.3.1.2" id="shibattr-departmentNumber"/>
    <Attribute name="urn:oid:2.16.840.1.113730.3.1.241" id="shibattr-displayName"/>
    <Attribute name="urn:oid:1.2.840.113556.1.2.610" id="shibattr-employeeNumber"/>
    <Attribute name="urn:oid:1.2.840.113556.1.2.613" id="shibattr-employeeType"/>
    <Attribute name="urn:oid:2.16.840.1.113730.3.1.39" id="shibattr-preferredLanguage"/>
    <Attribute name="urn:oid:0.9.2342.19200300.100.1.10" id="shibattr-manager"/>
    <Attribute name="urn:oid:2.5.4.34" id="shibattr-seeAlso"/>
    <Attribute name="urn:oid:2.5.4.23" id="shibattr-facsimileTelephoneNumber"/>
    <Attribute name="urn:oid:2.5.4.9" id="shibattr-street"/>
    <Attribute name="urn:oid:2.5.4.18" id="shibattr-postOfficeBox"/>
    <Attribute name="urn:oid:2.5.4.17" id="shibattr-postalCode"/>
    <Attribute name="urn:oid:2.5.4.8" id="shibattr-st"/>
    <Attribute name="urn:oid:2.5.4.7" id="shibattr-l"/>
    <Attribute name="urn:oid:2.5.4.10" id="shibattr-o"/>
    <Attribute name="urn:oid:2.5.4.11" id="shibattr-ou"/>
    <Attribute name="urn:oid:2.5.4.15" id="shibattr-businessCategory"/>
    <Attribute name="urn:oid:2.5.4.19" id="shibattr-physicalDeliveryOfficeName"/>
    -->

    <Attribute name="urn:mace:dir:attribute-def:uid" id="shibattr-uid"/>

</Attributes>
```