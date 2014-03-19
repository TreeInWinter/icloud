package com.nicolatesser.datastructure.tree;

import java.util.List;

public interface Tree {
	
	public Position root();
	
	public void setRoot(Position p);
		
	public boolean isInternal(Position p);
	
	public boolean isExternal(Position p);
	
	public boolean isRoot(Position p);
	
	public List<? extends Position> children (Position p);
	
	public Position parent (Position p);
		
	
}
