package juke.mapping;


import juke.exceptions.JukeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityMap
{

    private Map<String,Integer> fieldIndexes = new HashMap<String, Integer>();
    private String tableName;
    private String entityName;
    private FieldMap[] fieldMaps;
    private int[] keyIndexes;
    private String[] fieldNames;

    public EntityMap(String entityName, String tableName)
    {
        this.tableName = tableName;
        this.entityName = entityName;
    }


    public String getTableName() {
        return tableName;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setFieldMaps(List<FieldMap> fieldMaps)
    {
        this.fieldMaps = fieldMaps.toArray(new FieldMap[fieldMaps.size()]);
        List<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < this.fieldMaps.length; i++)
        {
            FieldMap fm = this.fieldMaps[i];
            fieldIndexes.put(fm.getFieldName().toLowerCase(),i);
            fm.setEntityMap(this);
            fm.setIndex(i);
            if (fm.isKeyField())
                keys.add(i);
        }

        keyIndexes = new int[keys.size()];
        for (int i = 0; i < keys.size(); i++)
            keyIndexes[i] = keys.get(i);

    }

    public FieldMap getFieldMap(String propertyName)
    {
	    Integer index = fieldIndexes.get(propertyName.toLowerCase());
	    if(index==null)
	    {
		    throw new JukeException("MapNode "+propertyName+" not found for entity "+entityName);
	    }
        return fieldMaps[fieldIndexes.get(propertyName.toLowerCase())];
    }

	public FieldMap getFieldMap(int index) throws JukeException
	{
		return fieldMaps[index];
	}

    public FieldMap[] getFieldMaps() {
        return fieldMaps;
    }

    public int[] getKeyIndexes() {
        return keyIndexes;
    }

    public String[] getFieldNames()
    {
        if(fieldNames==null)
        {
            fieldNames = new String[fieldMaps.length];
            for(int i=0;i<fieldNames.length;i++)
            {
                fieldNames[i] = fieldMaps[i].getFieldName();
            }

        }
        return fieldNames;
    }
}
