package com.vmware.gemfire.example.spatial.util;

public class GUIDUtil
{
		
		/**
		 * @return a somewhat unique GUID based on the current thread and the time
		 * For Testing and POC purposes only - not for production use
		 */
		public static String getDummyGUID()
		{
			
			String guid = new Long (Thread.currentThread().getId()).toString();
			Double time = new Double(System.currentTimeMillis()*Math.random()*3);
			return guid + time.toString();
			
		}
}
