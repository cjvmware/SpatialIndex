package com.vmware.gemfire.example.spatial.index;

import java.util.Vector;
import com.bbn.openmap.util.quadtree.QuadTree;
import com.vmware.gemfire.example.spatial.domain.SpatialKey;


public class QuadTreeIndex
{
	private static QuadTreeIndex myTree = new QuadTreeIndex();
	private QuadTree tree;
	
	private QuadTreeIndex ()
	{
		tree = new QuadTree();
	}
	
	public static QuadTreeIndex getSingleton()
	{
		if (myTree == null)
		{
			myTree = new QuadTreeIndex();
		}
		return myTree;
	}
	
	public void put ( float lat, float lon, Object obj)
	{
		tree.put(lat, lon, obj);
	}
	
	public SpatialKey get (float lat, float lon)
	{
		return (SpatialKey)tree.get(lat, lon);
	}
	
	public Vector get (float n, float w, float s, float e)
	{
		return tree.get(n, w, s, e);
	}
	
	public SpatialKey remove (float lat, float lon, Object obj)
	{
		return (SpatialKey)tree.remove(lat, lon, obj);
	}
}
