package com.nicolatesser.datastructure.btree.linked_structure;

import java.util.List;

import com.nicolatesser.datastructure.btree.BPosition;
import com.nicolatesser.datastructure.tree.Position;

public class BNode implements BPosition {

	private BNode parent;
	private BNode leftChild;
	private BNode rightChild;
	private Object object;
	
	public BNode getParent() {
		return parent;
	}
	public void setParent(BNode parent) {
		this.parent = parent;
	}
	public BNode getLeftChild() {
		return leftChild;
	}
	public void setLeftChild(BNode leftChild) {
		this.leftChild = leftChild;
	}
	public BNode getRightChild() {
		return rightChild;
	}
	public void setRightChild(BNode rightChild) {
		this.rightChild = rightChild;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	
	
	
}
