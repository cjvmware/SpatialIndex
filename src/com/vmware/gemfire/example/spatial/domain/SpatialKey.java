package com.vmware.gemfire.example.spatial.domain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.gemstone.gemfire.DataSerializable;

/**
 * @author vFabric Field COE
 * This class represents the key object for the data that will be indexed via that QuadTree.
 * The latitude and longitude are carried along with the key as a reference, but the 
 * uniqueness of the key is based strictly on the 'id' field, as seen in the equals and hashCode 
 * implementations.  
 *
 */
public class SpatialKey implements DataSerializable
{
	private float lat, lon;
	private String id;
	
	
	/**
	 * Default no arg constructor - required for DataSerializable implementation
	 */
	public SpatialKey ()
	{}
	
	/**
	 * @param lat The latitude associated with this key
	 * @param lon The longitude associated with this key
	 * @param id  The unique identifier of the key
	 */
	public SpatialKey (float lat, float lon, String id)
	{
		this.lat = lat;
		this.lon = lon;
		this.id = id;
	}
	
	/**
	 * @return The longitude of the data point
	 */
	public float getLat()
	{
		return lat;
	}

	/**
	 * @param lat The latitude to set for this data point
	 */
	public void setLat(float lat)
	{
		this.lat = lat;
	}

	/**
	 * @return The longitude for this data point
	 */

	public float getLon()
	{
		return lon;
	}

	/**
	 * @param lon The longitude to set for this data point
	 */
	public void setLon(float lon)
	{
		this.lon = lon;
	}

	/**
	 * @return The String that uniquely identifies this data point in the set of points being represented.
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id The unique identifier for his data point in the data set.  If this is not unique within the data
	 * set, data may be overwritten or lost on collisions.
	 */
	public void setId(String id)
	{
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.DataSerializable#fromData(java.io.DataInput)
	 * Custom deserializaiton for this object to increase speed of deserialization
	 */
	@Override
	public void fromData(DataInput in) throws IOException,
			ClassNotFoundException
	{
		this.lat = in.readFloat();
		this.lon = in.readFloat();
		this.id = in.readUTF();

		
	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.DataSerializable#toData(java.io.DataOutput)
	 * Custom Serializaiton for this object to increase speed of Serialization
	 */
	@Override
	public void toData(DataOutput out) throws IOException
	{
		out.writeFloat(lat);
	    out.writeFloat(lon);
	    out.writeUTF(id);	
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 * Used in assigning which bucket, or partition, this data object will belong.  Note that only 
	 * the id field is used in this calculation
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 * Determines if two objects are equal.  In this case, this returns whether or not the String that represents the
	 * id field are equivalent, using the String.equals() method.
	 * 
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SpatialKey other = (SpatialKey) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 * Returns a String representation of this object for debugging purposes
	 */
	@Override
	public String toString()
	{
		return "SpatialKey [lat=" + lat + ", lon=" + lon + ", id=" + id + "]";
	}
}
