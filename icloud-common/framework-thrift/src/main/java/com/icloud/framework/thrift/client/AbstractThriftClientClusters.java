package com.icloud.framework.thrift.client;

import org.apache.thrift.TException;
import org.apache.thrift.TServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractThriftClientClusters<T extends TServiceClient>
		implements IThriftClientClusters<T> {
	static final Logger logger = LoggerFactory
			.getLogger(AbstractThriftClientClusters.class);
	protected ThriftClientMultiPool<T> pool = null;

	@Override
	final public <P, R> R processWithRetry(Function<P, R> f, P searchReq) {
		R ret = null;
		ThriftClientContext<T> oldTcCxt = null;
		ThriftClientContext<T> tcCxt = null;
		int maxRetry = 100;
		while (maxRetry-- > 0) {
			long beginTime = System.currentTimeMillis();

			tcCxt = nextClientContext(oldTcCxt);
			if (tcCxt.isNoCluster()) {
				// can not get the alive client
				logger.info(tcCxt.getErrMsg());
				return null;
			}
			oldTcCxt = tcCxt;

			if (tcCxt.getClient() == null) {
				continue;
			}
			try {
				ret = f.apply(searchReq, tcCxt);
				return ret;
			} catch (TException e) {
				logger.warn("Cluster failed:{} ", tcCxt.getCluster(), e);
				T client = tcCxt.getClient();
				client.getInputProtocol().getTransport().close();
				client.getOutputProtocol().getTransport().close();
			} finally {
				releaseClient(oldTcCxt);
				logger.debug("Try SearchRoot:{} {} in {}ms.",
						tcCxt.getClusterKey(), ret != null ? "OK" : "Fail",
						System.currentTimeMillis() - beginTime);
			}
		}
		return null;
	}
}
