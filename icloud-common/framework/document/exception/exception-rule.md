异常处理规范是Java语言中复杂性和混乱程度可以与C++相比的一个方面。
 szframework作为一个全局的框架，需要定义一套适合自己的异常处理规范。

	目的：
	
*	使代码混乱最小化  
*	捕捉并保留诊断信息  
*	通知合适的人员  
*	比较得体地退出活动  


#概念定义 

###异常屏障:  程序中在比较高的层面上对异常进行捕捉和处理的代码， 比如

	
	
	try{
		callSomeServie();
	}catch(Exception e){
		logger.error(YRUtil.stringifyException(e));
		doSomeOtherOperation();
	}
	
callSomeServie中的所有异常都会被捕捉到，并做适当处理。



###从责任这个角度看异常
从责任这个角度看
Error属于JVM需要负担的责任;  可以(应该)被nagios监控到，并处理
RuntimeException是程序应该负担的责任;
checked exception 是具体应用负担的责任;




##1. szframework中的方法， 原则上不抛出 checkedException,除非作为返回值，让调用者根据异常类型来处理控制流。尽量选择已有异常类型：

**常用checkedException**
> AclNotFoundException, ActivationException, AlreadyBoundException, ApplicationException,
  AWTException,BackingStoreException, BadLocationException, CertificateException, ClassNotFoundException,
  CloneNotSupportedException,DataFormatException, DestroyFailedException, ExpandVetoException, FontFormatException, 
GeneralSecurityException,GSSException, IllegalAccessException, InstantiationException, InterruptedException, 
IntrospectionException,InvalidMidiDataException, InvalidPreferencesFormatException, InvocationTargetException,
  IOException,LastOwnerException, LineUnavailableException, MidiUnavailableException, MimeTypeParseException, 
NamingException,NoninvertibleTransformException, NoSuchFieldException, NoSuchMethodException, NotBoundException,
  NotOwnerException,ParseException, ParserConfigurationException, PrinterException, PrintException, 
PrivilegedActionException,PropertyVetoException, RefreshFailedException, RemarshalException, 
SAXException,ServerNotActiveException, SQLException, TooManyListenersException, TransformerException,
UnsupportedAudioFileException, UnsupportedCallbackException, UnsupportedFlavorException,UnsupportedLookAndFeelException, 
URISyntaxException, UserException, XAException

 **不要随便自己定义新异常.**
 
 

##2. szframework中的方法  应该在 方法文档中标出所有可能抛出的 RuntimeException， 以便调用者判断是否应该由自己 做错误恢复，或者交给 “异常屏障”。

**常用RuntimeException**
> ArithmeticException, ArrayStoreException, BufferOverflowException, BufferUnderflowException, CannotRedoException,
CannotUndoException, ClassCastException, CMMException, ConcurrentModificationException, DOMException,EmptyStackException, 
IllegalArgumentException, IllegalMonitorStateException, IllegalPathStateException,IllegalStateException, ImagingOpException, 
IndexOutOfBoundsException,
MissingResourceException,NegativeArraySizeException, NoSuchElementException, NullPointerException, ProfileDataException, 
ProviderException,RasterFormatException, SecurityException, SystemException, UndeclaredThrowableException, UnmodifiableSetException,
UnsupportedOperationException


##3. 对于szframework调用的外部方法， 除非明确知道对某些 CheckedException需要被特别对待， 否则 ，一律用


