 http://www.velocityreviews.com/forums/t275440-dump-complete-java-vm-state-as-core-dump-not-via-os-possible.html
 
 
 gdb --pid 21849
Attaching to process 21849
(gdb) generate-core-file java.core
Saved corefile java.core
(gdb) quit

> jsadebugd /home/xxx/external_data/java/jdk1.5.0_06/bin/java java.core DebugServer
Attaching to core java.core from executable
/home/xxx/external_data/java/jdk1.5.0_06/bin/java and starting RMI
services, please wait...