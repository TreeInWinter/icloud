package com.travelzen.framework.thrift.client.balancing;

import java.lang.reflect.Constructor;

import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.travelzen.framework.thrift.protocol.RIThriftProtocol;

class ThriftConnection<T> extends Connection<T> {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private String host;
    private int port;
    private boolean framed;

    private int SO_TIMEOUT = 100000;
    private TSocket socket;
    private TTransport transport;
    private TProtocol protocol;
    private boolean didFailConnect = false;

    private T client;

    public ThriftConnection(String host, int port, boolean framed, Class<? extends T> clazz) throws Exception {
        this.host = host;
        this.port = port;
        this.framed = framed;
        socket = new TSocket(host, port, SO_TIMEOUT);
        if(framed) transport = new TFramedTransport(socket);
        else transport = socket;
        protocol = new RIThriftProtocol(transport);   // changed to RIThriftProtocol
        Constructor constructor = clazz.getDeclaredConstructor(TProtocol.class);
        client = (T)constructor.newInstance(protocol);
    }

    public T getClient() throws Exception {
        return client;
    }

    public void ensureOpen() {
        if(transport.isOpen()) return;
        try {
            transport.open();
        } catch (TTransportException e) {
            didFailConnect = true;
            log.error(e.getMessage(), e);
        }
    }

    public void teardown() {
        try{
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        protocol.getTransport().close();
    }

    public void flush() {
        // try {
        //     transport.flush();
        // } catch (Exception ex) {
        //     teardown();
        //     didFailConnect = true;
        //     log.error(ex.getMessage(), ex);
        // }
    }

    public boolean isHealthy() {
        return !didFailConnect && super.isHealthy();
    }
}
