package juke.mapping;


import juke.exceptions.JukeException;

public class EntityContent
{
	private EntityMap entityMap;

	public EntityMap getEntityMap()
	{
		return this.entityMap;
	}

	private Object[] fieldValues;

	public EntityContent(EntityMap entityMap)
	{
		this.entityMap = entityMap;
		this.fieldValues = new Object[entityMap.getFieldMaps().length];
	}

	public EntityKey excludeKey()
	{
		int[] keyIndexes = entityMap.getKeyIndexes();
		if (keyIndexes.length == 0)
			throw new JukeException("Key is not specified for entity: " + entityMap.getEntityName());

		KeyValue[] values = new KeyValue[keyIndexes.length];
		for (int i = 0; i < keyIndexes.length; i++)
		{
			int ki = keyIndexes[i];
			FieldMap kfm = entityMap.getFieldMap(ki);
			values[i] = new KeyValue(kfm, fieldValues[ki]);// = fieldValues[ki];
		}
		return new EntityKey(values);
	}

	public <T> void setFieldValue(int fieldIndex, T value)
	{
		this.fieldValues[fieldIndex] = value;
	}

	public <T> T getFieldValue(int fieldIndex)
	{
		return (T) this.fieldValues[fieldIndex];
	}

	@Override
	public boolean equals(Object obj)
	{
		EntityContent content = (EntityContent) obj;
		if (content == null)
			return false;
		if (content.entityMap == null)
			return false;
		if (!content.entityMap.equals(this.entityMap))
			return false;
		for (int i = 0; i < fieldValues.length; i++)
		{
			if (fieldValues[i] == null && content.fieldValues[i] != null)
				return false;
			if (fieldValues[i] != null)
				if (!fieldValues[i].equals(content.fieldValues[i]))
					return false;
		}
		return true;
	}
}
