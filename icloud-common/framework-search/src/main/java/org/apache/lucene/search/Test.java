/**
 * Test.java
 *
 * Copyright 2007 easou, Inc. All Rights Reserved.
 *
 * created by jay 2009-6-25
 */
package org.apache.lucene.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class Test {
    
    private static final int NUM=199999;
    
    public static void main(String[] args) {
        List alist=new ArrayList();
        List llist=new LinkedList();
        Vector v=new Vector();
        HashMap hm=new HashMap();
        
        
//        long start=System.currentTimeMillis();
//        long mem=Runtime.getRuntime().freeMemory();
//        for(int i=0;i<NUM;i++) {
//            alist.add(new String(i+""));
//        }
//        for (Iterator iter = alist.iterator(); iter.hasNext();) {
//            String element = (String) iter.next();
//            
//        }
//        System.out.println(mem-Runtime.getRuntime().freeMemory());
//        System.out.println(System.currentTimeMillis()-start);
        
        long start=System.currentTimeMillis();
        long mem=Runtime.getRuntime().freeMemory();
        for(int i=0;i<NUM;i++) {
            hm.put(new String(i+""),"");
        }
        for (Iterator iter = hm.keySet().iterator(); iter.hasNext();) {
            String element = (String) iter.next();
        }
        System.out.println(mem-Runtime.getRuntime().freeMemory());
        System.out.println(System.currentTimeMillis()-start);
        
//        long start=System.currentTimeMillis();
//        long mem=Runtime.getRuntime().freeMemory();
//        for(int i=0;i<NUM;i++) {
//            llist.add(new String(i+""));
//        }
//        for (Iterator iter = llist.iterator(); iter.hasNext();) {
//            String element = (String) iter.next();
//        }
//        System.out.println(mem-Runtime.getRuntime().freeMemory());
//        System.out.println(System.currentTimeMillis()-start);
        
//        long start=System.currentTimeMillis();
//        long mem=Runtime.getRuntime().freeMemory();
//        for(int i=0;i<NUM;i++) {
//            v.add(new String(i+""));
//        }
//        for (Iterator iter = v.iterator(); iter.hasNext();) {
//            String element = (String) iter.next();
//        }
//        System.out.println(mem-Runtime.getRuntime().freeMemory());
//        System.out.println(System.currentTimeMillis()-start);
    }
}
