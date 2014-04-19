package com.nicolatesser.datastructure.graph.adjacency_list;

import java.util.List;
import java.util.Vector;

import com.nicolatesser.datastructure.graph.Edge;
import com.nicolatesser.datastructure.graph.Graph;
import com.nicolatesser.datastructure.graph.Vertex;

public class AdjacencyListGraph implements Graph {

	private List<Vertex> vertices;
	private List<Edge> edges;
	
	
	public AdjacencyListGraph()
	{
		vertices = new Vector<Vertex>();
		edges = new Vector<Edge>();
	}
	
	
	
	@Override
	public Vertex aVertex() {
		
		if (vertices.size()>1)
			return vertices.get(0);
		else
			return null;
	}

	@Override
	public List<Vertex> adjacentVertices(Vertex v) {
		
		int indexOf = this.vertices.indexOf(v);
		Vertex vertex = this.vertices.get(indexOf);
		List<Edge> edges = vertex.getEdges();
	
		List<Vertex> adjacentVertices = new Vector<Vertex>();
		for (Edge e : edges)
		{
			Vertex opposite = e.getOpposite(v);
			adjacentVertices.add(opposite);
		}
		return adjacentVertices;
	}

	@Override
	public boolean areAdjacent(Vertex v, Vertex w) {

		List<Vertex> adjacentVertices = adjacentVertices(v);
		for (Vertex x : adjacentVertices)
		{
			if (x==w) return true;
		}
		
		return false;
	}
	
	@Override
	public Edge getEdge(Vertex v, Vertex w) {
		List<Edge> incidendEdges = this.incidendEdges(v);
		for (Edge e : incidendEdges)
		{
			if (this.opposite(v, e)==w) return e;
		}
		return null;
	}

	@Override
	public int degree(Vertex v) {

		return v.getEdges().size();
	}

	@Override
	public List<Edge> edges() {
		return this.edges;
	}

	@Override
	public Vertex[] endVertices(Edge e) {
		Vertex[] endVertices = new Vertex[2];
		endVertices[0] = e.getVertex1();
		endVertices[1] = e.getVertex2();
		return endVertices;
	}

	@Override
	public List<Edge> incidendEdges(Vertex v) {
		int indexOf = this.vertices.indexOf(v);
		Vertex vertex = this.vertices.get(indexOf);
		
		return vertex.getEdges();
	}

	@Override
	public int numEdges() {
		return this.edges.size();
	}

	@Override
	public int numVertices() {
		return this.vertices.size();
	}

	@Override
	public Vertex opposite(Vertex v, Edge e) {
		int indexOf = this.edges.indexOf(e);
		Edge edge = this.edges.get(indexOf);
		return edge.getOpposite(v);
	}

	@Override
	public List<Vertex> vertices() {
		return this.vertices;
	}



	@Override
	public Edge insertEdge(Vertex v, Vertex w, Integer o) {
		Edge e = new Edge(v, w, o);
		this.edges.add(e);
		v.addEdge(e);
		w.addEdge(e);
		return e;
	}



	@Override
	public Vertex insertVertex(Integer o) {
		Vertex v = new Vertex(o);
		this.vertices.add(v);
		return v;
		
	}


	

}
