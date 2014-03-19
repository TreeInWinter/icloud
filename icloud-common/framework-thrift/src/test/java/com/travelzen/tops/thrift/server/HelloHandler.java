package com.travelzen.tops.thrift.server;

import org.apache.thrift.TException;

import service.demo.*;

public class HelloHandler implements Hello.Iface {
	static int count = 0;

	@Override
	public String helloString(String word) throws TException {
		// TODO Auto-generated method stub
		System.out.println("hello, server : " + word + "  " + count);
		count++;
		return "hello, server : " + word;
	}

}
