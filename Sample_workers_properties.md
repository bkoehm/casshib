
```
# Define 1 real worker using ajp13
worker.list=worker1
# Set properties for worker1 (ajp13)
worker.worker1.type=ajp13
worker.worker1.host=localhost
# This is the AJP port number you configured in Tomcat's server.xml.
worker.worker1.port=8020
```