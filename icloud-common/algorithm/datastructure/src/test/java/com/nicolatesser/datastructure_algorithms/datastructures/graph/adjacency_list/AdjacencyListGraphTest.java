package com.nicolatesser.datastructure_algorithms.datastructures.graph.adjacency_list;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicolatesser.datastructure.graph.Edge;
import com.nicolatesser.datastructure.graph.Vertex;
import com.nicolatesser.datastructure.graph.adjacency_list.AdjacencyListGraph;

public class AdjacencyListGraphTest {

	private AdjacencyListGraph target;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		target = new AdjacencyListGraph();
	}

	@Test
	public void testAVertex() {
		//Prepare
		makeTestGraph();
		//Execute
		Vertex aVertex = target.aVertex();
		//Verify
		List<Vertex> vertices = target.vertices();
		Assert.assertEquals(vertices.get(0), aVertex);
	}

	@Test
	public void testAdjacentVertices() {
		//Prepare
		makeTestGraph();
		Vertex v1 = target.aVertex();
		//Execute
		List<Vertex> adjacentVertices = target.adjacentVertices(v1);
		
		//Verify
		List<Vertex> vertices = target.vertices();
		Assert.assertTrue(adjacentVertices.contains(vertices.get(1)));
		Assert.assertTrue(adjacentVertices.contains(vertices.get(2)));
		Assert.assertTrue(adjacentVertices.contains(vertices.get(3)));
		
	}
	
	@Test
	public void testAdjacentVertices2() {
		//Prepare
		makeTestGraph();
		List<Vertex> vertices = target.vertices();
		Vertex v4 = vertices.get(3);
		//Execute
		List<Vertex> adjacentVertices = target.adjacentVertices(v4);
		
		//Verify
		Assert.assertTrue(adjacentVertices.contains(vertices.get(0)));
		Assert.assertTrue(adjacentVertices.contains(vertices.get(1)));

		
	}
	

	@Test
	public void testAreAdjacent() {
		//Prepare
		makeTestGraph();
		List<Vertex> vertices = target.vertices();
		Vertex v2 = vertices.get(1);
		Vertex v3 = vertices.get(2);
		
		Vertex v4 = vertices.get(3);
		
		//Execute
		boolean are24Adjacent = target.areAdjacent(v2, v4);
		boolean are34Adjacent = target.areAdjacent(v3, v4);
		
		//Verify
		Assert.assertTrue(are24Adjacent);
		Assert.assertFalse(are34Adjacent);
		
	}

	@Test
	public void testDegree() {
		//Prepare
		makeTestGraph();
		Vertex v1 = target.aVertex();
		//Execute
		int degree = target.degree(v1);
		
		//Verify
		Assert.assertEquals(3, degree);
	}

	@Test
	public void testEdges() {
		//Prepare
		makeTestGraph();
		//Execute
		List<Edge> edges = target.edges();
		//Verify
		Assert.assertEquals(4, edges.size());
		
	}

	@Test
	public void testEndVertices() {
		//Prepare
		makeTestGraph();
		List<Edge> edges = target.edges();
		Edge e = edges.get(0);
		
		List<Vertex> vertices = target.vertices();
		//Execute
		Vertex[] endVertices = target.endVertices(e);
		
		//Verify
		Assert.assertEquals(vertices.get(0), endVertices[0]);
		Assert.assertEquals(vertices.get(1), endVertices[1]);
		

	}

	@Test
	public void testIncidendEdges() {
		//Prepare
		makeTestGraph();
		Vertex v1 = target.aVertex();
		//Execute
		List<Edge> incidendEdges = target.incidendEdges(v1);
		//Verify
		Assert.assertEquals(3, incidendEdges.size());
	}

	@Test
	public void testNumEdges() {
		//Prepare
		makeTestGraph();
		//Execute
		int numEdges = target.numEdges();
		//Verify
		Assert.assertEquals(4, numEdges);
	}

	@Test
	public void testNumVertices() {
		//Prepare
		makeTestGraph();
		//Execute
		int numVertices = target.numVertices();
		//Verify
		Assert.assertEquals(4, numVertices);
	}

	@Test
	public void testOpposite() {
		//Prepare
		makeTestGraph();
		Vertex v1 = target.aVertex();
		List<Edge> edges = v1.getEdges();
		Edge e = edges.get(0);
		List<Vertex> vertices = target.vertices();
		//Execute
		Vertex opposite = target.opposite(v1, e);
		//Verify
		Assert.assertEquals(vertices.get(1), opposite);
	}

	@Test
	public void testVertices() {
		//Prepare
		makeTestGraph();
		//Execute
		List<Vertex> vertices = target.vertices();
		//Verify
		Assert.assertEquals(4, vertices.size());
	}

	@Test
	public void testInsertEdge() {
		Integer o = 3;
		Integer o1=1;
		Integer o2=2;
		Vertex v = target.insertVertex(o1);
		Vertex w = target.insertVertex(o2);
		//Execute
		target.insertEdge(v, w, o);
		
		//Verify
		
		Assert.assertEquals(2, target.numVertices());
		Assert.assertEquals(1, target.numEdges());
		List<Edge> edges = target.edges();
		Assert.assertEquals(o, edges.get(0).getO());
		
	}

	@Test
	public void testInsertVertex() {
		
		//Prepare
		Integer o = 1;
		//Execute
		target.insertVertex(o);
		//Verify
		
		Assert.assertEquals(1, target.numVertices());
		List<Vertex> vertices = target.vertices();
		Assert.assertEquals(o, vertices.get(0).getO());		
	}
	
	
	public void makeTestGraph()
	{
		//Creating this graph
		// V = 1,2,3,4
		// E = [1,2],[1,3,],[1,4],[2,4]
		//Inserting vertices
		Vertex v1 = target.insertVertex(1);
		Vertex v2 = target.insertVertex(2);
		Vertex v3 = target.insertVertex(3);
		Vertex v4 = target.insertVertex(4);
		//edges
		target.insertEdge(v1, v2, null);
		target.insertEdge(v1, v3, null);
		target.insertEdge(v1, v4, null);
		target.insertEdge(v2, v4, null);
		
		
	}
	
}
