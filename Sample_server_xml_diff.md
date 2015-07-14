
```
*** ./apache-tomcat-6.0.18/conf/server.xml.ORIG	Mon Jul 21 17:01:12 2008
--- ./apache-tomcat-6.0.18/conf/server.xml	Tue Apr  7 12:24:10 2009
***************
*** 19,25 ****
       define subcomponents such as "Valves" at this level.
       Documentation at /docs/config/server.html
   -->
! <Server port="8005" shutdown="SHUTDOWN">
  
    <!--APR library loader. Documentation at /docs/apr.html -->
    <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
--- 19,25 ----
       define subcomponents such as "Valves" at this level.
       Documentation at /docs/config/server.html
   -->
! <Server port="8581" shutdown="SHUTDOWN">
  
    <!--APR library loader. Documentation at /docs/apr.html -->
    <Listener className="org.apache.catalina.core.AprLifecycleListener" SSLEngine="on" />
***************
*** 64,72 ****
           APR (HTTP/AJP) Connector: /docs/apr.html
           Define a non-SSL HTTP/1.1 Connector on port 8080
      -->
!     <Connector port="8080" protocol="HTTP/1.1" 
                 connectionTimeout="20000" 
!                redirectPort="8443" />
      <!-- A "Connector" using the shared thread pool-->
      <!--
      <Connector executor="tomcatThreadPool"
--- 64,78 ----
           APR (HTTP/AJP) Connector: /docs/apr.html
           Define a non-SSL HTTP/1.1 Connector on port 8080
      -->
!     <!-- IMPORTANT: Make sure address="127.0.0.1" is present because we
!          don't want external people connecting directly to the application
!          server and spoofing headers. You may also comment this Connector
!          out entirely if you wish.  It is here really only for local
!          debugging purposes.  Apache will be connecting via the AJP
!          connector. -->
!     <Connector port="8585" protocol="HTTP/1.1" 
                 connectionTimeout="20000" 
!                address="127.0.0.1" />
      <!-- A "Connector" using the shared thread pool-->
      <!--
      <Connector executor="tomcatThreadPool"
***************
*** 85,93 ****
      -->
  
      <!-- Define an AJP 1.3 Connector on port 8009 -->
!     <Connector port="8009" protocol="AJP/1.3" redirectPort="8443" />
  
- 
      <!-- An Engine represents the entry point (within Catalina) that processes
           every request.  The Engine implementation for Tomcat stand alone
           analyzes the HTTP headers included with the request, and passes them
--- 91,104 ----
      -->
  
      <!-- Define an AJP 1.3 Connector on port 8009 -->
!     <!-- IMPORTANT: Make sure address="127.0.0.1" is present because we
!          don't want external people connecting directly to the application
!          server and spoofing headers. tomcatAuthentication="false" must also
!          be present here for CASShib. -->
!     <Connector port="8020" protocol="AJP/1.3"
!                address="127.0.0.1"
!                tomcatAuthentication="false" />
  
      <!-- An Engine represents the entry point (within Catalina) that processes
           every request.  The Engine implementation for Tomcat stand alone
           analyzes the HTTP headers included with the request, and passes them
```