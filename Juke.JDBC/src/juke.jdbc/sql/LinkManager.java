package juke.jdbc.sql;

import juke.exceptions.JukeException;
import juke.MappingData;
import juke.mapping.EntityMap;
import juke.mapping.FieldMap;
import juke.querying.QueryElement;
import juke.querying.QueryElementType;
import juke.querying.fields.Field;
import juke.querying.fields.LinkField;
import juke.querying.fields.QueryField;
import juke.querying.queries.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkManager
{
    private MappingData mappingData;
    private Map<Query,List<NameTarget>> nameSpaceMap = new HashMap<>();

    public LinkManager(MappingData mappingData)
    {
        this.mappingData = mappingData;
    }

    public List<NameTarget> getNameSpace(Query query)
    {
        List<NameTarget> nameSpace = nameSpaceMap.get(query);
        if(nameSpace!=null)
            return nameSpace;
        else
            nameSpace = new ArrayList<>();

        switch (query.getElementType())
        {
            case ENTITY_QUERY:
                break;
            case GROUP_QUERY:
                GroupQuery sq = (GroupQuery) query;
                nameSpace.addAll(getAvailableTargets(sq.getSource()));
                break;
            case JOIN_QUERY:
                JoinQuery jq = (JoinQuery)query;
                nameSpace.addAll(getAvailableTargets(jq.getLeftSource()));
                nameSpace.addAll(getAvailableTargets(jq.getRightSource()));
                break;
            default:
                throw new JukeException("Unknown query type");
        }

        if(query.getParentElement()!=null)
        {
            switch (query.getParentElement().getElementType())
            {
                case SUBQUERY_FIELD:
                    QueryField qf = (QueryField)query.getParentElement();
                    Query q = findOwnedQuery(qf);
                    if(q==null)
                        throw new JukeException("Invalid query structure");
                    nameSpace.addAll(getNameSpace(q));
                    break;
            }
        }

        nameSpaceMap.put(query,nameSpace);
        return nameSpace;
    }

    Query findOwnedQuery(QueryElement element)
    {
        if(element==null)
            return null;
        switch (element.getElementType())
        {
            case ENTITY_QUERY:
            case JOIN_QUERY:
            case GROUP_QUERY:
                return (Query)element;
        }
        return findOwnedQuery(element.getParentElement());
    }

    public List<NameTarget> getAvailableTargets(Source source)
    {
        List<NameTarget> result = new ArrayList<>();
        if(source.getQuery().getFields().size()!=0)
        {
            for(Field f: source.getQuery().getFields())
            {
                if(f.getAlias()!=null&&!f.getAlias().isEmpty())
                {
                    result.add(new NameTarget(f.getAlias(),null,source));
                }
                else
                {
                    if(f.getElementType()==QueryElementType.LINK_FIELD)
                    {
                        LinkField lf = (LinkField)f;
                        result.add(new NameTarget(((LinkField) f).getName(),null,source));
                    }
                }

            }
            return result;
        }
        switch (source.getQuery().getElementType())
        {
            case ENTITY_QUERY:
                EntityQuery eq = (EntityQuery) source.getQuery();
                EntityMap map = mappingData.getEntityMapper(eq.getEntityName()).getMap();
                for (FieldMap fm : map.getFieldMaps())
                {
                    result.add(new NameTarget(fm.getFieldName(), fm.getColumnName(), source));
                }
                break;
            case JOIN_QUERY:
                JoinQuery jq = (JoinQuery)source.getQuery();
                result.addAll(getAvailableTargets(jq.getLeftSource()));
                result.addAll(getAvailableTargets(jq.getRightSource()));
                break;
            default:
                throw new JukeException("Unknown source type");
        }
        return result;
    }
}
