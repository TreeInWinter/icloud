package com.nicolatesser.datastructure.trees;

import java.util.List;

public interface Tree {
	
	public Position root();
	
	public int size();
	
	public boolean isInternal(Position p);
	
	public boolean isExternal(Position p);
	
	public boolean isRoot(Position p);
	
	public List<Position> children (Position p);
	
	public Position parent (Position p);
	
	public void setChildren (List<Position> children);
	
	
}
