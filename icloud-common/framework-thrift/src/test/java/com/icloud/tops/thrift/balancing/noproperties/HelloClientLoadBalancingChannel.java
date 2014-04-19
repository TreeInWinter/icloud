package com.icloud.tops.thrift.balancing.noproperties;

import java.util.List;

import service.demo.Hello;
import service.demo.Hello.Iface;

import com.icloud.framework.thrift.balancing.Client;
import com.icloud.framework.thrift.balancing.LoadBalancingChannelNoProperty;
import com.icloud.framework.thrift.balancing.WjThriftClient;

public class HelloClientLoadBalancingChannel extends LoadBalancingChannelNoProperty<Hello.Iface> {

	public HelloClientLoadBalancingChannel(List<String> list, String prefix, String servcieName) {
		super(list, prefix, servcieName);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Client<Iface> crateThriftCient(String ip, int port) {
		// TODO Auto-generated method stub
		Client<Hello.Iface> tc = new WjThriftClient<Hello.Iface, Hello.Client>(ip, port, true, Hello.Client.class);
		return tc;
	}
}
