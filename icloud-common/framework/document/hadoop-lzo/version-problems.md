codelab/hadoop-lzo 中生成的 libgplcompression.so 与 
trunk/third_party/hadoop-cloudera/lib/hadoop-gpl-compression-0.2.0-dev.jar
不兼容,

要用 codelab/hadoop-gpl-compression 里   ant compile出来的  libgplcompression.so,

而本地编译出来的 liblzo2.so.2,

放到服务器上的 /var/yr/webserver/lib/native后， 要在 tomcat 的 catalina.sh中加上

JAVA_OPTS="-Djava.library.path=/var/yr/webserver/lib/native"
export LD_LIBRARY_PATH="/var/yr/webserver/lib/native"

使得JNI和动态链接都能成功。


