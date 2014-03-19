package com.travelzen.tops.thrift.balancing;

import java.util.ArrayList;
import java.util.List;

import service.demo.Hello;
import service.demo.Hello.Iface;

public class HelloClient {
	static HelloClientLoadBalancingChannel loadBalancingChannel = null;

	public static void main(String args[]) {
		// Client<Hello.Iface> tc = new WjThriftClient<Hello.Iface,
		// Hello.Client>("localhost", 9890, true, Hello.Client.class);
		// Client<Hello.Iface> tc1 = new WjThriftClient<Hello.Iface,
		// Hello.Client>("localhost", 9891, true, Hello.Client.class);
		// List<Client<Hello.Iface>> clients = new
		// ArrayList<Client<Hello.Iface>>();
		// clients.add(tc1);
		String tc = "localhost:9893";
//		String tc1 = "localhost:9891";
		List<String> list = new ArrayList<String>();
//		list.add(tc1);
		list.add(tc);
		loadBalancingChannel = new HelloClientLoadBalancingChannel(list, "");
		try {
			client();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void client() throws InterruptedException {
		// TODO Auto-generated method stub
		// Thread.sleep(1000);
		long time = System.currentTimeMillis();
		for (int i = 0; i < 1; i++) {
			// System.out.println("i " + i);
			Hello.Iface client = null;
			try {
				client = (Iface) loadBalancingChannel.proxy();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				// e1.printStackTrace();
			}
			if (client != null) {
				try {
					String world = client.helloString("nihao");
					System.out.println(world);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// Thread.sleep(1000);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("time is " + (endTime - time) / 1000.0f + "s");
	}
}
