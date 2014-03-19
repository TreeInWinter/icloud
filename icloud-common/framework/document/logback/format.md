http://www.cnblogs.com/luowei010101/archive/2012/01/04/2312438.html

记录输出到多个 appender 很简单，先定义各种 appender，然后在 logger 里进行引用，就行了。如下面的配置文件所示：
<configuration>
	<appender name="FILE" class="ch.qos.logback.core.FileAppender">
		<file>myApp.log</file>
		<!-- encoders are assigned by default the type ch.qos.logback.classic.encoder.PatternLayoutEncoder -->
		<encoder>
			<pattern>%date %level [%thread] %logger{10} [%file:%line] %msg%n</pattern>
		</encoder>
	</appender>
	<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%msg%n</pattern>
		</encoder>
	</appender>
	<root level="debug">
		<appender-ref ref="FILE" />
		<appender-ref ref="STDOUT" />
	</root>
</configuration>
该配置文件定义了两个 appender，分别是"FILE"和"STDOUT"。 "FILE" 这个 appender 把记录输 出到文件 "myapp.log " ，它的 encoder 是PatternLayoutEncoder，输出了日期、级别、线程名、logger 名、文件名及记录请求的行号、消息和行分隔符。 "STDOUT"这个 appender 把记录输出到控制台，它的 encoder 只是输出消息和行分隔符。 myApp.log文件内容如下：
2011-12-25 16:56:48,593 INFO [main] c.t.c.c.MyApp3 [MyApp3.java:48] Entering application.
2011-12-25 16:56:48,593 DEBUG [main] c.t.c.c.Foo [Foo.java:24] Did it again!
2011-12-25 16:56:48,593 INFO [main] c.t.c.c.MyApp3 [MyApp3.java:52] Exiting application.

	注意每个 appender 都有自己的 encoder。Encoder 通常不能被多个 appender 共享，layout也是。所以，logback 的配置文件里没有共享 encoder 或 layout 的语法。
Appender累积
默认情况下，appender 是可累积的：logger 会把记录输出到它自身的 appender 和它所有祖先的 appender。因此，把同一 appender 关联到多个 logger 会导致重复输出，如下面的配置文件会导致重复的输出：
<configuration>
	<appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
		<encoder>
			<pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
		</encoder>
	</appender>
	<logger name="chapters.configuration">
		<appender-ref ref="STDOUT" />
	</logger>
	<root level="debug">
		<appender-ref ref="STDOUT" /> <!—这会导致重复输出-->
	</root>
</configuration>
输出结果如下：
20:53:29.328 [main] INFO c.t.chapters.configuration.MyApp2 - Entering application.
20:53:29.328 [main] INFO c.t.chapters.configuration.MyApp2 - Entering application.
20:53:29.328 [main] DEBUG com.ttpod.chapters.configuration.Foo - Did it again!
20:53:29.328 [main] DEBUG com.ttpod.chapters.configuration.Foo - Did it again!
20:53:29.328 [main] INFO c.t.chapters.configuration.MyApp2 - Exiting application.
20:53:29.328 [main] INFO c.t.chapters.configuration.MyApp2 - Exiting application.

覆盖默认的累积行为
如果你觉得默认的累积行为不合适，可以设置叠加性标识为 false 以关闭它。 这样的话，logger 树里的某个分支可以输出到与其他 logger 不同的 appender。
示例：叠加性标识
<configuration>
	…………
	<logger name="com.ttpod.chapters.configuration.Foo" additivity="false">
		<appender-ref ref="FILE" />
	</logger>
	<root level="debug">
		<appender-ref ref="STDOUT" />
	</root>
</configuration>
输出结果：
Entering application.
Exiting application.
此例中，logger"chapters.configuration.Foo"关联 appender"FILE"，它的叠加性标记为false，这样它的记录输出仅会被发送到 appender"FILE"，不会被发送到更高 logger 等级关联的 appender。其他 logger 不受此影响。 用 additivityFlag.xml 配置 MyApp3 ， 运 行 后 ， 控 制 台 上 由 输 出 由"chapters.configuration.MyApp3"产生的记录。而 logger" chapters.configuration.Foo"将且仅仅将输出到文件 foo.log。
Layout格式化输出日志
