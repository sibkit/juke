package juke.querying.queries;

import juke.querying.QueryElementType;

public class EntityQuery extends Query
{
    private String entityName;

    public EntityQuery(String entityName)
    {
        this.entityName = entityName;
    }

    public String getEntityName()
    {
        return entityName;
    }
    public void setEntityName(String entityName)
    {
        this.entityName = entityName;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.ENTITY_QUERY;
    }

}
