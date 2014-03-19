package com.travelzen.framework.common;

public class FrameworkConstants {
	
	/** 记录状态 正常 **/
	public final static int STATUS_NORMAL = 0; 

	/** 等待审核 **/
	public final static int STATUS_IS_CHECKING = 1; 

	/** 审核未通过 **/
	public final static int STATUS_NOT_PASS = 2; 
	
	/** 取消申请 **/
	public static final int STATUS_CANCEL_APPLY = 3;
	
	/** 已过期 **/
	public final static int STATUS_OVERDUE = 7; 
	
	/** 禁用,冻结 **/
	public final static int STATUS_FORBIDDEN = 8; 

	/** 删除 **/
	public final static int STATUS_DELETED = 9; 
	
	
	
	//	 result dao code
	public final static int CODE_DEFAULT_RESULT = -1;

	public final static int CODE_DAO_SUCCESS = 1;

	public final static int CODE_DAO_FAILURE = -1;
	
	
	public final static int PAGE_SIZE_DEFAULT = 15; //分页默认记录数

}
