package com.travelzen.tops.thrift.balancing;

import java.util.List;
import java.util.Map;

import service.demo.Hello;
import service.demo.Hello.Iface;

import com.travelzen.framework.thrift.client.balancing.Client;
import com.travelzen.framework.thrift.client.balancing.LoadBalancingChannel;
import com.travelzen.framework.thrift.client.balancing.WjThriftClient;

public class HelloClientLoadBalancingChannel extends LoadBalancingChannel<Hello.Iface> {

	public HelloClientLoadBalancingChannel(List<String> list, String servcieName) {
		super(list, servcieName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Client<Iface> crateThriftCient(String ip, int port) {
		// TODO Auto-generated method stub
		Client<Hello.Iface> tc = new WjThriftClient<Hello.Iface, Hello.Client>(ip, port, true, Hello.Client.class);
		return tc;
	}
}
