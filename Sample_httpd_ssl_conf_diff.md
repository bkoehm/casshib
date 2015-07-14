
```
*** ./httpd/conf/original/extra/httpd-ssl.conf	Tue Apr  7 10:16:08 2009
--- ./httpd/conf/extra/httpd-ssl.conf	Tue Apr  7 12:48:59 2009
***************
*** 34,40 ****
  # Note: Configurations that use IPv6 but not IPv4-mapped addresses need two
  #       Listen directives: "Listen [::]:443" and "Listen 0.0.0.0:443"
  #
! Listen 443
  
  ##
  ##  SSL Global Context
--- 34,40 ----
  # Note: Configurations that use IPv6 but not IPv4-mapped addresses need two
  #       Listen directives: "Listen [::]:443" and "Listen 0.0.0.0:443"
  #
! Listen 9494
  
  ##
  ##  SSL Global Context
***************
*** 71,81 ****
  ## SSL Virtual Host Context
  ##
  
! <VirtualHost _default_:443>
  
  #   General setup for the virtual host
  DocumentRoot "/home/bkoehmstedt/casshib-demo/httpd/htdocs"
! ServerName www.example.com:443
  ServerAdmin you@example.com
  ErrorLog "/home/bkoehmstedt/casshib-demo/httpd/logs/error_log"
  TransferLog "/home/bkoehmstedt/casshib-demo/httpd/logs/access_log"
--- 71,81 ----
  ## SSL Virtual Host Context
  ##
  
! <VirtualHost _default_:9494>
  
  #   General setup for the virtual host
  DocumentRoot "/home/bkoehmstedt/casshib-demo/httpd/htdocs"
! ServerName casshib-demo.ucmerced.edu:9494
  ServerAdmin you@example.com
  ErrorLog "/home/bkoehmstedt/casshib-demo/httpd/logs/error_log"
  TransferLog "/home/bkoehmstedt/casshib-demo/httpd/logs/access_log"
***************
*** 96,103 ****
  #   in mind that if you have both an RSA and a DSA certificate you
  #   can configure both in parallel (to also allow the use of DSA
  #   ciphers, etc.)
! SSLCertificateFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server.crt"
  #SSLCertificateFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server-dsa.crt"
  
  #   Server Private Key:
  #   If the key is not combined with the certificate, use this
--- 96,104 ----
  #   in mind that if you have both an RSA and a DSA certificate you
  #   can configure both in parallel (to also allow the use of DSA
  #   ciphers, etc.)
! #SSLCertificateFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server.crt"
  #SSLCertificateFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server-dsa.crt"
+ SSLCertificateFile "/home/bkoehmstedt/casshib-demo/httpd/conf/certs/server.crt"
  
  #   Server Private Key:
  #   If the key is not combined with the certificate, use this
***************
*** 104,111 ****
  #   directive to point at the key file.  Keep in mind that if
  #   you've both a RSA and a DSA private key you can configure
  #   both in parallel (to also allow the use of DSA ciphers, etc.)
! SSLCertificateKeyFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server.key"
  #SSLCertificateKeyFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server-dsa.key"
  
  #   Server Certificate Chain:
  #   Point SSLCertificateChainFile at a file containing the
--- 105,113 ----
  #   directive to point at the key file.  Keep in mind that if
  #   you've both a RSA and a DSA private key you can configure
  #   both in parallel (to also allow the use of DSA ciphers, etc.)
! #SSLCertificateKeyFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server.key"
  #SSLCertificateKeyFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server-dsa.key"
+ SSLCertificateKeyFile "/home/bkoehmstedt/casshib-demo/httpd/conf/certs/server.key"
  
  #   Server Certificate Chain:
  #   Point SSLCertificateChainFile at a file containing the
***************
*** 115,120 ****
--- 117,123 ----
  #   when the CA certificates are directly appended to the server
  #   certificate for convinience.
  #SSLCertificateChainFile "/home/bkoehmstedt/casshib-demo/httpd/conf/server-ca.crt"
+ SSLCertificateChainFile "/home/bkoehmstedt/casshib-demo/httpd/conf/certs/server-ca.crt"
  
  #   Certificate Authority (CA):
  #   Set the CA certificate verification path where to find CA
***************
*** 228,231 ****
--- 231,264 ----
  CustomLog "/home/bkoehmstedt/casshib-demo/httpd/logs/ssl_request_log" \
            "%t %h %{SSL_PROTOCOL}x %{SSL_CIPHER}x \"%r\" %b"
  
+ # The Shibboleth wiki says this should be present for Shibboleth
+ UseCanonicalName On
+ 
+ # JK Connector
+ # make sure httpd-jk-init.conf is loaded before this conf file
+ Include conf/extra/httpd-jk-mount.conf
+ 
+ # Protect CASShib login URLs with the Shibboleth module so that Shibboleth
+ # embeds the authentication/attribute information in the HTTP headers for
+ # CASShib.  Also the Shibboleth.sso URLs need to be protected
+ # (Shibboleth.sso requests are handled by the Shibboleth module).
+ <Location /casshib/shib/*/login>
+   AuthType shibboleth
+   ShibRequireSession On
+   ShibUseHeaders On
+   require valid-user
+ </Location>
+ <Location /casshib/shib/*/Shibboleth.sso>
+   AuthType shibboleth
+   ShibRequireSession On
+   ShibUseHeaders On
+   require valid-user
+ </Location>
+ <Location /casshib/shib/*/Shibboleth.sso/*>
+   AuthType shibboleth
+   ShibRequireSession On
+   ShibUseHeaders On
+   require valid-user
+ </Location>
+ 
  </VirtualHost>
```