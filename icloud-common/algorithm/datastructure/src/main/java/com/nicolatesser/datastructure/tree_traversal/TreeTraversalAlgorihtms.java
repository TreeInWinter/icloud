package com.nicolatesser.datastructure.tree_traversal;

import java.util.List;

import com.nicolatesser.datastructure.tree.Position;
import com.nicolatesser.datastructure.tree.Tree;


public class TreeTraversalAlgorihtms {
	
	
	public static void preOrderTreeTraversal (Tree t)
	{
		System.out.println("preorder traversal");

		preOrderTreeTraversal(t, t.root());
		
		
	}
	
	private static void preOrderTreeTraversal (Tree t, Position p)
	{
		//visit t
		System.out.println(p.getObject());
		//base
		if (t.isExternal(p)) return;
		//recursion
		List<? extends Position> children = t.children(p);
		for (Position child : children)
		{
			preOrderTreeTraversal(t,child);
		}	
	}
	
	
	public static void postOrderTreeTraversal (Tree t)
	{
		System.out.println("postorder traversal");

		postOrderTreeTraversal(t, t.root());
		
		
	}
	
	private static void postOrderTreeTraversal (Tree t, Position p)
	{
		//base
		if (t.isExternal(p))
		{
			System.out.println(p.getObject());
			return;
		}
		//recursion
		List<? extends Position> children = t.children(p);
		for (Position child : children)
		{
			postOrderTreeTraversal(t,child);
		}	
		//visit t
		System.out.println(p.getObject());
		
	}
	
	

}
