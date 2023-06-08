package juke.cache;

import sibutils.common.Iterator;
import juke.EntityBuilder;
import juke.exceptions.JukeException;
import juke.mapping.EntityContent;
import juke.mapping.EntityKey;
import juke.mapping.EntityMapper;
import juke.querying.queries.EntityQuery;
import juke.storage.StorageIterator;
import juke.storage.operation.EntityOperationType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 29.09.12
 * Time: 5:45
 * To change this template use File | Settings | File Templates.
 */
public class RepositoryIterator<T>  implements Iterator<T>
{
	Repository repository;
	StorageIterator storageIterator;

	List<T> insertedEntities = new ArrayList<>();

	public RepositoryIterator(StorageIterator storageIterator, Repository repository) throws JukeException
	{
		this.storageIterator =  storageIterator;
		this.repository = repository;
		prepareInsertedEntities();
		putNext();
	}

	T nextValue;

	public boolean hasNext() throws JukeException
	{
		return nextValue!=null;
	}

	private EntityMapper<T> entityMapper;
	public EntityMapper<T> getMapper()
	{
		if(entityMapper ==null)
		{
			EntityQuery eq = (EntityQuery) storageIterator.getQuery();
			entityMapper = this.repository.getMappingData().getEntityMapper(eq.getEntityName());
		}
		return entityMapper;
	}

	EntityBuilder<T> builder;
	EntityBuilder<T> getBuilder()
	{
		if(builder==null)
		{
			builder = new EntityBuilder<T>(getMapper());
		}
		return builder;
	}

	void prepareInsertedEntities()
	{
		EntityCaseGroup cmi = repository.getCaseGroup(getMapper().getEntityClass());
		for(EntityCase ec : cmi.getCases())
		{
			EntitySnapShot ess = ec.getSnapShot();
			if(ess.getEntityOperationType()== EntityOperationType.INSERT)
			insertedEntities.add((T)ess.getEntity());
		}
	}

	void putNext() throws JukeException
	{
		nextValue=null;
		if(insertedEntities.size()>0)
		{
			nextValue = insertedEntities.get(insertedEntities.size()-1);
			insertedEntities.remove(nextValue);
		}
		else
		{
			while(storageIterator.hasNext())
			{
				Object[] qrResult = storageIterator.next();
				EntityContent content = getMapper().buildContent(qrResult);

				EntityKey key = content.excludeKey();
				EntityCaseGroup cmi = repository.getCaseGroup(getMapper().getEntityClass());
				EntityCase eCase = cmi.getByKey(key);
				EntitySnapShot ss = null;
				EntityOperationType ea = null;

				if(eCase!=null)
					ss = eCase.getSnapShot();

				if(ss!=null)
					ea = ss.getEntityOperationType();

				if(ea == null)
				{
					eCase = new EntityCase(getMapper());
					T entity =  getBuilder().build(content);
					eCase.putSnapShoot(EntityPutAction.LOAD,EntitySnapShot.PUT_KEY,entity);
					cmi.put(eCase);

					nextValue = entity;
					return;
				}

				if(ea == EntityOperationType.UPDATE)
				{
					nextValue = (T)ss.getEntity();
					return;
				}


				 // if EntityPutAction.DELETE do nothing
			}
		}
	}

	public T next() throws JukeException
	{
		if(nextValue==null)
			throw new JukeException("RepositoryIterator: next");
		T result = nextValue;

		putNext();
		return result;
	}
}
