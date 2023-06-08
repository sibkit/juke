package juke;


import juke.mapping.EntityMapper;
import juke.mapping.SequenceMap;
import juke.querying.queries.Query;
import juke.validation.QueryChecker;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 12.02.12
 * Time: 23:38
 * To change this template use File | Settings | File Templates.
 */
public class MappingData
{
    private Map<String,EntityMapper> mappersByName = new HashMap<>();
    private Map<Class,EntityMapper> mappersByClass = new HashMap<>();
	private Map<String,SequenceMap> sequenceMap = new HashMap<>();

    public Collection<EntityMapper> getMappers() {
        return this.mappersByName.values();
    }

	public void registerSequence(String sequenceName, String storageSequenceName)
	{
		sequenceMap.put(sequenceName,new SequenceMap(sequenceName,storageSequenceName));
	}

	public SequenceMap getSequenceMap(String sequenceName)
	{
		SequenceMap result = sequenceMap.get(sequenceName);
		if(result==null)
		{
			throw new RuntimeException("Unknown sequence name: "+sequenceName);
		}
		return result;
	}

    public void putMapper(EntityMapper mapper)
    {
        mappersByName.put(mapper.getMap().getEntityName().toLowerCase(),mapper);
        mappersByClass.put(mapper.getEntityClass(),mapper);
    }

    public void removeMapper(EntityMapper mapper)
    {
        mappersByName.remove(mapper.getMap().getEntityName().toLowerCase());
        mappersByClass.remove(mapper.getEntityClass());
    }

    public EntityMapper getEntityMapper(String entityName)
    {
        return mappersByName.get(entityName.toLowerCase());
    }

    public EntityMapper getEntityMapper(Class entityClass)
    {
        return mappersByClass.get(entityClass);
    }

    public void completeQuery(Query query)
    {
        QueryChecker.complete(query, this);
    }
}
