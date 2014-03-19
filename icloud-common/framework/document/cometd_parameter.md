如果因为client 端响应过慢而导致 transport queue中的数据没有发送完， 应该通知url sender 暂缓发送

由于 deliver 过程本身不会阻塞， 所以 在

session.deliver(session, JProxyDAOConst.URLDELIVER_CHANNEL, id, id);
加 timelimiter是没有意义的。

传输时的超时， 会直接抛给容器处理。


server端只能捕捉到   maxQueue消息。

client断开， 则servlet端抛一样， 但  session无法获得异常，

bayue会检测到 timeout， 并在可用session列表中移除该session，  但已发生的 deliver已不可恢复。

如果要保证可靠的送达， 必须要监听client端发送的 确认响应。


deliver出去一段之间之后， 如果没有收到确认响应，  应该给予重传。

用ehcache的 失效机制，并加监听器， 在过期的时候重传。


					
					




Parameter Name	Default Value	Parameter Description
timeout	30000	The time, in milliseconds, that a server will wait for a message before responding to a long poll with an empty response
ws.timeout	15000	Like the timeout parameter, but for the websocket transport
interval	0	The time, in milliseconds, that specifies how long the client must wait between the end of one long poll requests and the start of the next
ws.interval	2500	Like the interval parameter but for the websocket transport
maxInterval	10000	The max period of time, in milliseconds, that the server will wait for a new long poll from a client before that client is considered invalid and is removed
ws.maxInterval	15000	Like the maxInterval parameter, but for the websocket transport
maxLazyTimeout	5000	The max period of time, in milliseconds, that the server will wait before delivering or publishing lazy messages
metaConnectDeliverOnly	false	Whether the transport should deliver the messages only via long poll (enables strict message ordering)
jsonDebug	false	Whether or not the full JSON input should be kept for debugging purposes
maxSessionsPerBrowser	1	The max number of sessions (tab/frames) allowed to long poll from the same browser; a negative value allows unlimited sessions (see also here)
allowMultiSessionsNoBrowser	false	Whether to allow multiple sessions (tab/frames) in case the browser cannot be detected (see also here)
multiSessionInterval	2000	The period of time, in milliseconds, that specifies the client normal polling period in case the server detects more sessions (tabs/frames) connected from the same browser than allowed by the maxSessionsPerBrowser parameter. A non-positive value means that additional sessions will be disconnected.
long-polling.json.metaConnectDeliverOnly	false	Whether or not delivery of messages should only happen via /meta/connect. Enabling this option allows for strict message ordering at the cost of a slightly more chattier protocol (because delivery via /meta/connect requires waking up the long poll).