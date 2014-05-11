package com.icloud.framework.config.tops.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.api.CuratorWatcher;
import com.icloud.framework.config.tops.zk.TopsCuratorFramework;
import com.icloud.framework.core.util.StringUtil;
import com.icloud.framework.core.util.TZUtil;
import com.icloud.framework.util.TZCuratorFrameworkNoProperty;

public class ZkPropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(ZkPropertiesUtil.class);
	private static Map<String, byte[]> zkMap = new ConcurrentHashMap<String, byte[]>();

	public static byte[] getValueFromZk(String zkPath, boolean isNeedCached) {
		if (StringUtil.isEmpty(zkPath))
			return null;
		zkPath = zkPath.trim();
		if (isNeedCached && zkMap.containsKey(zkPath)) {
			byte[] value = zkMap.get(zkPath);
			return value;
		} else {
			try {
				DataWatcher dataWatcher = null;
				if (isNeedCached) {
					dataWatcher = new DataWatcher();
				}
				Stat stat = TopsCuratorFramework.getInstance().checkNodePath(zkPath, dataWatcher);
				if (stat != null) {// 节点存在，去获取数据
					byte[] data = TopsCuratorFramework.getInstance().getData(zkPath);
					if (isNeedCached && !TZUtil.isEmpty(data)) {
						zkMap.put(zkPath, data);
					}
					return data;
				} else {
					zkMap.remove(zkPath);
					return null;
				}
			} catch (Throwable thr) {
				logger.error("获取zookeeper节点数据异常", thr);
				return null;
			}
		}
	}

	public static byte[] getValueFromZKNoCache(String zkPath) {
		return getValueFromZk(zkPath, false);
	}

	private static class DataWatcher implements CuratorWatcher {

		@Override
		public void process(WatchedEvent event) throws Exception {
			try {
				String path = StringUtil.trim(event.getPath());
				// 重新安装数据变动watcher
				TZCuratorFrameworkNoProperty.getCuratorFramework().checkExists().usingWatcher(new DataWatcher()).forPath(path);
				DataWatcher dataWatcher = null;
				dataWatcher = new DataWatcher();
				Stat stat = TopsCuratorFramework.getInstance().checkNodePath(path, dataWatcher);

				if (event.getType() == EventType.NodeDataChanged || event.getType() == EventType.NodeCreated) {
					try {
						byte[] data = TopsCuratorFramework.getInstance().getData(path);
						if (TZUtil.isEmpty(data))
							zkMap.put(path, data);
					} catch (Throwable thr) {
						logger.error("", thr);
					}
				} else if (event.getType() == EventType.NodeDeleted) { // 配置不存在了
					zkMap.put(path, null);
				}
			} catch (Throwable thr) {
				logger.error("", thr);
			}

		}
	}
}
