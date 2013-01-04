/**
 * 
 */
package com.vmware.gemfire.example.spatial.listener;

import java.util.Properties;

import com.gemstone.gemfire.cache.CacheListener;
import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.RegionEvent;
import com.vmware.gemfire.example.spatial.domain.*;
import com.vmware.gemfire.example.spatial.index.QuadTreeIndex;



/**
 * @author vFabric Field COE
 *
 */
public class QuadTreeCacheListener implements CacheListener<SpatialKey, MyData>, Declarable
{

	QuadTreeIndex tree = null;
	
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheCallback#close()
	 */
	@Override
	public void close()
	{
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterCreate(com.gemstone.gemfire.cache.EntryEvent)
	 * When a new entry is created in the Data region, put that key into the QuadTreeIndex for this cacheserver node
	 */
	@Override
	public void afterCreate(EntryEvent<SpatialKey, MyData> ev)
	{
		
		SpatialKey key = (SpatialKey)ev.getKey();
		tree.put(key.getLat(), key.getLon(), key);
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterDestroy(com.gemstone.gemfire.cache.EntryEvent)
	 * When the key is destroyed in the region, make sure it is taken out of the QuadTree as well, so no nodes try to 
	 * look it up.
	 */
	@Override
	public void afterDestroy(EntryEvent<SpatialKey, MyData> ev)
	{
		SpatialKey key = (SpatialKey)ev.getKey();
		tree.remove(key.getLat(), key.getLon(), key);
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterInvalidate(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	public void afterInvalidate(EntryEvent<SpatialKey, MyData> ev)
	{
		SpatialKey key = (SpatialKey)ev.getKey();
		tree.remove(key.getLat(), key.getLon(), key);
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionClear(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionClear(RegionEvent<SpatialKey, MyData> ev)
	{
		//not in use
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionCreate(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionCreate(RegionEvent<SpatialKey, MyData> ev)
	{
		// not in use
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionDestroy(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionDestroy(RegionEvent<SpatialKey, MyData> ev)
	{
		// not in use
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionInvalidate(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionInvalidate(RegionEvent<SpatialKey, MyData> ev)
	{
		// not in use
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterRegionLive(com.gemstone.gemfire.cache.RegionEvent)
	 */
	@Override
	public void afterRegionLive(RegionEvent<SpatialKey, MyData> ev)
	{
		//not in use
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.CacheListener#afterUpdate(com.gemstone.gemfire.cache.EntryEvent)
	 */
	@Override
	public void afterUpdate(EntryEvent<SpatialKey, MyData> ev)
	{
		//not in use - no update needed when the object is updated.  They key is still in the tree
		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.Declarable#init(java.util.Properties)
	 */
	@Override
	public void init(Properties arg0)
	{
		tree = QuadTreeIndex.getSingleton();
		
	}

}
