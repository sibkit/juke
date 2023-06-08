package juke.mapping;

import juke.exceptions.JukeException;
import sibutils.common.ReflectionUtils;

public abstract class EntityMapper<T>
{
    public abstract Object readValue(T entity, FieldMap fieldMap);

    public abstract void writeValue(T entity, Object value, FieldMap fieldMap);

    public void bindToEntity(EntityContent content, T entity)
	{
		for(FieldMap fm: map.getFieldMaps())
		    writeValue(entity,content.getFieldValue(fm.getIndex()),fm);
	}

	public void bindToContent(T entity, EntityContent content)
	{
		for(FieldMap fm: map.getFieldMaps())
        {
            content.setFieldValue(fm.getIndex(),readValue(entity,fm));
        }
	}

    private EntityMap map;

    protected abstract EntityMap createMap();

    public EntityMap getMap() {
        if (map == null)
            map = createMap();
        return map;
    }

    private Class<T> entityClass;

    public abstract T createNewEntity();

    public Class<T> getEntityClass() {
        if (entityClass == null)
        {
            entityClass = (Class<T>)ReflectionUtils.getGenericParameterClass(this.getClass(), EntityMapper.class, 0);
        }
        return entityClass;
    }

    public EntityKey excludeKey(T entity) throws JukeException
    {
        if(entity==null)
            throw new JukeException("entity is null");

        EntityMap map = getMap();

        int[] keyIndexes = getMap().getKeyIndexes();
        if (keyIndexes.length == 0)
            throw new JukeException("Key is not specified for entity: " + map.getEntityName());

        KeyValue[] values = new KeyValue[keyIndexes.length];
        for (int i = 0; i < keyIndexes.length; i++)
        {
            int ki = keyIndexes[i];
            FieldMap kfm = map.getFieldMap(ki);
            values[i] = new KeyValue(kfm, readValue(entity,kfm));
        }

        return new EntityKey(values);
    }

/*
	int[] bindingMap;
	int[] getBindingMap(QueryResultItem[] siResult, FieldMap[] maps) throws JukeException
    {
        if (bindingMap == null)
        {
            bindingMap = new int[siResult.length];
            for (int i = 0; i < siResult.length; i++)
            {
                for (int j = 0; j < maps.length; j++)
                {
                    if (siResult[i].getField() instanceof LinkField)
                    {
                        LinkField lp = (LinkField) siResult[i].getField();

                        if (lp.getAlias() == null)
                        {
                                if (lp.getName().equals(maps[j].getFieldName()))
                                    bindingMap[i] = j;

                        }
                        else if (maps[j].getFieldName().equals(lp.getAlias()))
                                bindingMap[i] = j;
                    }
                }
            }
        }
        return bindingMap;
    }
*/

	public EntityContent buildContent(Object[] resultItems) throws JukeException
	{
		EntityMap em = getMap();
		EntityContent content = new EntityContent(em);
		//FieldMap[] fms = em.getFieldMaps();
		//int[] bm = getBindingMap(resultItems,fms);
		for(int i = 0;i<resultItems.length;i++)
		{
			content.setFieldValue(i,resultItems[i]);
		}
		return content;
	}
}
