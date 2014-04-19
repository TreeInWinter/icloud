package com.nicolatesser.datastructure.btree;


public interface BTree {
	
	public BPosition root();
	
	public void setRoot(BPosition p);
		
	public boolean isInternal(BPosition p);
	
	public boolean isExternal(BPosition p);
	
	public boolean isRoot(BPosition p);
	
	public BPosition leftChild (BPosition p);
	
	public BPosition rightChild (BPosition p);
	
	public BPosition parent (BPosition p);
		
	
}
