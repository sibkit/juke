package juke;


import juke.exceptions.JukeException;
import juke.mapping.*;
import juke.querying.queries.EntityQuery;
import juke.querying.queries.Query;
import juke.querying.conditions.Condition;
import juke.querying.conditions.ConditionType;
import juke.querying.fields.LinkField;
import juke.querying.fields.ValueField;
import juke.storage.Connection;
import juke.storage.QueryResultType;
import juke.storage.StorageIterator;
import juke.storage.operation.EntityOperation;
import juke.storage.operation.EntityOperationType;
import juke.storage.sequence.Sequence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author chelovek
 */
public class Session
{
	private final Connection connection;
	private final SessionManager sessionManager;
	private final MappingData mappingData;

	public Connection getConnection()
	{
		return connection;
	}

	protected Session(Connection connection, SessionManager sessionManager)
	{
		this.connection = connection;
		this.sessionManager = sessionManager;
		this.mappingData = sessionManager.getConfiguration().getMappingData();
	}

	public <T> T get(Class<T> entityClass, EntityKey key) throws JukeException
	{
		EntityMapper mapper = mappingData.getEntityMapper(entityClass);
		EntityMap map = mapper.getMap();

        EntityQuery eq = new EntityQuery(map.getEntityName());
        if(key.getValues().length==1)
        {
            KeyValue kv = key.getValues()[0];
            eq.setCondition(new Condition(ConditionType.EQUALS,
                    new LinkField(kv.getPropertyName()),
                    new ValueField(kv.getValue())));
        }
        else
        {
            Condition andCondition = new Condition(ConditionType.AND);
            for(KeyValue kv: key.getValues())
            {
                andCondition.getInnerConditions().add(
                        new Condition(
                                ConditionType.EQUALS,
                                new LinkField(kv.getPropertyName()),
                                new ValueField(kv.getValue())));
            }
            eq.setCondition(andCondition);
        }


		List data = list(eq,QueryResultType.ENTITY);
		if(data.size()>1)
			throw new JukeException("Query returns more then 1 record");
		if(data.size()==0)
			return null;
		return (T)data.get(0);
	}

	public <T> List<T> list(Query query, QueryResultType resultType) throws JukeException
	{
		Iterator qi = iterate(query, resultType);
		ArrayList result = new ArrayList();
		while (qi.hasNext())
		{
			result.add(qi.next());
		}
		return result;
	}

	public Iterator iterate(Query query, QueryResultType resultType) throws JukeException
	{
		StorageIterator si = connection.iterate(query);
		return new QueryIterator(mappingData, si, resultType);
	}

	public void executeOperation(Object entity, EntityOperationType operationType) throws JukeException
	{
		if (entity == null)
			throw new JukeException("entity is null");
		EntityMapper mapper = this.mappingData.getEntityMapper(entity.getClass());
		EntityContent content = new EntityContent(mapper.getMap());
		mapper.bindToContent(entity, content);
		EntityOperation operation = new EntityOperation();
		operation.setNewContent(content);
		operation.setOperationType(operationType);
		connection.executeOperation(operation);
	}

	public void update(Object entity) throws JukeException
	{
		executeOperation(entity, EntityOperationType.UPDATE);
	}

	public void delete(Object entity) throws JukeException
	{
		executeOperation(entity, EntityOperationType.DELETE);
	}

	public void insert(Object entity) throws JukeException
	{
		executeOperation(entity, EntityOperationType.INSERT);
	}

	public SessionManager getSessionManager()
	{
		return sessionManager;
	}

	public void close() throws JukeException
	{
		connection.close();
	}

	public <T> Sequence<T> getSequence(String sequenceName) throws JukeException
	{
		SequenceMap map = this.mappingData.getSequenceMap(sequenceName);
		return (Sequence<T>)connection.getSequence(map);
	}

	public void createTable(String entityName)
	{
		throw new JukeException("Session: createTable (Not implemented)");
	}

}
