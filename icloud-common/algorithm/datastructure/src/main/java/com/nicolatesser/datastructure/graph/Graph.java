// $Id:$

package com.nicolatesser.datastructure.graph;

import java.util.List;

/**
 * @author ntesser
 * 
 */
public interface Graph
{
	
	public int numVertices();
	
	public int numEdges();
	
	public List<Vertex> vertices();
	
	public List<Edge> edges();
	
	public Vertex aVertex();
	
	public int degree(Vertex v);
	
	public List<Vertex> adjacentVertices(Vertex v);
	
	public List<Edge> incidendEdges(Vertex v);
	
	public Vertex[]endVertices (Edge e);
	
	public Vertex opposite (Vertex v, Edge e);
	
	public boolean areAdjacent (Vertex v, Vertex w);
	
	public Edge getEdge (Vertex v, Vertex w);
	
	
	public Edge insertEdge (Vertex v, Vertex w, Integer o);
	
	public Vertex insertVertex(Integer o);
	
//	public void removeVertex(Vertex v);
	
//	public void removeEdge (Edge e);
	

}
