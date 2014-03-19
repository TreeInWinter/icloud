ssh登录 10.21.115.7

cd /var/yr/zookeeper

bin/zkCli.sh -server zk1:2181

可以直接修改zookeeper的值


 String evnName = "YRFS_SERVER";
        String connectString = System.getenv().get(evnName);


         负载均衡、 主备、以及各类配置文件， 都可以放在 YRNS 里面，   然后在整个机房的任何一台机器上都可以方便的访问， 

         读写 zookeeper的java代码在 framework项目里有很多地方用到，  特别是  szstandardserver的初始化，  会直接把自己注册到zookeeper  ， 还有 logclient 取后端logrouter的时候， 也是到 zookeeper上取ip和端口。

