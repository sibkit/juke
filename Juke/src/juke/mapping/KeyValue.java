package juke.mapping;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 04.10.12
 * Time: 19:23
 * To change this template use File | Settings | File Templates.
 */
public class KeyValue
{
	private FieldMap fieldMap;
	private Object value;

	public KeyValue(FieldMap fieldMap, Object value)
	{
		this.fieldMap = fieldMap;
		this.value = value;
	}

	public String getPropertyName()
	{
		return fieldMap.getFieldName();
	}

	public Object getValue()
	{
		return value;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj==null)
			return false;
		KeyValue kv = (KeyValue)obj;

		if(fieldMap==null || kv.fieldMap==null)
			return false;
		if(!fieldMap.equals(kv.fieldMap))
			return false;

		if(value==null && kv.value==null)
			return true;
		if(value==null)
			return false;

		return (value.equals(kv.value));
	}

	@Override
	public String toString()
	{
		return fieldMap+": "+value;
	}
}