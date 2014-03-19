package com.travelzen.framework.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;

public class TZCuratorFrameworkTest {
	private static Logger logger = LoggerFactory.getLogger(TZCuratorFrameworkTest.class);
	@Test
	public void test() throws Exception{
		
		TZCuratorFramework.addConnectionStateListener(
				new ConnectionStateListener(){

					@Override
					public void stateChanged(CuratorFramework client,
							ConnectionState newState) {
						if(newState == ConnectionState.CONNECTED){
							logger.info("connected");
						}
						if(newState == ConnectionState.LOST){
							logger.info("lost");
						}
						if(newState == ConnectionState.RECONNECTED){
							logger.info("reconnected");
						}
						if(newState == ConnectionState.SUSPENDED){
							logger.info("suspended");
						}
						
					}
					
				}
		);
		Thread.currentThread().join();
	}
	@Test
	public void test_getRpcAdress() throws InterruptedException{
		for(int i = 0;i < 1; i++){
			new Thread(){
				
				public void run(){
					while(true){
						logger.error(TZCuratorFramework.getRpcAdress("hotel").toString());
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}.start();
		}
		Thread.currentThread().join();
	}
	
	@Test
	public void test_registerRpc() throws Exception{
		TZCuratorFramework.registerRpc("localhost:1005");
		Thread.currentThread().join();
	}
	
}
