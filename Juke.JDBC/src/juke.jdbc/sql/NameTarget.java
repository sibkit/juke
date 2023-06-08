package juke.jdbc.sql;

import juke.querying.queries.Source;

public class NameTarget
{
    private String fieldName;
    private String storageFieldName;
    private Source source;

    public NameTarget(String fieldName, String storageFieldName, Source source)
    {
        this.fieldName = fieldName;
        this.storageFieldName = storageFieldName;
        this.source = source;
    }

    public String getFieldName()
    {
        return fieldName;
    }
    public String getStorageFieldName()
    {
        return storageFieldName;
    }
    public Source getSource()
    {
        return source;
    }

}
