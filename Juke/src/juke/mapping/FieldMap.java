package juke.mapping;

public class FieldMap
{
    private EntityMap entityMap;
    private String columnName;
    private String fieldName;
    private boolean keyField;
    private boolean isRequiredField;
    private Class contentValueClass;
    private Class storageValueClass;
    private int index;
    private ValueConverter valueConverter;

    public FieldMap()
    {

    }

    public FieldMap(String fieldName, String columnName)
    {
        this();
        this.setColumnName(columnName);
        this.setFieldName(fieldName);
    }

    public FieldMap(String fieldName, String columnName, Class valueClass)
    {
        this();
        this.setColumnName(columnName);
        this.setFieldName(fieldName);
        this.contentValueClass = valueClass;
        this.storageValueClass = valueClass;
    }

    public FieldMap(String fieldName, String columnName, boolean isKeyField, boolean isRequiredField)
    {
        this(fieldName, columnName);
        this.setKeyField(isKeyField);
        this.setRequiredField(isRequiredField);
    }

    void setEntityMap(EntityMap entityMap)
    {
        this.entityMap = entityMap;
    }

    public EntityMap getEntityMap()
    {
        return entityMap;
    }

    public String getColumnName()
    {
        return columnName;
    }

    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public boolean isKeyField()
    {
        return keyField;
    }

    public void setKeyField(boolean keyField)
    {
        this.keyField = keyField;
    }

    public boolean isRequiredField()
    {
        return isRequiredField;
    }

    public void setRequiredField(boolean requiredField)
    {
        isRequiredField = requiredField;
    }

    public Class getContentValueClass()
    {
        return contentValueClass;
    }

    public void setContentValueClass(Class contentValueClass)
    {
        this.contentValueClass = contentValueClass;
    }

    public Class getStorageDataType()
    {
        return storageValueClass;
    }

    public void setStorageValueClass(Class storageValueClass)
    {
        this.storageValueClass = storageValueClass;
    }

    public int getIndex()
    {
        return index;
    }

    void setIndex(int index)
    {
        this.index = index;
    }

    public ValueConverter getValueConverter()
    {
        return valueConverter;
    }

    public void setValueConverter(ValueConverter valueConverter)
    {
        this.valueConverter = valueConverter;
    }
}
