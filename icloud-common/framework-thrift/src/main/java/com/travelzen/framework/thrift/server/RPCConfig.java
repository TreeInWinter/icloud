package com.travelzen.framework.thrift.server;

/*
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

import java.util.List;

public class RPCConfig {
	public Long rpc_timeout_in_ms = new Long(2000);

	public String listen_address;

	public String rpc_address;
	public Integer rpc_port = 9160;
	public Boolean rpc_keepalive = true;
	public Integer rpc_send_buff_size_in_bytes;
	public Integer rpc_recv_buff_size_in_bytes;

	public Integer thrift_max_message_length_in_mb = 16;
	public Integer thrift_framed_transport_size_in_mb = 16;
	
	
	
	public String server_name;
	public int server_wokrer_threads_min;
	public int server_wokrer_threads_max;
	public int server_selector_threads;
	public int zookeeper_connect_timeout_ms;
	public int zookeeper_session_timeout_ms;
	public String zookeeper_path;
	public int getServer_wokrer_threads_min() {
		return server_wokrer_threads_min;
	}
	public void setServer_wokrer_threads_min(int server_wokrer_threads_min) {
		this.server_wokrer_threads_min = server_wokrer_threads_min;
	}
	public int getServer_wokrer_threads_max() {
		return server_wokrer_threads_max;
	}
	public void setServer_wokrer_threads_max(int server_wokrer_threads_max) {
		this.server_wokrer_threads_max = server_wokrer_threads_max;
	}
	public int getServer_selector_threads() {
		return server_selector_threads;
	}
	public void setServer_selector_threads(int server_selector_threads) {
		this.server_selector_threads = server_selector_threads;
	}
	public int getZookeeper_connect_timeout_ms() {
		return zookeeper_connect_timeout_ms;
	}
	public void setZookeeper_connect_timeout_ms(int zookeeper_connect_timeout_ms) {
		this.zookeeper_connect_timeout_ms = zookeeper_connect_timeout_ms;
	}
	public int getZookeeper_session_timeout_ms() {
		return zookeeper_session_timeout_ms;
	}
	public void setZookeeper_session_timeout_ms(int zookeeper_session_timeout_ms) {
		this.zookeeper_session_timeout_ms = zookeeper_session_timeout_ms;
	}
	public String getZookeeper_path() {
		return zookeeper_path;
	}
	public void setZookeeper_path(String zookeeper_path) {
		this.zookeeper_path = zookeeper_path;
	}
	
	
	
	


}
