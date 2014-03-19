package com.nicolatesser.datastructure.graph;

public class Edge {

	private Vertex vertex1;
	
	private Vertex vertex2;
	
	Integer o;
	
	public Edge (Vertex vertex1, Vertex vertex2, Integer o)
	{
		this.vertex1=vertex1;
		this.vertex2=vertex2;
		this.o = o;
	}
	
	public Vertex getOpposite(Vertex v)
	{
		if (v==vertex1)
			return vertex2;
		else if (v==vertex2)
		{
			return vertex1;
		}
		else
			return null;
	}

	public Vertex getVertex1() {
		return vertex1;
	}

	public void setVertex1(Vertex vertex1) {
		this.vertex1 = vertex1;
	}

	public Vertex getVertex2() {
		return vertex2;
	}

	public void setVertex2(Vertex vertex2) {
		this.vertex2 = vertex2;
	}

	public Integer getO() {
		return o;
	}

	public void setO(Integer o) {
		this.o = o;
	}
	
	
	
	
}
