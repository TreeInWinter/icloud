package com.travelzen.framework.core.util;

import java.util.Vector;

import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GraphAdapterBuilderTest {
	class ClassA{
	    public int field;
	    public ClassB parent;
	    public ClassA(int f, ClassB p){
	        field = f;
	        parent = p;
	    }
	}

	class ClassB{
	    public Vector<ClassA> vector = new Vector<ClassA>();
	}

	@Test
	public void test() {
		ClassB b = new ClassB();        

		ClassA a1 = new ClassA(1,b);
		ClassA a2 = new ClassA(2,b);
		ClassA a3 = new ClassA(3,b);

		b.vector.add(a1);
		b.vector.add(a2);
		b.vector.add(a3);

		//Serializing object b

		GsonBuilder gsonBuilder = new GsonBuilder();

		new GraphAdapterBuilder()
		    .addType(ClassA.class)
		    .addType(ClassB.class)
		    .registerOn(gsonBuilder);

		Gson gson = gsonBuilder.create();

		String json = gson.toJson(b);
		System.out.println(json);
	}

}
