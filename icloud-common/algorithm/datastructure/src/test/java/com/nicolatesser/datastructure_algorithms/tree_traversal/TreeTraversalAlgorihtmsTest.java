package com.nicolatesser.datastructure_algorithms.tree_traversal;

import java.util.Vector;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicolatesser.datastructure.tree.Tree;
import com.nicolatesser.datastructure.tree.linked_structure.LinkedStructureTree;
import com.nicolatesser.datastructure.tree.linked_structure.Node;
import com.nicolatesser.datastructure.tree_traversal.TreeTraversalAlgorihtms;

public class TreeTraversalAlgorihtmsTest {

	private Node rootNode;
	private Node children1;
	private Node children2;
	private Node children3;
	private Node children21;
	private Node children22;
	
	
	private Tree tree;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tree = new LinkedStructureTree();
	}

	@Test
	public void testPreOrderTreeTraversal() {
		//Prepare
		makeTestTree();
		//Execute
		TreeTraversalAlgorihtms.preOrderTreeTraversal(tree);
		
		
	}

	@Test
	public void testPostOrderTreeTraversal() {
		makeTestTree();
		//Execute
		TreeTraversalAlgorihtms.postOrderTreeTraversal(tree);
	}
	
	public void makeTestTree()
	{

		rootNode = new Node();
		rootNode.setObject("root");
		rootNode.setParent(null);
		
		tree.setRoot(rootNode);
		
		children1 = new Node();
		children1.setParent(rootNode);
		children1.setObject("children1");
		
		children2 = new Node();
		children2.setParent(rootNode);
		children2.setObject("children2");
		
		children3 = new Node();
		children3.setParent(rootNode);
		children3.setObject("children3");
		
		Vector<Node> rootChildren = new Vector<Node>();
		rootChildren.add(children1);
		rootChildren.add(children2);
		rootChildren.add(children3);
		
		rootNode.setChildren(rootChildren);

		
		children21 = new Node();
		children21.setParent(children2);
		children21.setObject("children21");
		
		children22 = new Node();
		children22.setParent(children2);
		children22.setObject("children22");
		
		Vector<Node> children2Children = new Vector<Node>();
		children2Children.add(children21);
		children2Children.add(children22);
		
		children2.setChildren(children2Children);
		
		
	}

}
