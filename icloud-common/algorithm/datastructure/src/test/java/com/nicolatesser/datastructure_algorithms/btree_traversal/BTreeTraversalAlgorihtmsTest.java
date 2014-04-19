package com.nicolatesser.datastructure_algorithms.btree_traversal;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.nicolatesser.datastructure.btree.BPosition;
import com.nicolatesser.datastructure.btree.BTree;
import com.nicolatesser.datastructure.btree.linked_structure.BNode;
import com.nicolatesser.datastructure.btree.linked_structure.LinkedStructureBTree;
import com.nicolatesser.datastructure.btree_traversal.BTreeTraversalAlgorihtms;

public class BTreeTraversalAlgorihtmsTest {
	
	private BNode rootNode;
	private BNode node2;
	private BNode node3;
	private BNode node6;
	private BNode node9;
	private BNode node12;
	private BNode node25;
	
	
	
	private BTree tree;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.tree = new LinkedStructureBTree();
	}

	@Test
	public void testPreOrderTreeTraversal() {
		//Prepare
		makeBTestTree();
		//Execute
		BTreeTraversalAlgorihtms.preOrderTreeTraversal(tree);
	}

	@Test
	public void testPostOrderTreeTraversal() {
		//Prepare
		makeBTestTree();
		//Execute
		BTreeTraversalAlgorihtms.postOrderTreeTraversal(tree);	}

	@Test
	public void testInOrderTreeTraversal() {
		//Prepare
		makeBTestTree();
		//Execute
		BTreeTraversalAlgorihtms.inOrderTreeTraversal(tree);	
	}

	
	@Test
	public void testBinarySearch_Found() {
		//Prepare
		makeBTestTree();
		//Execute
		BPosition position = BTreeTraversalAlgorihtms.binarySearch(tree, 9);	
		//Verify
		Assert.assertEquals(9, position.getObject());
	}
	

	@Test
	public void testBinarySearch_NotFound() {
		//Prepare
		makeBTestTree();
		//Execute
		BPosition position = BTreeTraversalAlgorihtms.binarySearch(tree, 10);	
		//Verify
		Assert.assertNull(position);
	}
	
	
	/**
	 *        7
	 *      3   12
	 *    2  6 9  25
 	 *    
	 */
	
	public void makeBTestTree()
	{

		rootNode = new BNode();
		rootNode.setObject(7);
		rootNode.setParent(null);
		tree.setRoot(rootNode);
		
		node3 = new BNode();
		node3.setObject(3);
		node3.setParent(rootNode);

		node12 = new BNode();
		node12.setObject(12);
		node12.setParent(rootNode);

		
		node2 = new BNode();
		node2.setObject(2);
		node2.setParent(node3);
		
		node6 = new BNode();
		node6.setObject(6);
		node6.setParent(node3);
		
		node9 = new BNode();
		node9.setObject(9);
		node9.setParent(node12);
		
		node25 = new BNode();
		node25.setObject(25);
		node25.setParent(node12);
		
		rootNode.setLeftChild(node3);
		rootNode.setRightChild(node12);
		
		node3.setLeftChild(node2);
		node3.setRightChild(node6);
		
		node12.setLeftChild(node9);
		node12.setRightChild(node25);
	
	}

}
