package juke.validation;

import juke.MappingData;
import juke.mapping.EntityMapper;
import juke.mapping.FieldMap;
import juke.querying.fields.LinkField;
import juke.querying.queries.EntityQuery;
import juke.querying.queries.Query;

public class QueryChecker
{
    private void checkFields()
    {

    }

    public static void complete(Query query, MappingData md)
    {

        //ENTITY
        //GROUP
        //JOIN
        if(query instanceof EntityQuery)
        {
            EntityQuery eq = (EntityQuery)query;

            if(eq.getFields().size()==0)
            {
                EntityMapper mapper = md.getEntityMapper(eq.getEntityName());
                for(FieldMap fm : mapper.getMap().getFieldMaps())
                {
                    eq.getFields().add(new LinkField(fm.getFieldName()));
                }
            }
        }
    }
}
