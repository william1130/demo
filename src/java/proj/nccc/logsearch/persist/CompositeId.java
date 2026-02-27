/*
 * CompositeId.java
 *
 * Created on 2007年8月23日, 下午 3:31
 * ==============================================================================================
 * $Id$
 * ==============================================================================================
 */

package proj.nccc.logsearch.persist;

import java.util.ArrayList;
import java.util.List;

/**
 * 當一物件的primary key有兩個欄位時, 可繼承本類別, 做為其primary key的物件, 但請注意, 此類型的物件主要只是用來在程式流程中參考, 
 * 不應直接取代persistable物件中的primary key的欄位.
 * @author 許欽程(Vincent Shiu)
 * @version $Revision$
 */
public abstract class CompositeId
{
	protected List keys;
	
	private CompositeId()
	{
		keys = new ArrayList();
	}
	
	/** Creates a new instance of CompositeId */
	public CompositeId( Object key1, Object key2 )
	{
		this();
		keys.add( key1 );
		keys.add( key2 );
	}
	
	public CompositeId( Object key1, Object key2, Object key3 )
	{
		this();
		keys.add( key1 );
		keys.add( key2 );
		keys.add( key3 );
	}
	
	public CompositeId( Object key1, Object key2, Object key3, Object key4 )
	{
		this();
		keys.add( key1 );
		keys.add( key2 );
		keys.add( key3 );
		keys.add( key4 );
	}

	public CompositeId( Object key1, Object key2, Object key3, Object key4, Object key5 )
	{
		this();
		keys.add( key1 );
		keys.add( key2 );
		keys.add( key3 );
		keys.add( key4 );
		keys.add( key5 );
	}
	
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final CompositeId other = (CompositeId) obj;
		if( other.keys == null || this.keys == null )
			return false;
		if( other.keys.size() != this.keys.size() )
			return false;
		for( int i=0; i<this.keys.size(); i++ )
		{
			Object key1 = (Object) this.keys.get(i);
			Object key2 = (Object) other.keys.get(i);
			if( key1 == null || !key1.equals(key2) )
				return false;
		}
        return true;
    }

    public int hashCode()
    {
		if( keys == null )
			return super.hashCode();
        int hash = 11;
		for( int i=0; i<this.keys.size(); i++ )
		{
			Object key = (Object) this.keys.get(i);
			hash += (key != null ? key.hashCode() : 0);
		}
        return hash;
    }
	
}
