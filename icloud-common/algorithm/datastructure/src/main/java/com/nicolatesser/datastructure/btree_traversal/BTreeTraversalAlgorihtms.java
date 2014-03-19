package com.nicolatesser.datastructure.btree_traversal;

import java.util.List;

import com.nicolatesser.datastructure.btree.BPosition;
import com.nicolatesser.datastructure.btree.BTree;
import com.nicolatesser.datastructure.tree.Position;
import com.nicolatesser.datastructure.tree.Tree;


public class BTreeTraversalAlgorihtms {
	
	
	public static void preOrderTreeTraversal (BTree t)
	{
		System.out.println("preorder traversal");

		preOrderTreeTraversal(t, t.root());
		
		
	}
	
	private static void preOrderTreeTraversal (BTree t, BPosition p)
	{
		if (p==null)return;
		//visit t
		System.out.println(p.getObject());
		//base
		if (t.isExternal(p)) return;
		//recursion
		preOrderTreeTraversal(t,t.leftChild(p));
		preOrderTreeTraversal(t,t.rightChild(p));	
	}
	
	
	public static void postOrderTreeTraversal (BTree t)
	{
		System.out.println("postorder traversal");

		postOrderTreeTraversal(t, t.root());
		
		
	}
	
	private static void postOrderTreeTraversal (BTree t, BPosition p)
	{
		if (p==null)return;
		//base
		if (t.isExternal(p))
		{
			System.out.println(p.getObject());
			return;
		}
		//recursion
		postOrderTreeTraversal(t,t.leftChild(p));
		postOrderTreeTraversal(t,t.rightChild(p));
		//visit t
		System.out.println(p.getObject());
		
	}
	
	public static void inOrderTreeTraversal (BTree t)
	{
		System.out.println("postorder traversal");

		inOrderTreeTraversal(t, t.root());
		
		
	}
	
	private static void inOrderTreeTraversal (BTree t, BPosition p)
	{
		if (p==null)return;
		//base
		if (t.isExternal(p))
		{
			System.out.println(p.getObject());
			return;
		}
		//recursion left
		inOrderTreeTraversal(t,t.leftChild(p));
		//visit t
		System.out.println(p.getObject());
		//recursion right
		inOrderTreeTraversal(t,t.rightChild(p));

		
	}
	
	//this work just if the tree is ordered
	public static BPosition binarySearch(BTree t, Object key)
	{
		return binarySearch(t, t.root(), key);
	}
	
	//this work just if the tree is ordered
	private static BPosition binarySearch(BTree t, BPosition p, Object key)
	{
		if (p.getObject()==key) return p;
		else
		{
			int intKey = (Integer)key;
			int intO = (Integer)p.getObject();
			if (intKey<intO)
			{
				if (t.leftChild(p)!=null)
					return binarySearch(t, t.leftChild(p),key);
				return null;
			}
			else if (intKey>intO)
			{
				if (t.rightChild(p)!=null)
					return binarySearch(t, t.rightChild(p),key);
				return null;
			}
			
		}
		return null;
					
	}
	
	
	
	

}
