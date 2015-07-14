
```
*** shibboleth-sp/etc/shibboleth/attribute-policy.xml.ORIG	Tue Apr  7 10:42:44 2009
--- shibboleth-sp/etc/shibboleth/attribute-policy.xml	Tue Apr  7 15:55:58 2009
***************
*** 32,55 ****
          <afp:PolicyRequirementRule xsi:type="ANY"/>
  
          <!-- Filter out undefined affiliations and ensure only one primary. -->
!         <afp:AttributeRule attributeID="affiliation">
              <afp:PermitValueRule xsi:type="AND">
                  <RuleReference ref="eduPersonAffiliationValues"/>
                  <RuleReference ref="ScopingRules"/>
              </afp:PermitValueRule>
          </afp:AttributeRule>
!         <afp:AttributeRule attributeID="unscoped-affiliation">
              <afp:PermitValueRuleReference ref="eduPersonAffiliationValues"/>
          </afp:AttributeRule>
!         <afp:AttributeRule attributeID="primary-affiliation">
              <afp:PermitValueRuleReference ref="eduPersonAffiliationValues"/>
          </afp:AttributeRule>
          
!         <afp:AttributeRule attributeID="eppn">
              <afp:PermitValueRuleReference ref="ScopingRules"/>
          </afp:AttributeRule>
  
!         <afp:AttributeRule attributeID="targeted-id">
              <afp:PermitValueRuleReference ref="ScopingRules"/>
          </afp:AttributeRule>
          
--- 32,55 ----
          <afp:PolicyRequirementRule xsi:type="ANY"/>
  
          <!-- Filter out undefined affiliations and ensure only one primary. -->
!         <afp:AttributeRule attributeID="shibattr-affiliation">
              <afp:PermitValueRule xsi:type="AND">
                  <RuleReference ref="eduPersonAffiliationValues"/>
                  <RuleReference ref="ScopingRules"/>
              </afp:PermitValueRule>
          </afp:AttributeRule>
!         <afp:AttributeRule attributeID="shibattr-unscoped-affiliation">
              <afp:PermitValueRuleReference ref="eduPersonAffiliationValues"/>
          </afp:AttributeRule>
!         <afp:AttributeRule attributeID="shibattr-primary-affiliation">
              <afp:PermitValueRuleReference ref="eduPersonAffiliationValues"/>
          </afp:AttributeRule>
          
!         <afp:AttributeRule attributeID="shibattr-eppn">
              <afp:PermitValueRuleReference ref="ScopingRules"/>
          </afp:AttributeRule>
  
!         <afp:AttributeRule attributeID="shibattr-targeted-id">
              <afp:PermitValueRuleReference ref="ScopingRules"/>
          </afp:AttributeRule>
```