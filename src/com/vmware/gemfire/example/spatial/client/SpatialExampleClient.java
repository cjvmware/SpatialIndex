/**
 * 
 */
package com.vmware.gemfire.example.spatial.client;

import java.util.ArrayList;
import java.util.Iterator;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientCache;
import com.gemstone.gemfire.cache.client.ClientCacheFactory;
import com.gemstone.gemfire.cache.execute.Execution;
import com.gemstone.gemfire.cache.execute.FunctionService;
import com.gemstone.gemfire.cache.execute.ResultCollector;

import com.vmware.gemfire.example.spatial.domain.MyData;
import com.vmware.gemfire.example.spatial.domain.SpatialKey;
import com.vmware.gemfire.example.spatial.function.BoundingBoxGetFunction;
import com.vmware.gemfire.example.spatial.function.SinglePointGetFunction;


/**
 * @author vFabric Field COE
 * This client is an example of how to load data into an alternative index and call functions against it.
 *
 */
public class SpatialExampleClient
{

	private Region myData = null;
	ClientCache cache = null;
	private static final int NUM_DATA_POINTS = 100;
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		SpatialExampleClient test = new SpatialExampleClient();
		test.initData();
		test.callSinglePointGet();
		test.callBoundingBoxGet();

	}
	
	public void initData()
	{
		cache = new ClientCacheFactory()
		  .set("name", "ClientWorker")
	      .set("cache-xml-file", "xml/client.xml")
	      .create();

	    // Get the exampleRegion
	    myData = cache.getRegion("MyData");
	    float currentLat = 0;
	    float currentLon = 0;
	    String id = null;
	    
	    for (int i = 0; i < NUM_DATA_POINTS; i++)
	    {
	    	SpatialKey key = new SpatialKey(currentLat, currentLon, getDummyGUID());
	    	MyData person = new MyData(currentLat, currentLon, "I am some Object", "I am a filler object" + i);
	    	currentLat++;
	    	currentLon++;
	    	myData.put(key, person);
	    }
	    
	}
	
	public void callSinglePointGet()
	{
		System.out.println("***Running single point get***");
		SinglePointGetFunction function = new SinglePointGetFunction();
		FunctionService.registerFunction(function);
		
		ResultCollector rc;
		
		ArrayList args = new ArrayList();
		args.add (new Float(1));
		args.add (new Float(1));

		Execution execution = FunctionService.onRegion(myData).withArgs(args);

		rc = execution.execute(function);
		
		ArrayList list = (ArrayList)rc.getResult();
		System.out.println(list);
	}
	
	public String getDummyGUID()
	{
		
		String guid = new Long (Thread.currentThread().getId()).toString();
		Double time = new Double(System.currentTimeMillis()*Math.random()*3);
	//	System.out.println(time);

		return guid + time.toString();
		
	}
	
	public void callBoundingBoxGet()
	{
		BoundingBoxGetFunction function = new BoundingBoxGetFunction();
		FunctionService.registerFunction(function);
		ResultCollector rc;

		ArrayList args = new ArrayList();
		args.add (new Float(10));
		args.add (new Float(-10));
		args.add (new Float(-10));
		args.add (new Float(10));
		

		Execution execution = FunctionService.onRegion(myData).withArgs(args);
		rc = execution.execute(function);

		System.out.println("***Running bounding box get***");
		ArrayList list = (ArrayList)rc.getResult();
		Iterator it = list.iterator();
		while (it.hasNext())
		{
			MyData theData = (MyData)it.next();
			if (theData != null)
				System.out.println("Result was: " + theData);
		}

	}

}
