package com.nicolatesser.datastructure.tree.linked_structure;

import java.util.List;

import com.nicolatesser.datastructure.tree.Position;
import com.nicolatesser.datastructure.tree.Tree;

public class LinkedStructureTree implements Tree {
	
	private Node root;
	
	public LinkedStructureTree()
	{
		this.root=null;
	}
	

	@Override
	public List<? extends Position> children(Position p) {
		Node node = (Node)p;
		return node.getChildren();
		
	}

	@Override
	public boolean isExternal(Position p) {
		Node node = (Node)p;

		return node.getChildren()==null;
	}

	@Override
	public boolean isInternal(Position p) {
		Node node = (Node)p;

		return node.getChildren()!=null;
	}

	@Override
	public boolean isRoot(Position p) {
		Node node = (Node)p;
		return node == this.root;
	}

	@Override
	public Position parent(Position p) {
		Node node = (Node)p;
		return node.getParent();
	}

	@Override
	public Position root() {
		return this.root;
	}
	
	@Override
	public void setRoot(Position p)
	{
		this.root=(Node)p;
	}

}
