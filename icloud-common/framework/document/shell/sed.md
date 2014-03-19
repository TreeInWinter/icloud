一般在sed 中替换都用单引号，如下边
sed -in-place ‘s/8080/8001/g’ /home/work/server.xml
但是如果需要把8001改成变量，如
sed -in-place ’s/8080/$port/g‘ /home/work/server.xml
这样就不成功。
此时需要把单引号改成双引号,如下边例子
$port=8001
sed -in-place "s/8080/$port/g" /home/work/server.xml