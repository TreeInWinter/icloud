package com.nicolatesser.datastructure.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

public class GraphAlgorithms {
	
	
	public static List<Vertex> breadthFirstSearch (Graph graph, Vertex a)
	{
		
		Queue<Vertex> queue = new LinkedList<Vertex>();
		queue.add(a);
		
		return GraphAlgorithms.breadthFirstSearch(graph, queue);
	}
	
	
	private static List<Vertex> breadthFirstSearch (Graph graph, Queue<Vertex> q)
	{
		List<Vertex> bfsVertices = new Vector<Vertex>();
		
		while (!q.isEmpty())
		{
			Vertex v = q.poll();
			v.setVisited(true);
			bfsVertices.add(v);
			
			List<Vertex> adjacentVertices = graph.adjacentVertices(v);
			for (Vertex adjacent : adjacentVertices)
			{
				if ((!adjacent.getVisited())&&(!q.contains(adjacent)))
				{
					q.add(adjacent);
				}
			}
		}
		return bfsVertices;
	}
	

	public static List<Vertex> depthFirstSearch (Graph graph, Vertex a)
	{
		List<Vertex> dfsVertices = new Vector<Vertex>();
		if (a.getVisited()) return dfsVertices;
		
		a.setVisited(true);
		dfsVertices.add(a);
		
		System.out.println("vertex is "+a.getO());
		
		List<Vertex> adjacentVertices = graph.adjacentVertices(a);
		for (Vertex adjacent : adjacentVertices)
		{
			System.out.println("adjacent vertex is "+adjacent.getO()+ "visited = "+adjacent.getVisited());
			if (!adjacent.getVisited())
			{
			   dfsVertices.addAll(depthFirstSearch(graph, adjacent));
			}
		}
		return dfsVertices;
	}
	
	
	public static  List<Vertex> dijkstra(Graph graph, Vertex a)
	{
		/*
		 * The algorihtm grow a section of the graph where the SSSP property is always valid.
		 * S = starting vertex a, cost to reach the vertex a is null
		 * at every iteration looking for the the vertex outside of the edge that minimize the global distance (distance to reach the previous node + cost of the edge)
		 */
		
		int n = graph.numVertices();
		
		List<Vertex> s = new Vector<Vertex>();
		a.setCost(0);
		a.setPred(null);
		a.setVisited(true);
		s.add(a);
		
		while (s.size()!= n)
		{
			int min = Integer.MAX_VALUE;
			Vertex newVisitedVertex = null;
			//for each element in s
			for (int i=0;i<s.size();i++)
			{
				Vertex v = s.get(i);
				System.out.println("selecting vertex "+v.getO()+" in S.");
				List<Edge> edges = graph.incidendEdges(v);
				for (Edge e : edges)
				{
					Vertex opposite = graph.opposite(v, e);
					if (!opposite.getVisited())
					{
						int cost = v.getCost()+ e.getO();
						if (cost<min)
						{
							newVisitedVertex = opposite;
							newVisitedVertex.setPred(v);
							min = cost;

						}
					}
				}
				
			}
			
			newVisitedVertex.setVisited(true);
			newVisitedVertex.setCost(min);

			s.add(newVisitedVertex);
			System.out.println("s size is "+s.size());
		}
		
		return s;
	}
	
	
	public static void floydWarschall(Graph graph, Integer[][] costs, Integer [][] pred)
	{
		//find all shortest paths among every couple of vertex
		//at every iteration consider 3 vertex, and finds the min path between i and j using just vertices till index h.
		
		//initialize, prec i, j is i
		
		List<Vertex> vertices = graph.vertices();
		int n = graph.numVertices();
		
		for (int i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
			{
				pred[i][j]=i;
			}
		}
		
		//initialize costs for i,j if i=j 0, if there is a edge, the cost of the edge otherwise infinite
		
		for (int i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
			{
				Vertex vi = vertices.get(i);
				Vertex vj = vertices.get(j);

				if (i==j) costs[i][j]=0;
				else if (graph.areAdjacent(vi, vj))
				{
					Edge edge = graph.getEdge(vi, vj);
					costs[i][j]=edge.getO();
				}
				else
				{
				//	costs[i][j]=Integer.MAX_VALUE;
					costs[i][j]=100;
				}
			}
		}
		
		for (int i=0;i<n;i++)
		{
			for (int j=0;j<n;j++)
			{
				System.out.println("costs "+(i+1)+","+(j+1)+" = "+costs[i][j]);
			}
		}
		
		
		//3 cycles
		
		for (int h=0;h<n;h++)
		{
			for (int i=0;i<n;i++)
			{
				for (int j=0;j<n;j++)
				{
					
					int cost = costs[i][h]+costs[h][j];
					if (cost<costs[i][j])
					{
						costs[i][j] = cost;
						pred[i][j] = pred [h][j];
					}
					
				}
			}
		}
	}
	
	
	
	// minimum spanning tree
	
