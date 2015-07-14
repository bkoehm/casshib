
```
<IfModule jk_module>

  # If you want just CASShib requests to be forwarded to application server:
  # This is recommended for production but since we're also deploying our
  # demo apps ('app1' and 'app2') on the same application server, we will
  # mount everything.
  #JkMount /casshib/* worker1

  # If you want all requests forwarded to application server:
  JkMount /* worker1

  # If you mount everything, it's probably a good idea to unmount Tomcat
  # admin apps and the other default deployed contexts
  # These are for Tomcat 6:
  JkUnMount /docs/* worker1
  JkUnMount /examples/* worker1
  JkUnMount /host-manager/* worker1
  # These are for Tomcat 5.5:
  JkUnMount /balancer/* worker1
  JkUnMount /jsp-examples/* worker1
  JkUnMount /servlets-examples/* worker1
  JkUnMount /tomcat-docs/* worker1
  JkUnMount /webdav/* worker1
  # These are for both:
  JkUnMount /manager/* worker1
  # Also unmount the Tomcat root page
  JkUnMount / worker1
  JkUnMount /index.html worker1

  # Don't forward Shibboleth.sso requests which the Apache Shibboleth module
  # handles
  JkUnMount /casshib/shib/*/Shibboleth.sso worker1
  JkUnMount /casshib/shib/*/Shibboleth.sso/* worker1

  # module also handles /shibboleth-sp/*
  JkUnMount /shibboleth-sp worker1
  JkUnMount /shibboleth-sp/* worker1

</IfModule>
```