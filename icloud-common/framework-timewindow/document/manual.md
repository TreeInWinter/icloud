轻量级内存流计算框架   szframework_timewindow:


本框架的特点：

（1）对于业务线程完全无锁、无阻塞， 丝毫不影响业务线程。
（2）可以精确控制 内存使用量， 不影响宿主JVM。
（3）所有操作都是O(0)常量复杂度， 性能可扩展。



	目前我实现了第一个计算场景： TimeSectionAggregateCounter
	 * 过去5分钟内 a ->123次 b -> 456次
    * expire the whole frame on every evict operation
	按时间分片来进行聚合运算。
	
==========================================	
	
	另外马上会实现另外3个计算需求：
	
	com/travelzen/szframework/timewindow/actor
	
	1) String2StringListTimeLimitHolderActor
	 *   按 frame 整帧过期 ,     expire whole frame every timewindow
 *   
 *   帧内是    CircularFifoBuffer, 可以指定每个key上最多保留多少条记录  
 *   广告主a 最近5分钟点击过的创意名 -> （abc, def, ghj ..,）,
 *   广告主b 最近5分钟点击过的创意名 -> （abc, def, ghj ..,）,
 *   广告主c 最近5分钟点击过的创意名 -> （abc, def, ghj ..,）,
 
   2) StringListSizeLimitHolderActor
 *    最近N分钟处理过的url -> （a.com, b.com）, frame中的url按数量过期(超出最大数量之后，淘汰最老的URL)
 
 
  3) StringListTimeLimitHolderActor
   *   最近5分钟处理过的url -> （a.com, b.com）, frame中的url按时间过期(超出最大时间之后，淘汰最老的URL)
 
 
	
	

代码在：
https://sourcer.travelzen.com/svn/yr/projects/branches/develop/ads/szframework_timewindow


测试代码路径:  
	 szframework_timewindow/src/test/java/com/travelzen/szframework/core/timewindow/TestTimewindow.java
	 
＝＝＝框架使用方法：＝＝＝＝
初始化:

	(1)构造一个每1000ms发出一个evict消息的 TimeWindowFactory
   
	TimeWindowFactory factory = new TimeWindowFactory(1);


	(2) 从TimeWindowFactory中构造一个timewindow

		final int windowLengthSec = 20; sec 
		final int frameCnt = 10;//  
		final int maxMagebytes = 10;
		
		
		final TimeSectionAggregateCounter aggregateCounter = factory
				.createTimeSectionAggregateCounter("test", windowLengthSec,
						frameCnt, maxMagebytes);

		 
		 对于任意多的工作线程， 可以公用同一个  aggregateCounter, 调用processStringNumberPair，使数据流过aggregateCounter
		 
		 
							Pair<String, AtomicLong> entry = Pair.with("key",
									new AtomicLong(1));
							aggregateCounter.processStringNumberPair(entry);
							
							
	(3) 然后我们可以在一个额外的监控数据发送线程（MonitorDataSender）中调用
	
	List<Map<String, Long>> retall = counter
							.getAllFrameResult();
		这是一个同步的方法， 会阻塞， 但是在MonitorDataSender线程中是允许阻塞的。
		
		


===如何实现其他的计算需求＝＝＝＝
		
(1)仿造 String2LongCounterActor 的代码， 实现类似	

 processEntry(Pair<String, AtomicLong> entry, long timestamp)
		
的方法， 处理流入的数据。 并在内存数据结构中进行运算。

(2) 实现evict方法， 保证 frame可以被过期释放。

(3)实现类西 getAllFrameResult( RP rp) 的方法，提供计算结果， 使得MonitorDataSender可以获得计算结果。

(4) 实现类似 initilizeTimeWindow()的方法， 初始化数据结构。



		
		
							
							
							
