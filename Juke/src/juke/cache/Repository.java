/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package juke.cache;


import juke.exceptions.JukeException;
import sibutils.common.Iterator;

import juke.MappingData;
import juke.Session;
import juke.mapping.EntityContent;
import juke.mapping.EntityKey;
import juke.mapping.EntityMapper;
import juke.querying.queries.Query;
import juke.storage.StorageIterator;
import juke.storage.operation.EntityOperation;
import juke.storage.operation.EntityOperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository
{
	private MappingData mappingData;
	private Map<Class, EntityCaseGroup> caseMap = new HashMap<>();
	Session session;

	public Repository(MappingData mappingData)
	{
		this.mappingData = mappingData;
	}

	public void merge(Repository repository)
	{
		throw new RuntimeException("Not implemented");
	}

	public void save(Object savePoint)
	{
		for (Map.Entry<Class, EntityCaseGroup> e : caseMap.entrySet())
		{
			for (EntityCase ec : e.getValue().getCases())
			{
				EntitySnapShot ess = ec.getSnapShot();
				ec.putSnapShoot(EntityPutAction.SAVE, savePoint, ess.getEntity());
			}
		}
	}

	public void restore(Object savePoint)
	{
		for (Map.Entry<Class, EntityCaseGroup> e : caseMap.entrySet())
		{
			List<EntityCase> casesForRemove = new ArrayList<>();
			for (EntityCase ec : e.getValue().getCases())
			{
				if (!ec.restore(savePoint))
					casesForRemove.add(ec);
			}
			for (EntityCase ec : casesForRemove)
				e.getValue().getCases().remove(ec);
		}
	}

	public void attachSession(Session session)
	{
		this.session = session;
	}

	public Object get(Class entityClass, EntityKey entityKey)
	{
		EntityCaseGroup ecg = getCaseGroup(entityClass);
		EntityCase c = ecg.getByKey(entityKey);
		if (c == null)
		{
			if (session == null)
				return null;
			Object entity = session.get(entityClass, entityKey);
			if (entity != null)
				put(entity, EntityPutAction.LOAD);
			return entity;
		}
		return c.getSnapShot().getEntity();
	}

	public List list(Class entityClass)
	{
		EntityCaseGroup ecg = getCaseGroup(entityClass);
		List result = new ArrayList();
		for (EntityCase ec : ecg.getCases())
		{
			EntityOperationType ot = ec.getSnapShot().getEntityOperationType();
			if (ot != null && ot != EntityOperationType.DELETE)
				result.add(ec.getSnapShot().getEntity());
		}
		return result;
	}

	public List list(Query query)
	{
		Iterator qi = iterate(query);
		ArrayList result = new ArrayList();
		while (qi.hasNext())
		{
			result.add(qi.next());
		}
		return result;
	}


	public <T> Iterator<T> iterate(Query query)
	{
		StorageIterator si;
		si = session.getConnection().iterate(query);
		RepositoryIterator<T> result = new RepositoryIterator<>(si, this);
		return result;
	}


	public void flush() throws JukeException
	{
		List<EntityOperation> operations = new ArrayList<>();

		for (Map.Entry<Class, EntityCaseGroup> ecgEntry : caseMap.entrySet())
		{
			for (EntityCase ec : ecgEntry.getValue().getCases())
			{
				EntityOperation operation = new EntityOperation();
				EntitySnapShot sourceSs = ec.getSourceSnapShot();
				ec.putSnapShoot(EntityPutAction.SAVE, EntitySnapShot.FLUSH_POINT);
				EntitySnapShot newSs = ec.getSnapShot();
				if(newSs.getEntityOperationType()!=null)
				{
					if(sourceSs!=null)
						operation.setOldContent(sourceSs.getContent());
					operation.setNewContent(newSs.getContent());
					operation.setOperationType(newSs.getEntityOperationType());
					operations.add(operation);
				}
			}
		}
		this.session.getConnection().executeOperations(operations);
	}

	private void put(Object entity, EntityPutAction putAction) throws JukeException
	{
		if (entity == null)
			throw new JukeException("entity is null");

		Class eClass = entity.getClass();
		EntityCaseGroup cmi = getCaseGroup(eClass);

		EntityMapper mapper = mappingData.getEntityMapper(eClass);
		EntityContent content = new EntityContent(mapper.getMap());
		mapper.bindToContent(entity, content);
		EntityKey key = content.excludeKey();
		EntityCase eCase = cmi.getByKey(key);

		if(eCase==null)
		{
			eCase = new EntityCase(mapper);
			eCase.putSnapShoot(putAction, EntitySnapShot.PUT_KEY, entity);
			cmi.put(eCase);
		}
		else
		{
			eCase.putSnapShoot(putAction, EntitySnapShot.PUT_KEY, entity);
		}
	}

	public void delete(Object entity) throws JukeException
	{
		put(entity, EntityPutAction.DELETE);
	}

	public void insert(Object entity) throws JukeException
	{
		put(entity, EntityPutAction.INSERT);
	}

	public void put(Object entity) throws JukeException
	{
		put(entity, EntityPutAction.LOAD);
	}

	public void remove(Object entity) throws JukeException
	{
		put(entity, EntityPutAction.REMOVE);
	}

	public void clearGroup(Class entityClass)
	{
		caseMap.get(entityClass).clear();
	}

	public void clear()
	{
		for (Map.Entry<Class, EntityCaseGroup> e : caseMap.entrySet())
		{
			e.getValue().clear();
		}
	}

	MappingData getMappingData()
	{
		return mappingData;
	}

	public EntityCaseGroup getCaseGroup(Class entityClass)
	{
		EntityCaseGroup result = caseMap.get(entityClass);
		if (result == null)
		{
			result = new EntityCaseGroup(mappingData.getEntityMapper(entityClass));
			caseMap.put(entityClass, result);
		}
		return result;
	}
}
