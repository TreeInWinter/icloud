package com.nicolatesser.datastructure.tree.linked_structure;

import java.util.List;

import com.nicolatesser.datastructure.tree.Position;

public class Node implements Position {

	private Node parent;
	private List<Node> children;
	private Object object;
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	
	
	
	
}
