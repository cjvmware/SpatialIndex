package com.vmware.gemfire.example.spatial.listener;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.partition.PartitionListener;
import com.gemstone.gemfire.internal.cache.BucketRegion;
import com.gemstone.gemfire.internal.cache.PartitionedRegion;
import com.gemstone.gemfire.internal.cache.partitioned.Bucket;
import com.vmware.gemfire.example.spatial.domain.SpatialKey;
import com.vmware.gemfire.example.spatial.index.QuadTreeIndex;


/**
 * @author vFabric Field COE
 * As HA comes in to play, we cannot always count on a specific create or remove being called on a data element.
 * There is the possibility that a bucket could be promoted from a redundant to primary, or moved from a node.
 * In those cases, we need to keep the local QuadTree Index up to date.  This listener achieves that.
 *
 */
public class QuadTreePartitionListener implements PartitionListener, Declarable
{

	private static PartitionedRegion region;
	private static QuadTreeIndex tree;
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterBucketCreated(int, java.lang.Iterable)
	 * Implement this method to create the QuadTree index when a bucket is being reloaded from disk after a 
	 * graceful shutdown.
	 */
	@Override
	public void afterBucketCreated(int bucketId, Iterable<?> keys)
	{
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterBucketRemoved(int, java.lang.Iterable)
	 * When the bucket is removed from a node, remove the keys in that bucket from the local QuadTree Index
	 */
	@Override
	public void afterBucketRemoved(int bucketId, Iterable<?> keys)
	{
		tree = QuadTreeIndex.getSingleton();
		Iterator <SpatialKey >it = (Iterator <SpatialKey>)keys.iterator(); 
		while (it.hasNext())
		{
			SpatialKey key = it.next();
			tree.remove(key.getLat(), key.getLon(), key);
			
		}
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterPrimary(int)
	 * When a bucket is made primary on a node, make sure all of the keys in that bucket are added
	 * to the Spatial Index
	 */
	@Override
	public void afterPrimary(int bucketId)
	{
		tree = QuadTreeIndex.getSingleton();
		Bucket b = region.getRegionAdvisor().getBucket(bucketId);
		BucketRegion bucketRegion = b.getBucketAdvisor().getProxyBucketRegion().getHostedBucketRegion();
		Set <SpatialKey> keys = (Set <SpatialKey>)bucketRegion.keySet();
		for (SpatialKey key:keys)
		{
			tree.put(key.getLat(), key.getLon(), key);
		}

	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.partition.PartitionListener#afterRegionCreate(com.gemstone.gemfire.cache.Region)
	 */
	@Override
	public void afterRegionCreate(Region<?, ?> reg)
	{
		region = (PartitionedRegion)reg;

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