> catch(Exception e){
  logger.error(YRUtil.stringifyException(e);
throw Throwable
 s.propagate(t);

}
 记录日志并转化为RuntimeException 向外传播。





##4. szframework中的返回值使用 Optional类型

[安全地使用null(Using and avoiding null)](http://www.cnblogs.com/icejoywoo/archive/2012/06/20/2556930.html)

[安全地使用null(原文)](http://code.google.com/p/guava-libraries/wiki/UsingAndAvoidingNullExplained)

Absent 和 Present 是Option的两个子类,

**原则上禁止使用原生的null作为返回值**


比如

		public static CuratorFramework client(String connectString,
			String namespace, RetryPolicy retryPolicy) {
		try {
			Builder builder = CuratorFrameworkFactory.builder();
			builder.connectString(connectString); // required
			builder.retryPolicy(retryPolicy); // required
			builder.namespace(namespace); // optional
			return builder.build();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
	应该改成
	public static Optional<CuratorFramework> buildClient(String connectString,
			String namespace, RetryPolicy retryPolicy) {
		try {
			Builder builder = CuratorFrameworkFactory.builder();
			builder.connectString(connectString); // required
			builder.retryPolicy(retryPolicy); // required
			builder.namespace(namespace); // optional
			return Optional.of(builder.build());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return Optional.absent();
	}
	
###使用实例
 

	Optional使用下列三个静态方法来构造, Present和Absent都是不可直接实例化的, 只可以使用下列的方法来构建实例, 应该使用了工厂模式.
	Optional.of(T)	返回一个包含给定数值的Present, 如果是null值, 就直接失败.
	Optional.absent()	返回具有某种类型的Absent.INSTANCE
	Optional.fromNullable(T)	返回一个包含给定数值的Present, 如果是null值, 返回具有某种类型的Absent.INSTANCE 
	
	访问方法 
	boolean isPresent()	如果非null, 返回true, 如果是null, 返回false
	T get()	如果非null, 返回其中T的值; 否则, 抛出IllegalStateException.
	T or(T)	参数是默认值, 如果非null, 就返回Optional的值, 否则就返回默认值
	T orNull()	如果非null, 返回Optional的值, 否则, 返回null. 和fromNullable方法操作相反.
	Set<T> asSet()	如果非null, 返回一个包含Optional值的不可变Set, 否则返回空的Set.
	
	下面是一个例子, 展示其最基本的使用. 使用Optional可以避免使用null, 而又可以具有null不存在的含义.

	package optional;
 
	import com.google.common.base.Objects;
	import com.google.common.base.Optional;
 
	public class OptionalDemo {
    public static void main(String[] args) {
        Optional<Integer> possible = Optional.<Integer>of(5);
        if (possible.isPresent()) // true
            System.out.println(possible.get()); // 5
        System.out.println(possible.asSet()); // [5]
         
        Optional<Integer> absentValue = Optional.<Integer>absent();
        if (absentValue.isPresent()) // false
            System.out.println(absentValue.get()); 
        System.out.println(absentValue.or(-1)); // -1
        System.out.println(absentValue.orNull()); // null
        System.out.println(absentValue.asSet()); // []
         
        // absent is not null
        System.out.println(Objects.firstNonNull(possible, absentValue)); // Optional.of(5)
        System.out.println(Objects.firstNonNull(absentValue, possible)); // Optional.absent()
        System.out.println(Objects.firstNonNull(absentValue, absentValue)); // Optional.absent()
         
        Optional<Integer> nullValue = Optional.<Integer>fromNullable(null);
        if (nullValue.isPresent()) // false
            System.out.println(nullValue.get());
        System.out.println(nullValue.or(-1)); // -1
        System.out.println(nullValue.orNull()); // null
        System.out.println(nullValue.asSet()); // []
         
        System.out.println(Objects.firstNonNull(possible, nullValue)); // Optional.of(5)
        System.out.println(Objects.firstNonNull(nullValue, possible)); // Optional.absent()
        System.out.println(Objects.firstNonNull(nullValue, nullValue)); // Optional.absent()
         
        System.out.println(Objects.firstNonNull(null, 1)); // 1
        System.out.println(Objects.firstNonNull(1, null)); // 1
        System.out.println(Objects.firstNonNull(null, null)); // cause a java.lang.NullPointerException
    }
}	
	

#5.使用 guava中的Preconditions	来抛出  Runtime的 IllegalArgumentException, 直接退回到 异常屏障。



#6.确定异常屏蔽层
 对于一段复杂的业务逻辑代码， 首先确定故障屏蔽层， 各个业务分支要明确故障屏蔽层。

 以 jproxy的 JProxyServlet为例， 故障屏蔽层在 service 方法上,应该重载
 service 方法， 在其中的  get,post 外 catch throwable异常。

 再往里， 则及时catch checkedException 并 log.error, propagate 。

 这样， 基本遇到任何错， 都会直接回退到service 方法的“错误屏障”。

 唯一的可能例外是需要重连的情况， 这时应该处理好checkedException ， 做好重连工作，

#7. Guava 的Throwables实用方法


Throwables可以将检查异常转换成未检查异常): 

	1.package com.ociweb.jnb.apr2010;  
 	3.import com.google.common.base.Throwables;  
 	5.import java.io.InputStream;  
 	6.import java.net.URL;  
 	7.  
 	8.public class ExerciseThrowables {  
 	9.    public static void main(String[] args) {  
 	10.        try {  
 	11.            URL url = new URL("http://ociweb.com");  
 	12.            final InputStream in = url.openStream();  
 	13.            // read from the input stream  
 	14.            in.close();  
 	15.        } catch (Throwable t) {  
 	16.            throw Throwables.propagate(t);  
 	17.        }  
 	18.    }  
 	19.}  
  

 

http://stackoverflow.com/questions/5090417/how-do-i-use-throwables-propagateifinstanceof-from-google-guava


in other words, the code in question

	public void doSomething() 
    throws IOException, SQLException {

    try {
        someMethodThatCouldThrowAnything();
    } catch (IKnowWhatToDoWithThisException e) {
        handle(e);
    } catch (Throwable t) {
        Throwables.propagateIfInstanceOf(t, IOException.class);
        Throwables.propagateIfInstanceOf(t, SQLException.class);
        throw Throwables.propagate(t);
    }  
}

is a shorthand for the following code:
	public void doSomething() 
    throws IOException, SQLException {

    try {
        someMethodThatCouldThrowAnything();
    } catch (IKnowWhatToDoWithThisException e) {
        handle(e);
    } catch (SQLException ex) {
        throw ex;
    } catch (IOException ex) {
        throw ex;
    } catch (Throwable t) {
        throw new RuntimeException(t);
    }  
}

##8.选择checked exceptions还是unchecked exceptions的指南



1.	 当要决定是采用checked exception还是Unchecked exception的时候，你要问自己一个问题，"如果这种异常一旦抛出，客户端会做怎样的补救？"
	如果客户端可以通过其他的方法恢复异常，那么这种异常就是checked exception；如果客户端对出现的这种异常无能为力，那么这种异常就是Unchecked exception；从使用上讲，当异常出现的时候要做一些试图恢复它的动作而不要仅仅的打印它的信息，总来的来说，看下表：

	Client's reaction when exception happens   
	Exception type

	Client code cannot do anything   
	Make it an unchecked exception

	Client code will take some useful recovery action based on information in exception   
	Make it a checked exception

	此外，尽量使用unchecked exception来处理编程错误：因为unchecked exception不用使客户端代码显示的处理它们，它们自己会在出现的地方挂起程序并打印出异常信息。
	Java API中提供了丰富的unchecked excetpion，譬如：NullPointerException , IllegalArgumentException 和 IllegalStateException等，
	因此我一般使用这些标准的异常类而不愿亲自创建新的异常类，这样使我的代码易于理解并避免的过多的消耗内存。



2.	 如果调用者需要根据不同的异常类型作不同的合理处理， 可以抛出checkedException，
	一个checked exceptions实际上就对应一个处理分支。


3.	 保护封装性 
     不要让你要抛出的checked exception升级到较高的层次。例如，不要让SQLException延伸到业务层
      

比如:     

	public void dataAccessCode(){ 
	try{ 
	..some code that throws SQLException 
	}catch(SQLException ex){ 
	throw new RuntimeException(ex); 
	} 
	} 

上边的做法是把SQLException转换为RuntimeException，一旦SQLException被抛出，那么程序将抛出RuntimeException,此时程序被挂起并返回客户端异常信息。 调用该方法的程序代码中也不用再捕获SQLException异常：

	public void do(){
		dataAccessCode();    
	}


**问题 例子 肯定回答时的建议:**

+ 是否发生了致命错误? 
	扩展RuntimeException.
+ 错误是不可恢复的吗?还不清楚? 
	扩展RuntimeException.将可能抛出的异常都写进文档, 以便调用者决定是否捕获和捕获哪些异常

____________ 

<p> 
	&nbsp; &nbsp;在包一级做一个决定, 决定每个包如何使用checked和unchecked exceptions. 将使用unchecked exceptions的决定写进文档,

	因为很多程序员还不习惯它. 使用unchecked exceptions的唯一危险是没有正确地写文档.   
 使用unchecked exceptions时, **一定要将所有可能的异常写进文档**, 让调用者选择是否捕获异常. 理想的情况下, 编译器应该强制将所有的异常写进Javadoc.   <br/> 

&nbsp; &nbsp;当分配JDBC连接这样的在所有情况下都应释放的资源时, 不管你是否需要捕获异常, 切记使用一个finalize块以保证正确的清除. 记住即使没有catch块, 仍然可以使用finalize块.   
 未捕获的运行时异常将杀死执行的线程, 有时这成为避免使用运行时异常的一个理由. 在一些情况下这是一个合理的论据, 但是J2EE应用中这通常不是一个问题, 因为我们极少控制线程, 而是让应用服务器来做这个. 应用服务器将捕获和处理那些在应用代码中没有捕获的运行时异常, 而不会让这些异常挂起整个Java虚拟机. 在EJB容器中发生的一个未捕获的运行时异常将导致EJB容器抛弃当前的EJB实例. 如果发生的错误是致命的, 这通常没什么不对.
</p>




##9.com.travelzen.szframework.core.common.ReturnClass
szframework中定义的，比如Optional略微重量级一些的结果封装, 
可以让调用着根据 ReturnClass.isSuccess， 以及 status， objects等信息作合理的流程控制。


##异常的文档 Document exceptions.
You can use Javadoc's @throws tag to document both checked and unchecked exceptions that your API throws. However, I prefer to write unit tests to document exceptions. Tests allow me to see the exceptions in action and hence serve as documentation that can be executed. Whatever you do, have some way by which the client code can learn of the exceptions that your API throws. Here is a sample unit test that tests for IndexOutOfBoundsException:

	public void testIndexOutOfBoundsException() {
	    ArrayList blankList = new ArrayList();
	    try {
	        blankList.get(10);
	        fail("Should raise an IndexOutOfBoundsException");
	    } catch (IndexOutOfBoundsException success) {}
	}

The code above should throw an IndexOutOfBoundsException when blankList.get(10) is invoked. If it does not, the fail("Should raise an IndexOutOfBoundsException") statement explicitly fails the test. By writing unit tests for exceptions, you not only document how the exceptions work, but also make your code robust by testing for exceptional scenarios.

##异常使用的最佳实践 Best Practices for Using Exceptions

The next set of best practices show how the client code should deal with an API that throws checked exceptions.

###1. Always clean up after yourself
If you are using resources like database connections or network connections, make sure you clean them up. If the API you are invoking uses only unchecked exceptions, you should still clean up resources after use, with try - finally blocks.

	public void dataAccessCode(){
	    Connection conn = null;
	    try{
	        conn = getConnection();
	        ..some code that throws SQLException
	    }catch(SQLException ex){
	        ex.printStacktrace();
	    } finally{
	        DBUtil.closeConnection(conn);
	    }
	}

	class DBUtil{
	    public static void closeConnection
	        (Connection conn){
	        try{
	            conn.close();
	        } catch(SQLException ex){
	            logger.error("Cannot close connection");
	            throw new RuntimeException(ex);
	        }
	    }
	}
	
DBUtil is a utility class that closes the Connection. The important point is the use of finally block, which executes whether or not an exception is caught. In this example, the finally closes the connection and throws a RuntimeException if there is problem with closing the connection.

###2. Never use exceptions for flow control
Generating stack traces is expensive and the value of a stack trace is in debugging. In a flow-control situation, the stack trace would be ignored, since the client just wants to know how to proceed.

In the code below, a custom exception, MaximumCountReachedException, is used to control the flow.

	public void useExceptionsForFlowControl() {
	    try {
	        while (true) {
	            increaseCount();
	        }
	    } catch (MaximumCountReachedException ex) {
	    }
	    //Continue execution
	}

	public void increaseCount()
	    throws MaximumCountReachedException {
	    if (count >= 5000)
	        throw new MaximumCountReachedException();
	}
The useExceptionsForFlowControl() uses an infinite loop to increase the count until the exception is thrown. This not only makes the code difficult to read, but also makes it slower. Use exception handling only in exceptional situations.

###3. Do not suppress or ignore exceptions
When a method from an API throws a checked exception, it is trying to tell you that you should take some counter action. If the checked exception does not make sense to you, do not hesitate to convert it into an unchecked exception and throw it again, but do not ignore it by catching it with {} and then continue as if nothing had happened.

###4. Do not catch top-level exceptions
Unchecked exceptions inherit from the RuntimeException class, which in turn inherits from Exception. By catching the Exception class, you are also catching RuntimeException as in the following code:

try{
..
}catch(Exception ex){
}
The code above ignores unchecked exceptions, as well.
一旦你写出了上边的代码（注意catch块是空的），它将忽略所有的异常，包括unchecked exception.

###5. Log exceptions just once
Logging the same exception stack trace more than once can confuse the programmer examining the stack trace about the original source of exception. So just log it once.

Summary

These are some suggestions for exception-handling best practices. I have no intention of staring a religious war on checked exceptions vs. unchecked exceptions. You will have to customize the design and usage according to your requirements. I am confident that over time, we will find better ways to code with exceptions.

I would like to thank Bruce Eckel, Joshua Kerievsky, and Somik Raha for their support in writing this article.


#杂项

[深入探索 高效的Java异常处理框架]（http://www.sunwei.org/archives/196 ）

http://hi.baidu.com/suofang/item/8956d6412a76cb97833ae1e8


最后推荐几篇关于异常设计原则 
1.异常设计 
http://www.cnblogs.com/JavaVillage/articles/384483.html（翻译） 
http://www.javaworld.com/jw-07-1998/jw-07-techniques.html（原文） 

2. 异常处理最佳实践 
http://tech.e800.com.cn/articles/2009/79/1247105040929_1.html（翻译） 
http://onjava.com/pub/a/onjava/2003/11/19/exceptions.html（原文）




http://topic.csdn.net/u/20080711/13/ba508f6b-faf4-478c-baff-0adc831c8e7b.html

 特别对于szframework-core中的util方法，对于必须try-catch的代码块，
 先log， 再throw一个合适的 RuntimeException, 这样方法的调用者就不需要用try-catch块，

 一般在错误屏障层， catch所有 throwable的异常。

 如果是tomcat中的请求， servlet会处理异常， 屏障所有异常，

 如果在普通线程中执行，需要自己catch所有 throwable的异常来屏障。

 如果在 Executor中，  异常会在getFuture中抛出，Executor会屏障异常。

 checkedException的作用一般是给直接调用者返回一个有意义的值，并把程序跳转到checkedException
 异常处理代码段去执行。


 对于 szframework-core中的util中一般方法， 都不需要用checkedException去返回值， 与其强制调用者
 去写try-cath块， 到不如让调用栈退回“错误屏障层”。

 szframework-core.util中的方法，应该使用 com.google.common.base 中的
   checkArgument(count > 0, "must be positive: %s", count);

http://code.google.com/p/guava-libraries/wiki/ThrowablesExplained 


http://bbs.chinaunix.net/thread-3660890-1-1.html





 对于
	try {
    httpconn.setRequestMethod("POST");
} catch (
    throw CanNeverHappenException("oh dear!");
}

这样的情况， 我们认为这是一种错误用法， 

 httpconn.setRequestMethod函数
应该检查参数，并在参数错误时抛出 IllegalArgumentException， 而不是把参数检查和返回值混淆。 再以  new String(byte[], String)为例， 本方法强制要求catch一个异常， 但一般情况下， 如果本方法异常， 则可以直接推到错误屏障了， 如果期望在当前编码错误的情况下尝试其他编码， 则应该自己catch-RuntimeException,也比强制  catch要好。 作为更好的时间，可以这样写： 
Charsets

###new String时候可以不加try-catch
Don't do this:
try {
   bytes = string.getBytes("UTF-8");
} catch (UnsupportedEncodingException e) {
   // how can this possibly happen?
   throw new AssertionError(e);
} 

Do this instead:
bytes = string.getBytes(Charsets.UTF_8);


byte[] byteGBK = null;  
        byte[] byteUTF8 = null;  
        byteGBK = strChineseString.getBytes(Charset.forName("GBK"));  
        byteUTF8 = strChineseString.getBytes(Charset.forName("utf-8"));  

========================================




###UncaughtExceptionHandler

If you're using JDK 5+, then you may define your own UncaughtExceptionHandler. AnUncaughtExceptionHandler can be defined at 3 different levels. From the highest level to the lowest level, they are:
for the whole Java Runtime, call Thread.setDefaultUncaughtExceptionHandler
for a ThreadGroup, override ThreadGroup.uncaughtException
for a single Thread, call Thread.setUncaughtExceptionHandler




Error 是非常严重的问题， 不应该捕捉， 而应该让jvm自己处理， 做coredump，并由nagios报警。
AssertionError, AWTError, CoderMalfunctionError, FactoryConfigurationError, LinkageError, ThreadDeath,TransformerFactoryConfigurationError, VirtualMachineError