
```
*** ./httpd/conf/original/httpd.conf	Tue Apr  7 10:16:07 2009
--- ./httpd/conf/httpd.conf	Tue Apr  7 12:48:35 2009
***************
*** 37,43 ****
  # prevent Apache from glomming onto all bound IP addresses.
  #
  #Listen 12.34.56.78:80
! Listen 80
  
  #
  # Dynamic Shared Object (DSO) Support
--- 37,46 ----
  # prevent Apache from glomming onto all bound IP addresses.
  #
  #Listen 12.34.56.78:80
! #Listen 80
! # Non-SSL connections on the loopback interface only.  The outside world
! # will use SSL.
! Listen 127.0.0.1:9393
  
  #
  # Dynamic Shared Object (DSO) Support
***************
*** 151,157 ****
  #
  # If your host doesn't have a registered DNS name, enter its IP address here.
  #
! #ServerName www.example.com:80
  
  #
  # DocumentRoot: The directory out of which you will serve your
--- 154,160 ----
  #
  # If your host doesn't have a registered DNS name, enter its IP address here.
  #
! ServerName casshib-demo.ucmerced.edu:8383
  
  #
  # DocumentRoot: The directory out of which you will serve your
***************
*** 453,460 ****
  # Various default settings
  #Include conf/extra/httpd-default.conf
  
  # Secure (SSL/TLS) connections
! #Include conf/extra/httpd-ssl.conf
  #
  # Note: The following must must be present to support
  #       starting without SSL on platforms with no /dev/random equivalent
--- 456,468 ----
  # Various default settings
  #Include conf/extra/httpd-default.conf
  
+ # JK Connector
+ # httpd-jk-init.conf should be loaded before httpd-ssl.conf
+ Include conf/extra/httpd-jk-init.conf
+ Include conf/extra/httpd-jk-mount.conf
+ 
  # Secure (SSL/TLS) connections
! Include conf/extra/httpd-ssl.conf
  #
  # Note: The following must must be present to support
  #       starting without SSL on platforms with no /dev/random equivalent
***************
*** 464,466 ****
--- 472,477 ----
  SSLRandomSeed startup builtin
  SSLRandomSeed connect builtin
  </IfModule>
+ 
+ # Load the Shibboleth module
+ Include /home/bkoehmstedt/casshib-demo/shibboleth-sp/etc/shibboleth/apache22.config
```