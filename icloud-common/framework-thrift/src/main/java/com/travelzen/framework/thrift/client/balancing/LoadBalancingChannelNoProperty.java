package com.travelzen.framework.thrift.client.balancing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.util.TZCuratorFramework;
import com.travelzen.framework.util.TZCuratorFrameworkNoProperty;

public abstract class LoadBalancingChannelNoProperty<T> implements Client<T> {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	// private List<Client<T>> underlying;
	private Random rand = new Random();
	private List<String> serverNameList;// 备注：serverName必须合法:ip:port
	private Map<String, Client<T>> underlyingMap;

	// 是否使用curtor framework
	private boolean isUseCurator;
	private String serviceName;
	private String prefix;

	/**
	 * list代表的是服务端地址，每个地址必须符合这样的格式: ip:port,譬如：192.168.163.54:9860
	 * serviceName必须是跟服务端的服务名一致。null表示取消无缝切换功能
	 * 
	 * @param list
	 * @param servcieName
	 */
	public LoadBalancingChannelNoProperty(List<String> list, String prefix, String serviceName) {
		// Map<String, Client<T>> clientMap, String servcieName) {
		// underlying = new ArrayList<Client<T>>(clients);
		if (list == null || list.size() == 0) {
			throw new ClientUnavailableException();
		}
		serverNameList = new ArrayList<String>();
		underlyingMap = new HashMap<String, Client<T>>();
		for (String ipAndPort : list) {
			serverNameList.add(ipAndPort);
			crateThriftCient(ipAndPort);
		}
		// for (Entry<String, Client<T>> entry : clientMap.entrySet()) {
		// serverNameList.add(entry.getKey());
		// underlyingMap.put(entry.getKey(), entry.getValue());
		// }
		if (serviceName != null && serviceName.length() > 0 && prefix != null && prefix.length() > 0) {
			isUseCurator = true;
			this.serviceName = serviceName;
			// TZCuratorFramework.getRpcAdress(serviceName);
			this.prefix = prefix;
			TZCuratorFrameworkNoProperty.getRpcAdress(serviceName, prefix);
		}

	}

	/**
	 * 如果使用Curator的话，所有的serverName都用zookeeper取;
	 * 如果使用本地的话，所有的serverName都用serverNameList取
	 */
	public T proxy() throws Exception {
		List<Client<T>> healthyClients = new ArrayList<Client<T>>();

		List<String> list = null;

		if (isUseCurator) {// 如果使用curator
			List<String> rpcList = TZCuratorFrameworkNoProperty.getRpcAdress(serviceName, prefix);
			if (rpcList == null || rpcList.size() == 0) {
				list = serverNameList;
			} else {
				list = rpcList;
			}
		} else {
			list = serverNameList;
		}
		logger.info("rpcList--->"+list);
		for (String serverName : list) {
			Client<T> client = getClient(serverName);
			if (client.isHealthy()) {
				healthyClients.add(client);
			}
		}

		if (healthyClients.size() == 0)
			throw new ClientUnavailableException();
		// System.out.println(healthyClients);
		int index = rand.nextInt(healthyClients.size());
		Client<T> client = healthyClients.get(index);
		// logger.debug("use client: {}", client.toString());
		// System.out.println("use client " + client.toString());
		return client.proxy();
	}

	/**
	 * 检查serverNameList
	 */
	public boolean isHealthy() {
		for (String serverName : serverNameList) {
			Client<T> client = getClient(serverName);
			if (client != null && client.isHealthy())
				return true;
		}
		return false;
	}

	public abstract Client<T> crateThriftCient(String ip, int port);

	private synchronized Client<T> crateThriftCient(String serverName) {
		if (serverName == null)
			return null;
		// 做一次检查
		Client<T> client = underlyingMap.get(serverName);
		if (client != null) {
			return client;
		}

		String ip = null;
		int port = 0;
		String[] params = serverName.split(":");
		if (params.length == 2) {
			ip = params[0];
			port = Integer.parseInt(params[1]);
			if (port > 0) {
				client = crateThriftCient(ip, port);
				if (client != null) {
					underlyingMap.put(serverName, client);// 将生成的client放入underlyingMap中.
				}
			}
		}
		return client;
	}

	private Client<T> getClient(String serverName) {
		// TODO Auto-generated method stub
		Client<T> client = underlyingMap.get(serverName);
		if (client == null) {
			client = crateThriftCient(serverName);
		}
		return client;
	}
}
