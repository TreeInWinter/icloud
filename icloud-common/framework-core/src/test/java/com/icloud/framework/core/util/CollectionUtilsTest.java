package com.icloud.framework.core.util;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.junit.Test;
public class CollectionUtilsTest {

	@Test
	public void test() {
		Set<String> authorizedTicketOfficeNoSet = new HashSet<String>();
    	authorizedTicketOfficeNoSet.add("ab");
    	authorizedTicketOfficeNoSet.add("cd");
    	CollectionUtils.transform(authorizedTicketOfficeNoSet, new Transformer(){

			@Override
			public Object transform(Object input) {
				if(input != null)
					return ((String)input).toUpperCase();
				else return input;
			}
			
		});
    	System.out.println(authorizedTicketOfficeNoSet);
	}

}
