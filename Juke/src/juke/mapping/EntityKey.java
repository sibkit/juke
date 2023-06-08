package juke.mapping;

public class EntityKey
{
	private KeyValue[] values;
	public EntityKey(KeyValue[] values)
	{
		this.values = values;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(super.equals(obj))
			return true;

		if(obj==null)
			return false;
		if(obj.getClass()!=getClass())
			return false;

		EntityKey ek = (EntityKey)obj;

		if(values.length!=ek.getValues().length)
			return false;

		for(KeyValue kv: values)
		{
			boolean found = false;
			for(KeyValue kv2: ek.values)
			{
				 if(kv.equals(kv2))
				 {
					 found = true;
					 break;
				 }
			}
			if(!found)
				return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		return 1;
		/*
		int result=0;
		for(Object o:values)
		if(o!=null)
			result+=o.hashCode();
		return result;      */
	}


	public String toString()
	{
		String result = "EntityKey [";
		for(Object o:values)
			result+=o+", ";
		result = result.substring(0,result.length()-2);
		result+="]";
		return result;
	}

	public KeyValue[] getValues()
	{
		return values;
	}
}
