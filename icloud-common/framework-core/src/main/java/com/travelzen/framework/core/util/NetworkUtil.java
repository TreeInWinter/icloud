package com.travelzen.framework.core.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtil {
	/**
	 * 获取本地ip，获取不到返回null
	 * @return
	 */
	public static String getLocalIp(){
       String ip = null;
       try {
           Enumeration<NetworkInterface> e1 = NetworkInterface.getNetworkInterfaces();
          L1: while (e1.hasMoreElements()) {
               NetworkInterface ni = e1.nextElement();
               if (!ni.getName().startsWith("eth") && !ni.getName().startsWith("bond") && !ni.getName().startsWith("em")) {
                   continue L1;
               } else {
                   Enumeration<InetAddress> e2 = ni.getInetAddresses();
                   L2: while (e2.hasMoreElements()) {
                       InetAddress ia = e2.nextElement();
                       if (!(ia  instanceof Inet4Address))
                           continue L2;
                       ip = ia.getHostAddress();
                       ip = StringUtil.trim(ip);
                       if(!StringUtil.isEmpty(ip) && (ip.startsWith("192.") || ip.startsWith("10.")))
                    	   break L1;
                   }
               }
           }
       } catch (Throwable thr) {
           return null;
       }
       return ip;
	}
}
