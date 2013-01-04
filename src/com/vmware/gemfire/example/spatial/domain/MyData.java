/**
 * 
 */
package com.vmware.gemfire.example.spatial.domain;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.gemstone.gemfire.DataSerializable;

/**
 * @author vFabric Field COE
 * A wrapper class for some piece of data that has some spatial coordinates associated with it.  For performance 
 * purposes, it's a good idea to carry along the unique identifier and the coordinates directly with the object.  
 * This may not suit the purposes of your data set, so please review when implementing
 *
 */
public class MyData implements DataSerializable
{
	private float lat, lon;
	private Object person;
	private String id;
	
	public MyData()
	{}
	
	public MyData(float lat, float lon, Object theData, String id)
	{
		super();
		this.lat = lat;
		this.lon = lon;
		this.person = theData;
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
	 * @return theData The data object that is associated with some coordinates, eg. a person,
	 * a building, a vehicle, etc.  It is the actual data to associate to a geography, not necessarily
	 * containing any coordinates itself.
	 */
	public Object getPerson()
	{
		return person;
	}

	/**
	 * @param theData The data object associated with some coordinates.
	 */
	public void setPerson(Object theData)
	{
		this.person = theData;
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
	 * Custom deserialization for this object to increase speed of deserialization
	 */
	@Override
	public void fromData(DataInput in) throws IOException,
			ClassNotFoundException
	{
		lat = in.readFloat();
		lon = in.readFloat();
		person = in.readUTF();
		id = in.readUTF();

	}

	/* (non-Javadoc)
	 * @see com.gemstone.gemfire.DataSerializable#toData(java.io.DataOutput)
	 * Custom Serialization for this object to increase speed of Serialization
	 */
	@Override
	public void toData(DataOutput out) throws IOException
	{
		out.writeFloat(lat);
	    out.writeFloat(lon);
	    out.writeUTF((String)person);
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
		MyData other = (MyData) obj;
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
		return "MyData [lat=" + lat + ", lon=" + lon + ", theData=" + person
				+ ", id=" + id + "]";
	}
	
	

	

}
