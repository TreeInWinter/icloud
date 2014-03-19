http://www.blogjava.net/hulizhong/archive/2009/05/04/268846.html


转 http://javathinker.blog.ccidnet.com/blog-htm-itemid-1262479-do-showone-type-blog-uid-36384.html

在《Pragmatic AJAX中文问题 A Web 2.0 Primer 》中偶然看到对readyStae状态的介绍，感觉这个介绍很实在，摘译如下：

0: (Uninitialized) the send( ) method has not yet been invoked. 
1: (Loading) the send( ) method has been invoked, request in progress. 
2: (Loaded) the send( ) method has completed, entire response received.
3: (Interactive) the response is being parsed. 
4: (Completed) the response has been parsed, is ready for harvesting. 

0 － （未初始化）还没有调用send()方法
1 － （载入）已调用send()方法，正在发送请求
2 － （载入完成）send()方法执行完成，已经接收到全部响应内容
3 － （交互）正在解析响应内容
4 － （完成）响应内容解析完成，可以在客户端调用了

对 于readyState的这五种状态，其他书中大都语焉不详。像《Foundations of AJAX中文问题》中，只在书中的表2-2简单地列举了状态的“名称”－－The state of the request. The five possible values are 0 = uninitialized, 1 = loading, 2 = loaded, 3 = interactive, and 4 = complete。而《Ajax in Action》中好像根本就没有提到这5种状态的细节。《Professional AJAX中文问题》中虽不尽人意，但还是有可取之处：

There are five possible values for readyState: 
0 (Uninitialized): The object has been created but the open() method hasn’t been called. 
1 (Loading): The open() method has been called but the request hasn’t been sent. 
2 (Loaded): The request has been sent. 
3 (Interactive). A partial response has been received. 
4 (Complete): All data has been received and the connection has been closed. 

readyState有五种可能的值：
0 (未初始化)： (XMLHttpRequest)对象已经创建，但还没有调用open()方法。
1 (载入)：已经调用open() 方法，但尚未发送请求。
2 (载入完成)： 请求已经发送完成。
3 (交互)：可以接收到部分响应数据。
4 (完成)：已经接收到了全部数据，并且连接已经关闭。

在《Understanding AJAX中文问题: Using JavaScript to Create Rich Internet Applications》中，则用下表进行了说明：

readyState Status Code
Status of the XMLHttpRequest Object 
(0) UNINITIALIZED
未初始化 The object has been created but not initialized. (The open method has not been called.)
（XMLHttpRequest）对象已经创建，但尚未初始化（还没有调用open方法）。 
(1) LOADING
载入 The object has been created, but the send method has not been called.
（XMLHttpRequest）对象已经创建，但尚未调用send方法。 
(2) LOADED
载入完成 The send method has been called, but the status and headers are not yet available.
已经调用send方法，（HTTP响应）状态及头部还不可用。 
(3) INTERACTIVE
交 互 Some data has been received. Calling the responseBody and responseText properties at this state to obtain partial results will return an error, because status and response headers are not fully available.
已经接收部分数据。但若在此时调用responseBody和responseText属性获取部分结果将会产生错误，因为状态和响应头部还不完全可用。 
(4) COMPLETED
完成 All the data has been received, and the complete data is available in the responseBody and responseText properties.
已经接收到了全部数据，并且在responseBody和responseText属性中可以提取到完整的数据。 

根 据以上几本书中的关于readyState五种状态的介绍，我认为还是《Pragmatic AJAX中文问题 A Web 2.0 Primer 》比较到位，因为它提到了对接收到的数据的解析问题，其他书中都没有提到这一点，而这一点正是“(3)交互”阶段作为一个必要的转换过程存在于“(2)载 入完成”到“(4)完成”之间的理由，也就是其任务是什么。归结起来，我觉得比较理想的解释方法应该以“状态：任务（目标）+过程+表现（或特征）”表达 模式来对这几个状态进行定义比较准确，而且让人容易理解。现试总结如下：

readyState 状态
状态说明

(0)未初始化
此阶段确认XMLHttpRequest对象是否创建，并为调用open()方法进行未初始化作好准备。值为0表示对象已经存在，否则浏览器会报错－－对象不存在。

(1)载入
此阶段对XMLHttpRequest对象进行初始化，即调用open()方法，根据参数(method,url,true)完成对象状态的设置。并调用send()方法开始向服务端发送请求。值为1表示正在向服务端发送请求。

(2)载入完成
此阶段接收服务器端的响应数据。但获得的还只是服务端响应的原始数据，并不能直接在客户端使用。值为2表示已经接收完全部响应数据。并为下一阶段对数据解析作好准备。

(3)交互
此阶段解析接收到的服务器端响应数据。即根据服务器端响应头部返回的MIME类型把数据转换成能通过responseBody、responseText或responseXML属性存取的格式，为在客户端调用作好准备。状态3表示正在解析数据。

(4)完成
此阶段确认全部数据都已经解析为客户端可用的格式，解析已经完成。值为4表示数据解析完毕，可以通过XMLHttpRequest对象的相应属性取得数据。


概而括之，整个XMLHttpRequest对象的生命周期应该包含如下阶段：
创建－初始化请求－发送请求－接收数据－解析数据－完成

在 具体应用中，明确了readyState的五个状态（XMLHttpRequest对象的生命周期各个阶段）的含义，就可以消除对Ajax核心的神秘感 （语焉不详的背后要么是故弄玄虚，制造神秘感；要么就是“以其昏昏，使人昭昭”），迅速把握其实质，对减少学习中的挫折感和增强自信心都极其有益。

比如，通过如下示例：


//声明数组
var states ＝ [“正在初始化……”,
“正在初始化请求……成功！
正在发送请求……”,
“成功！
正在接收数据……”,
“完成！
正在解析数据……”,
“完成！
”]; 

//回调函数内部代码片段
if (xmlHttp.readyState==4)
{
var span = document.createElement(“span”);
span.innerHTML = states[xmlHttp.readyState];
document.body.appendChild(span); 

if (xmlHttp.status == 200)
{
var xmldoc = xmlHttp.responseXML;
//其他代码
} 

//别忘记销毁，防止内存泄漏
xmlHttp = null;
}else{
var span = document.createElement(“span”);
span.innerHTML = states[xmlHttp.readyState];
document.body.appendChild(span);
}结果如下：

正在初始化请求……成功！
正在发送请求……成功！
正在接收数据……完成！
正在解析数据……完成！

我们很容易明白XMLHttpRequest对象在各个阶段都在做什么。因此，也就很容易对Ajax的核心部分有一个真正简单明了的理解。
