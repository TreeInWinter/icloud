package com.icloud.framework.common;

public class FrameworkHttpConstant {
	
	public final static String AdvApplicationService = "appService"; 

	
	static public final String RECONNET_TIME="重连时间";
	public final static  int DEFAUL_RECONNECT_TIME=1000 ;
	public final static  int DEFAUL_RECONNECT_CNT=3 ;
	
	static public final int   DEFAULT_CONNECTING_TIMEOUT = 60000;
	static public final int  DEFAULT_CONNECTION_MANAGER_TIMEOUT = 120000;
	public final static String CONNECTING_TIMEOUT="连接超时";
	public final static String CONNECTION_MANAGER_TIMEOUT="连接管理器超时";
	
	/**
	 * 

爬虫配置： 名称=conf1,编码=GBK,最大线程数=25,站点最大并发数=5,抓取延迟=500,
引擎=single Thread,输出=数据库, 连接超时=50s,读取超时=60s，连结管理器超时=120s.
	 */
	public final static String MAX_TOTAL_CONNECTIONS="最大并发访问数";
	public final static String MAX_HOST_CONNECTIONS="站点最大并发数";
	

	public final static String SO_TIMEOUT="读取超时";
	
	
static public final String RETURN_TYPE="返回类型";
	
	
	static public final String ROOT_PAGE="rootPage";
	
	static public final String PROXY_SERVER="代理服务器";
	
	static public final String EXTERNAL_CONF_FILE="外部配置文件";
	
	static public final String RECOGNIZE="识别";
	static public final String STEP_LENGHT="步长";
	static public final String VAR_REPLACE="替换";
	static public final String VAR_PARAM="参数";
	
	static public final String VARIANT="变量";
	
	static public final String PAGE_FINGERPRINT_CREATE="页面指纹生成";
	
	
	static public final String ENCODING="编码";
	static public final String NAME="名称";
	
	static public final String SAVE="保存";
	static public final String BRING="带入次级页面";
	
	static public final String FULL_MATCH="fullMatch";
	static public final String HREF="href";
	static public final String ENTRANCE="uubotkey_entrance";
	static public final String EXTEND="extend";
	
	static public final String REVERSE_MATCH="reverse";
	
	
	static public final String SUBMERGE="submerge";
	static public final String RECUSIVE="recursive";
	static public final String ANCHOR="anchor";
	static public final String FORK="fork";
	static public final String ENTER="enter";
	
	static public final String GRP_MODE="组模式";
	
	
	static public final String FIRST="first";
	static public final String ALL="all";
	static public final String MERGE="merge";
	static public final String COMBINE="combine";
	
	static public final String HIERARCHY="hierarchy";
	
	
	static public final String HIERARCHY_NAME="key";
	static public final String HIERARCHY_FIELD="field";
	
	static public final String OUPUT="输出";
	static public final String DB="数据库";
	static public final String CONSOLE="控制台";
	
	static public final String RECONNET_CNT="重连次数";
	static public final String RECONNET="重连";
	
	static public final String FIELD_NAME="field";
	
	static public final String KEY="key";
	static public final String VALUE="value";
	
	static public final String PATTERN="pattern";
	static public final String JS="JS";
	
	static public final String CN_YES="是";
	static public final String CN_NO="否";
	
	static public final String FETCHER="抓取器";
	static public final String FETCHER_HTTPCLIENT="HttpClient";
	static public final String FETCHER_JDK="java.net";
	
	static public final String DEEP_VALUE="深度内容";
	static public final String RANGE="范围";
	static public final String POST="post";
	static public final String GET="get";
	static public final String MAX_MATCH="最多匹配";
	
	static public final String MAX_MATCH2="最大匹配";
	
	static public final String MAX_NODE_MATCH="最多节点匹配";
	static public final String MAX_NODE_MATCH2="最大节点匹配";
	
	static public final String MAX_THREAD="最大线程数";
	
	static public final String MAX_FETCH_THREAD="最大抓取线程数";
	
	static public final String POSITIVE_CLOSE="被动关闭";
	
	static public final String CRAWLER_DALEY="抓取延迟";
	static public final String PARENT_PAGE="parentPage";
	static public final String THIS_PAGE="this";
	static public final String PROCESS_STANDARD="处理条件";
	
	static public final String MAX_DEPTH="最大深度";
	
	static public final String REGULAR="正则";
	
	
	static public final String VM_ENGINE="引擎";
	static public final String VM_SINGL_THREAD="SingleThread";
	static public final String VM_MUTIL_THREAD="MultiThread";
	static public final String VM_FINITE_MUTIL_THREAD="FiniteMultiThread";
	
	
	static public final String RECRORD_FETCHED_LIST="记录已爬列表";
	
	
	static public final String SKIP="skip";
	static public final String MATCH_TRUE="true";
	
	static public final String NULL="NULL";
	static public final String TRUE="TRUE";
	static public final String FALSE="FALSE";
	
	static public final String STAX="STAX"; 
	
	

	
	static public final int DEFAUL_RESULT_CACHE_SIZE=1000;
	
	
	
	//默认每个站点并发10个链接
	static public final int DEFAULT_BACKUP_THREAD_CNT=5;
	
	
	//默认每个站点并发10个链接
	static public final int DEFAULT_LINK_PERHOST=10;
	
	static public final int MAX_REWAIT_CNT=3;
	static public final int DEFAULT_SO_TIMEOUT=60000;
	static public final int DEFAULT_SO_READ_TIMEOUT=60000;

	static public final int DEFAULT_MAX_TOTAL_CONNECTIONS=50;
	
	
	
	static public final String TO_BYTE=":toByte";
	
	static public final String CUR_PAGEURL="CUR_PAGEURL";
	static public final String AR_META="META";
	static public final String AR_IMAGE = "IMAGE";
	static public final String AR_CONTENT = "CONTENT";
	static public final String AR_PRICE="PRICE";
	
	
	static public final String DIRECTION = "方向";
	static public final String ACTION = "动作";
	static public final String DOWNLOAD = "下载";
	
	static public final String DIRECTION_REVERSE = "反";
	
	
	static public final String FILE_PREFIX="file:///";
	
	static public final String SPLIT_CONTENT_LINE="splitcontentline:///";
	
	static public final String FUNCTOIN_POSTFIX=":FUNCTION";
	
	
	static public final String ACTION_TYPE_UPDATE="update";
	static public final String ACTION_TYPE_INSERT="insert";
	static public final String ACTION_TYPE_BATCH_INSERT="batchInsert";
	static public final String ACTION_TYPE_INSERT_BATCH="InsertBatch";
	static public final String ACTION_TYPE_DELETE="delete";
	static public final String ACTION_TYPE_LIST="list";


	static public final int MAX_TRAIL_SAVE_CNT=10;
	
	
	static public final String URL_SEPARATOR="--->";
}
