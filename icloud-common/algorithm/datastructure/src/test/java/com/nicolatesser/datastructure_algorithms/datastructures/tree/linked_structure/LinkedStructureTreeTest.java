package com.nicolatesser.datastructure_algorithms.datastructures.tree.linked_structure;

import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicolatesser.datastructure.tree.Position;
import com.nicolatesser.datastructure.tree.linked_structure.LinkedStructureTree;
import com.nicolatesser.datastructure.tree.linked_structure.Node;

public class LinkedStructureTreeTest {

	private LinkedStructureTree target;
	
	private Node rootNode;
	private Node children1;
	private Node children2;
	private Node children3;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		target = new LinkedStructureTree();
	}


	@Test
	public void testChildren() {
		//Prepare
		makeTestTree();
		//Execute
		List<? extends Position> children = target.children(rootNode);
		
		//Verify
		Assert.assertEquals(3, children.size());
		
	}

	@Test
	public void testIsExternal_Root() {
		
		//Prepare
		makeTestTree();
		//Execute
		Boolean isExternal = target.isExternal(rootNode);
		
		//Verify
		Assert.assertFalse(isExternal);
		
	}
	
	@Test
	public void testIsExternal_Children() {
		
		//Prepare
		makeTestTree();
		//Execute
		Boolean isExternal = target.isExternal(children3);
		
		//Verify
		Assert.assertTrue(isExternal);
		
	}

	@Test
	public void testIsInternal_Children() {
		//Prepare
		makeTestTree();
		//Execute
		Boolean isInternal = target.isInternal(children3);
		
		//Verify
		Assert.assertFalse(isInternal);
	}
	
	@Test
	public void testIsInternal_Root() {
		//Prepare
		makeTestTree();
		//Execute
		Boolean isInternal = target.isInternal(rootNode);
		
		//Verify
		Assert.assertTrue(isInternal);
	}

	@Test
	public void testIsRoot_Root() {
		//Prepare
		makeTestTree();
		//Execute
		Boolean isRoot = target.isRoot(rootNode);
		
		//Verify
		Assert.assertTrue(isRoot);	
	}
	
	@Test
	public void testIsRoot_Children() {
		//Prepare
		makeTestTree();
		//Execute
		Boolean isRoot = target.isRoot(children2);
		
		//Verify
		Assert.assertFalse(isRoot);	
	}

	@Test
	public void testParent_Root() {
		//Prepare
		makeTestTree();
		//Execute
		Position parent = target.parent(rootNode);
		
		//Verify
		Assert.assertNull(parent);	
	}

	@Test
	public void testParent_Children() {
		//Prepare
		makeTestTree();
		//Execute
		Position parent = target.parent(children1);
		
		//Verify
		Assert.assertEquals(rootNode, parent);	
	}
	
	@Test
	public void testRoot() {
		makeTestTree();
		//Execute
		Position root = target.root();
		//Verify
		Assert.assertEquals(rootNode.getObject(), ((Node)root).getObject());
		Assert.assertEquals(rootNode, (Node)root);
	}
	
	
	public void makeTestTree()
	{

		rootNode = new Node();
		rootNode.setObject("root");
		rootNode.setParent(null);
		
		target.setRoot(rootNode);
		
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
		
	}

}
