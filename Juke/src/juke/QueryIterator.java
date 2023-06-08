package juke;


import juke.exceptions.JukeException;
import juke.mapping.EntityMapper;
import juke.querying.queries.EntityQuery;
import juke.storage.QueryResultType;
import juke.storage.StorageIterator;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 19.01.12
 * Time: 11:55
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public class QueryIterator<T> implements Iterator<T>
{
    private final StorageIterator storageIterator;
    private final MappingData mappingData;
    private final QueryResultType resultType;
    private EntityBuilder<T> builder;

    public QueryIterator(MappingData data, StorageIterator storageIterator, QueryResultType resultType)
    {
        this.storageIterator =  storageIterator;
        this.mappingData = data;
        this.resultType = resultType;
    }

    public boolean hasNext() throws JukeException
    {
        return storageIterator.hasNext();
    }


	EntityBuilder<T> getBuilder()
	{
		if(builder==null)
		{
		    EntityQuery eq = (EntityQuery) storageIterator.getQuery();
			EntityMapper eMapper = this.mappingData.getEntityMapper(eq.getEntityName());
			builder = new EntityBuilder<T>(eMapper);
		}
		return builder;
	}

    public T next() throws JukeException
    {
        Object[]  siResult = this.storageIterator.next();

        switch (resultType)
        {
            case OBJECT_ARRAY:
                return (T)siResult;
            case ENTITY:
                if(storageIterator.getQuery() instanceof EntityQuery)
                {
					return getBuilder().build(siResult);
                }
                else throw new JukeException("Invalid QueryResultType for this query");
            default:
                throw new JukeException("Invalid QueryResultType for this query");

        }
    }
}
