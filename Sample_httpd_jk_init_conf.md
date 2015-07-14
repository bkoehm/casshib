
```
# note that this file needs to be loaded before httpd-ssl.conf

LoadModule    jk_module  modules/mod_jk.so

<IfModule jk_module>
  JkWorkersFile conf/extra/workers.properties
  JkShmFile     logs/mod_jk.shm
  JkLogFile     logs/mod_jk.log
  JkLogLevel    info
  JkLogStampFormat "[%a %b %d %H:%M:%S %Y] "
</IfModule>
```