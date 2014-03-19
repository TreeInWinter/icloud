package com.nicolatesser.datastructure.graph;

import java.util.List;
import java.util.Vector;

public class Vertex {
	
	private List<Edge> edges;
	
	private Integer o;
	
	private Boolean visited;
	
	private Vertex pred;
	
	private Integer cost;
	
	private Integer estimation;
	
	
	
	public Vertex(int o)
	{
		this.o=o;
		this.edges = new Vector<Edge>();
		this.visited=false;
	}
	
	public void addEdge(Edge e)
	{
		edges.add(e);
	}
	
	public List<Edge> getEdges()
	{
		return this.edges;
	}

	public Integer getO() {
		return o;
	}

	public void setO(Integer o) {
		this.o = o;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public Boolean getVisited() {
		return visited;
	}

	public void setVisited(Boolean visited) {
		this.visited = visited;
	}

	public Vertex getPred() {
		return pred;
	}

	public void setPred(Vertex pred) {
		this.pred = pred;
	}

	public Integer getCost() {
		return cost;
	}

	public void setCost(Integer cost) {
		this.cost = cost;
	}

	public Integer getEstimation() {
		return estimation;
	}

	public void setEstimation(Integer estimation) {
		this.estimation = estimation;
	}
	
	
	
	
	

}
