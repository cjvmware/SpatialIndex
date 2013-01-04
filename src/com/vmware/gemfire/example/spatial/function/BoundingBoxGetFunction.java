/**
 * 
 */
package com.vmware.gemfire.example.spatial.function;

import java.util.Properties;
import java.util.Vector;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.ResultSender;

import java.util.ArrayList;

import com.gemstone.gemfire.cache.Region;

import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.vmware.gemfire.example.spatial.domain.MyData;
import com.vmware.gemfire.example.spatial.domain.SpatialKey;
import com.vmware.gemfire.example.spatial.index.QuadTreeIndex;


/**
 * @author vFabric Field COE
 *
 */
public class BoundingBoxGetFunction implements Function, Declarable
{

	transient QuadTreeIndex tree = null;


	/* (non-Javadoc)
	 * A function that gets data associated with a set of four points.  It is assumed this function is called with
	 * 'onRegion()' set to the region that holds the data that the Spatial Index points to. 
	 * @see com.gemstone.gemfire.cache.execute.Function#execute(com.gemstone.gemfire.cache.execute.FunctionContext)
	 */
	@Override
	public void execute(FunctionContext fc)
	{
		tree = QuadTreeIndex.getSingleton();
		ArrayList<Float> args = (ArrayList<Float>)fc.getArguments();
		float n = (Float)args.get(0).floatValue();
		float w = (Float)args.get(1).floatValue();
		float s = (Float)args.get(2).floatValue();
		float e = (Float)args.get(3).floatValue();
				
		ResultSender rs = fc.getResultSender();

		
		if (fc instanceof RegionFunctionContext)
		{		
			RegionFunctionContext rfc = (RegionFunctionContext)fc;
			Region <SpatialKey, MyData> deals = rfc.getDataSet();	
			Vector <SpatialKey> keys = (Vector <SpatialKey>)tree.get(n,  w, s, e);
			
			for (SpatialKey key:keys)
			{
				MyData data = (MyData)deals.get(key);
				rs.sendResult(data);
			}
					
		}
		//send a null at the end - not a perfect thing to do.  Client must to a null check.
		rs.lastResult(null);

	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.Function#getId()
	 * This must be implemented for the server to start properly
	 */
	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return "BoundingBoxGetFunction";
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.Function#hasResult()
	 * This function will always return a result
	 */
	@Override
	public boolean hasResult()
	{
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.Function#isHA()
	 * This function is not implemented as HA
	 */
	@Override
	public boolean isHA()
	{
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.Function#optimizeForWrite()
	 * No writes to the data set are done during this method call
	 */
	@Override
	public boolean optimizeForWrite()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.Declarable#init(java.util.Properties)
	 * Get a reference to the singleton QuadTreeIndex for this cachenode
	 */
	@Override
	public void init(Properties arg0)
	{
		tree = QuadTreeIndex.getSingleton();
		
	}

}