	/*
	 * Template: T is empty
	 * until T size is not n-1
	 * Si,...Sj are the components connected by the tree (or the single vertices)
	 * look for a edge between 2 different component, and add it to the tree.
	 */
	
	
	public static List<Edge> primDijkstra(Graph graph)
	{
		//this algorithm consider mostly 2 components: S with the vertices connected by the tree (always 1 component), and all others.
		//at every step it select the mim vertex among one vertex of S and one of V/S.
		
		List<Edge> tree = new Vector<Edge>();
		int n = graph.numVertices();
		
		Vertex v1 = graph.aVertex();
		List<Vertex> s = new Vector<Vertex>();
		s.add(v1);
		
		while(tree.size()<(n-1))
		{
			int min = Integer.MAX_VALUE;
			Edge minEdge = null;
			Vertex vertexToAdd=null;
			for (Vertex v : s)
			{
				List<Edge> incidendEdges = graph.incidendEdges(v);
				for (Edge e : incidendEdges)
				{
					Vertex w = graph.opposite(v, e);
					if (!s.contains(w))
					{
						if (e.getO()<min)
						{
							min = e.getO();
							minEdge = e;
							vertexToAdd=w;
						}
					}
				}
				
			}
			
			tree.add(minEdge);
			s.add(vertexToAdd);
			
		}
		
		
		return tree;
	}
	
	
	public static List<Edge> kruskal(Graph graph)
	{
		//order the edges from the smallest to the biggest
		//keep track of connected components, at the beginning all components are unconnected
		//select always the smallest edge that does connect 2 different components
		
		int n = graph.numVertices();
		int m = graph.numEdges();
		List<Edge> edges = graph.edges();
		//order (stupid bubble sort)
		for (int i=0;i<m-1;i++)
		{
			for (int j=i+1;j<m;j++)
			{
				Edge ei = edges.get(i);
				Edge ej = edges.get(j);
				if (ej.getO()<ei.getO())
				{
					edges.set(i, ej);
					edges.set(j, ei);

				}
			}
		}
		
		for (int i=0;i<m;i++)
		{
			System.out.println("order test i="+i+", edge cost="+edges.get(i).getO());
		}
		
		
		List<Edge> tree = new Vector<Edge>();
		
		//initialize connected components
		int[]connectedComponents = new int[n];
		for (int i=0;i<n;i++)
		{
			connectedComponents[i]=i;
		}
		
		int edgesIndex = 0;
		
		while (tree.size()<(n-1))
		{
			
			Edge e =edges.get(edgesIndex);
			
			Integer i = e.getVertex1().getO()-1;
			Integer j = e.getVertex2().getO()-1;
			
			//found the edge
			if (connectedComponents[i]!=connectedComponents[j])
			{
				
				tree.add(e);
				
				//update the connected components
				for (int k=0;k<n;k++)
				{
					if (connectedComponents[k]==connectedComponents[j])
					{
						connectedComponents[k]=connectedComponents[i];
					}
				}
					
					
			}
			edgesIndex++;
			
		}
		
		
		
		return tree;
	}
	
	
	
	public static List<Vertex> aStar(Graph graph, Vertex start, Vertex goal, Integer[] h)
	{
		List<Vertex> vertices = graph.vertices();
		
		
		List<Vertex> openSet = new Vector<Vertex>();
		List<Vertex> closedSet = new Vector<Vertex>();
				
		
		start.setEstimation(h[(start.getO()-1)]);
		start.setCost(0);
		openSet.add(start);


		
		while (!openSet.isEmpty())
		{
			//select the goal in openset with min estimated score
			int min = Integer.MAX_VALUE;
			Vertex minVertex =  null;
			for (Vertex v : openSet)
			{
				if (v.getEstimation()<min)
				{
					min = v.getEstimation();
					minVertex = v;
				}
			}
			minVertex.setVisited(true);
			openSet.remove(minVertex);
			closedSet.add(minVertex);

			System.out.println("*** Selected vertex "+minVertex.getO());
			System.out.println("** vertex "+minVertex.getO()+ "has cost "+minVertex.getCost()+" and estimation "+minVertex.getEstimation());
				
			
			
			
			
			//calculate f (estimation)
			List<Edge> incidendEdges = graph.incidendEdges(minVertex);
			for (Edge e : incidendEdges)
			{
				
				Vertex opposite = graph.opposite(minVertex, e);
				
				
				if (closedSet.contains(opposite))
				{
					continue;
				}
				
				
				int distance = minVertex.getCost() + e.getO();
				boolean tentativeIsBetter;
				
				System.out.println("considering vertex "+opposite.getO()+", distance is "+distance);
				
				
				if (!openSet.contains(opposite))
				{
					openSet.add(opposite);
					System.out.println("vertex "+opposite.getO()+" is added to openset.");
					tentativeIsBetter = true;
				}
				else if (distance < opposite.getCost())
				{
					tentativeIsBetter=true;	
				}
				else
				{
					tentativeIsBetter=false;
				}
				
				if (tentativeIsBetter)
				{
					opposite.setCost(distance);
					opposite.setEstimation(distance + h[(opposite.getO()-1)]);
					System.out.println("(updated)vertex "+opposite.getO()+ "has cost "+opposite.getCost()+" and estimation "+opposite.getEstimation());
						

				}
			}
			
		}
		
		return vertices;
	}
	
	
}
