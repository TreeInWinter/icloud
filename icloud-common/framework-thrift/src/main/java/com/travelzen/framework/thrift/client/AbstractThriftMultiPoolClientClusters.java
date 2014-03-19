package com.travelzen.framework.thrift.client;

import java.util.Map;

import org.apache.thrift.TServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.thrift.client.ClientCluster;
import com.travelzen.framework.thrift.client.ThriftClientContext;
import com.travelzen.framework.thrift.client.ThriftClientMultiPool;

/**
 * Initialize thrift clients, get the clients' configuration from configuration
 * file.
 * 
 * @author taosu
 * 
 */
public abstract class AbstractThriftMultiPoolClientClusters<T extends TServiceClient>
		extends AbstractThriftClientClusters<T> {

	/** Log instance. */
	static final Logger logger = LoggerFactory
			.getLogger(AbstractThriftMultiPoolClientClusters.class);
	/** all accessible client. */
	protected Map<String, ClientCluster> clusters = null;
	
	public AbstractThriftMultiPoolClientClusters(
			Map<String, ClientCluster> clusters) {
		this.clusters = clusters;
	};

	// !!!
	// Remember to call these before/after using this class.
	//
	public void init() throws Exception {
		this.pool = createPool();
	}

	public void destroy() throws Exception {
		pool.close();
	}

	abstract public ThriftClientMultiPool<T> createPool();

	//
	// public void closePool() throws Exception {
	// pool.close();
	// }

	// --------------------------------------------------------
	// 负载均衡部分
	//
	@Override
	final public ThriftClientContext<T> nextClientContext(
			ThriftClientContext<T> tcCxt) {
		return getClientContext(tcCxt);
	}

	@Override
	// robin ring
	final public ThriftClientContext<T> getClientContext(
			ThriftClientContext<T> preTcCxt) {
		T client = null;
		String clusterKey = null;
		if (preTcCxt == null) {
			clusterKey = "0";
		} else {
			Integer preClusterKey;
			try {
				preClusterKey = Integer.parseInt(preTcCxt.getClusterKey());
			} catch (NumberFormatException e) {
				logger.debug("[InvalidKey]key:{}", clusterKey);
				ThriftClientContext<T> ret = new ThriftClientContext<T>(client,
						clusterKey, this.clusters.get(clusterKey));
				ret.setNoCluster(true);
				ret.setErrMsg(e.getMessage());
				return ret;
			}

			Integer key = preClusterKey + 1;
			if (key > clusters.size() - 1) {
				logger.debug(
						"[AllClusterFailed]cur key:{} > clusters' szie:{}",
						key, clusters.size());
				ThriftClientContext<T> ret = new ThriftClientContext<T>(client,
						clusterKey, this.clusters.get(clusterKey));
				ret.setNoCluster(true);
				ret.setErrMsg("No avaliable cluster.");
				return ret;

			}
			clusterKey = key.toString();
		}
		logger.debug("[GetClient][Begin]key:{}", clusterKey);
		try {
			client = pool.getClient(clusterKey);
		} catch (Exception e) {
			logger.debug("[GetClient][Failed]key:{} for: {}", clusterKey,
					e.getMessage(), e);
			ThriftClientContext<T> ret = new ThriftClientContext<T>(client,
					clusterKey, this.clusters.get(clusterKey));
			ret.setNoCluster(false);
			ret.setErrMsg(e.getMessage());
			return ret;
		}
		logger.debug("[GetClient][OK]key:{}", clusterKey);
		return new ThriftClientContext<T>(client, clusterKey,
				this.clusters.get(clusterKey));

	}

	@Override
	final public void releaseClient(ThriftClientContext<T> context) {
		try {
			pool.releaseClient(context.getClusterKey(), context.getClient());
		} catch (Exception e) {
			logger.warn("ClientPool releaseClient Exception : {}",
					e.getMessage());
		}
	}

}
