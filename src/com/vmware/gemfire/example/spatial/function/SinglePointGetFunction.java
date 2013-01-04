package com.vmware.gemfire.example.spatial.function;

import java.util.ArrayList;
import java.util.Properties;

import com.gemstone.gemfire.cache.Declarable;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.execute.Function;
import com.gemstone.gemfire.cache.execute.FunctionContext;
import com.gemstone.gemfire.cache.execute.RegionFunctionContext;
import com.vmware.gemfire.example.spatial.domain.MyData;
import com.vmware.gemfire.example.spatial.domain.SpatialKey;
import com.vmware.gemfire.example.spatial.index.QuadTreeIndex;


/**
 * @author vFabric Field COE
 * This class is a Function that is executed on GemFire server nodes.  It takes two float arguments,
 * then does a "get" on the Spatial Index to get the data associated with that lat/long.  The function
 * call is simply a pass through to the Spatial Index.
 *
 */
public class SinglePointGetFunction implements Function, Declarable
{

	transient QuadTreeIndex tree = null;
	/* (non-Javadoc)
	 * A function that gets data associated with a single lat/long.  It is assumed this function is called with
	 * 'onRegion()' set to the region that holds the data that the Spatial Index points to. 
	 * @see com.gemstone.gemfire.cache.execute.Function#execute(com.gemstone.gemfire.cache.execute.FunctionContext)
	 */
	@Override
	public void execute(FunctionContext fc)
	{
		tree = QuadTreeIndex.getSingleton();
		//Retrieve the arguments from the function call
		ArrayList<Float> args = (ArrayList<Float>)fc.getArguments();
		float lat = (Float)args.get(0).floatValue();
		float lon = (Float)args.get(1).floatValue();
		
		//Call the desired function on the index, a key will be returned
		SpatialKey key = tree.get(lat, lon);
		
		//It is assumed that the function call is made 
		Region <SpatialKey, MyData> theData = null;
		if (fc instanceof RegionFunctionContext)
		{
			RegionFunctionContext rfc = (RegionFunctionContext)fc;
			theData = rfc.getDataSet();
		}
		
		//Call a get on the region with the key returned from the Spatial Index above
		MyData dataPoint = theData.get(key);
		
		//Since there is only one result, send "lastResult" to the ResultCollector to notify the client 
		//that all results have been sent.
		fc.getResultSender().lastResult(dataPoint);
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.cache.execute.Function#getId()
	 * This must be implemented for the server to start properly
	 */
	@Override
	public String getId()
	{
		// TODO Auto-generated method stub
		return "SinglePointGetFunction";
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
		
		
	}

}
