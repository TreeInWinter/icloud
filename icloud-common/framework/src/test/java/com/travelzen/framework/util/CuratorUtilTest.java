package com.travelzen.framework.util;

import com.netflix.curator.framework.CuratorFramework;
import com.travelzen.framework.util.CuratorUtil;

public class CuratorUtilTest {
	public static void main(String[] args) {
//		CuratorFramework client = CuratorUtil.client("zk1.sh.idc.yunyun.com", "YRFS/jinjing/search/suggest/blacklist");
		CuratorFramework client = CuratorUtil.client("zk1.internal.yunyun.com", "/YRNS/yunyun/search/related_search/rs/0/rpc");
//		CuratorFramework client = CuratorUtil.client("zk1.internal.yunyun.com,zk2.internal.yunyun.com,zk3.internal.yunyun.com,zk4.internal.yunyun.com,zk5.internal.yunyun.com", "YRFS/yunyun/search/query_rewriter");
//		CuratorFramework client = CuratorUtil.client("zk1:2181,zk2:2181,zk3:2181,zk4:2181,zk5:2181", "YRFS/yunyun/search/query_rewriter");
		client.start();
//		for (String path : CuratorUtil.children(client))
//			System.out.println(path);
//		System.out.println(CuratorUtil.getData(client, "/microblog_blacklist.data"));
//		System.out.println(CuratorUtil.getData(client, "/microblog_whitelist.data"));
//		System.out.println(CuratorUtil.getData(client, "/microblog_sensitive.data"));
//		System.out.println(CuratorUtil.getData(client, "/microblog_account_blacklist.data"));
//		System.out.println(CuratorUtil.getData(client, "/update_levels"));
		System.out.println(CuratorUtil.getData(client, "/1"));
//		System.out.println(CuratorUtil.getData(client, "/whitelist_query_8"));
		client.close();
	}
}
