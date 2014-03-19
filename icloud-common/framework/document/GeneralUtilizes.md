https://github.com/springside/springside4/wiki/GeneralUtilizes 
GeneralUtilizes
Page History
1. Overview

Apache Commons， Google Guava，Spring， 各家工具类各有所长，没有一种可以一统天下。尽量从三家中选择一个最好的，如果没有，就参考三家的代码自己编写。
1.1 String

Apache Commons的StringUtils有三家最全面的NullSafe的String工具函数，另外Guava提供少量更 高级的函数。
1.2 Collection

Guava 的提供了collection初始化的简写法和一些扩展类型. Apache Commons Collections有点老了，尽量不再使用。SpringSide Core中封装了一个Collections3.
1.3 Reflection

三 家的Reflections类都不够粗暴，不能完全无视private/protected修饰符强制访问。因此 SpringSide Core自行编写了一个粗暴型的，聚合各家函数的Relections。
1.4 Assert防御式 编程

三 家都有提供校验参数的防御式编程API，选用Apache Commons Lang的Validate。
 一开始用的是Spring的Assert，但Assert很容易和junit的弄乱，而且校验函数没有返回输入 参数的能力， 而且出错信息也没有格式化字符串+自定义参数的能力， 而Guava的类名叫Preconditions太长太怪，所以最后选了Apache的。
1.5 IO

选 用Apache Commons IO 因为它提供了Stream/Reader/Writer/String间操作的函数 和 文件目录操作的函数。 不用Guava是因为它那完全脱离Java IO之外的抽象有不少的学习成本，而且功能并不完整。

另外 Spring提供了一个Resource的抽象，可以方便加载文件路径，Classpath路径和Web App内路径的内容。
1.6 Concurrency

JDK 自带，Google补充，还有SpringSide自己也写了个Threads。
1.7 Exceptions

Springside- core自己感觉常用需求整理了Exceptions。
1.8 Encode

Apache Commons Encode提供正宗的Base64/Hex编码， Apache Commons Lang提供XML/HTML的编码，JDK提供URL的编码，在SpringSide Core中提供一个Encodes封装，以一个统一入口调用上述各家的能力。
1.9 Time

Joda- Time，无异意。
2. Apache Commons
2.1 Lang3 StringUtils

选 型见1.1， 示例见showcase中的StringDemo
2.2 Lang3 Validate

选 型见1.4， 示例见showcase中的ValiateDemo
2.3 IOUtils

选 型见1.5， 示例见showcase中的IOUtilsDemo, 含File与stream,string,byte[]的各种操作， String,Stream,Reader/Writer之间的个总转换.
3. Google Guava
3.1 Collections

Guava 提供一个比JDK7更舒服的Collection初始化函数，见showcase中的CollectionsDemo。 另外Guava提供了BiMap， MultiValueMap与Table三种高级的Map，见showcase中的AdvancedMapDemo。
3.2 String相关

选 型见1.1， 示例见showcase中的StringDemo，提供高级的Splitter和Joiner，以及在表名，类名，变量名见转换的函数(eg. ORDER_ITME, OrderItem, orderItem.)
4. Spring
4.1 Resource

见 选型1.5
4. SpringSide
4.1 Reflections

提 供反射调用函数和getter/setter函数以及直接绕过getter/setter直接访问属性的能力。
 无视private/protected/public修饰符的关键是 Class.getDeclaredMethods()函数，它与Class.getMethods()比，能返回 private的函数，但只能返回当前类的(不会继承于父类的函数一起返回)，然后可以 method.setAccessible(true)来强制转换，当然，这并不总是成功，因为 SecurityManager有时候会干预。

提供反 射获取Class声明中的泛型参数，例子是public UserDao extends HibernateDao，获得User.class.注意这个User是定义在父类旁边的，这也是Java泛型中唯一能反射获得Class的地方。
4.2 Collections3

因 为JDK有Collections, Guava有Collections2，所以只好叫Collections3。
 主要提供两类函数:
 一是从Collection中通过反射，取得元素的某个属性值，组成新的Map,List或者String。或 者将 Collection转为String，每个元素执行toString()，元素间用分割符或者用 prefix,postfix.
 一是Collection的加减运算，及isEmpty(),getFirst,getLast()这样的常用 函数, 目标是不用再引入Apache Commons Collection。见Showcase中的CollectionsDemo
4.3 Exceptions

封 装了三个实用函数： 1. unchecked(), 将CheckedException转化为RuntimeException的方法 1. getStackTraceAsString(), 将整个Exception Stack所有消息转化为一个String 1. isCausedBy(), 判断异常是否由某些底层异常引起
4.4 Threads

只 封装了两类函数： 一个是sleep函数，屏蔽了InterruptedException的异常. 一是仿照JDK的shutdown/shutdownNow中的注释，提供了一个有超时控制，而且先尝试shutdown，超时了再尝试 shutdownNow的gracefulShutdown()函数.
4.5 Encodes

选 型见上， 见SpringSide Core中Encodes的测试用例。
4.6 Identities

各 种生成Id的函数，比如uuid()，randomLog()，randomBase62()
4.7 PropertiesLoader

Properties 文件载入工具类. 仿造Spring编写，可载入多个properties文件, 相同的属性在最后载入的文件中的值将会覆盖之前的值，但以System的Property优先. Path可以是classpath，绝对路径file或者web-inf/
5. Dozer

异 构对象间的复制拷贝，详见Dozer章 节。

返 回参考手册

Last edited by springside, 12 days ago