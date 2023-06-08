package juke.storage;

import juke.exceptions.JukeException;
import juke.MappingData;
import juke.mapping.EntityMap;
import juke.querying.queries.EntityQuery;
import juke.querying.queries.JoinQuery;
import juke.querying.queries.Query;

import java.sql.ResultSet;

public class ResultSetHandler
{
    private MappingData mappingData;
    private int fieldsCount;

    public ResultSetHandler(MappingData mappingData)
    {
        this.mappingData = mappingData;
    }

    private Query query;

    int getFieldsCount(Query query)
    {
        switch (query.getElementType())
        {
            case ENTITY_QUERY:
                EntityQuery eq = (EntityQuery)query;
                EntityMap map = mappingData.getEntityMapper(eq.getEntityName()).getMap();
                return map.getFieldMaps().length;
            case GROUP_QUERY:
                return query.getFields().size();

            case JOIN_QUERY:
                if(query.getFields().size()!=0)
                    fieldsCount = query.getFields().size();
                else
                {
                    JoinQuery jq = (JoinQuery)query;
                    return getFieldsCount(jq.getLeftSource().getQuery())+getFieldsCount(jq.getRightSource().getQuery());
                }

            default:
                throw new JukeException("Unknown query type");
        }
    }

    public Query getQuery()
    {
        return query;
    }
    public void setQuery(Query query)
    {
        this.query = query;
        fieldsCount = getFieldsCount(query);
    }

    public Object[] handleRow(ResultSet rs) throws JukeException
    {
        try
        {
            Object[] result = new Object[fieldsCount];
            for (int i = 0; i < fieldsCount; i++)
            {
                result[i] = rs.getObject(i+1);
            }
            return result;
        }
        catch (Exception ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }


}