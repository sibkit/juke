package juke.cache;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 23.10.12
 * Time: 18:14
 * To change this template use File | Settings | File Templates.
 */

import juke.mapping.EntityContent;
import juke.mapping.EntityKey;
import juke.storage.operation.EntityOperationType;

public class EntitySnapShot
{
	public static Object FLUSH_POINT;
	public static Object PUT_KEY;

	private Object entity;
	private EntityContent content;
	private EntityPutAction putAction;
	private Object savePoint;
	private EntityKey entityKey;
	private EntityOperationType entityOperationType;

	public EntityContent getContent()
	{
		return content;
	}

	public void setContent(EntityContent content)
	{
		this.content = content;
	}

	public EntityPutAction getPutAction()
	{
		return putAction;
	}

	public void setPutAction(EntityPutAction putAction)
	{
		this.putAction = putAction;
	}

	public Object getSavePoint()
	{
		return savePoint;
	}

	public void setSavePoint(Object savePoint)
	{
		this.savePoint = savePoint;
	}

	public Object getEntity()
	{
		return entity;
	}

	public void setEntity(Object entity)
	{
		this.entity = entity;
	}

	public EntityKey getEntityKey()
	{
		return entityKey;
	}

	public void setEntityKey(EntityKey entityKey)
	{
		this.entityKey = entityKey;
	}

	public EntityOperationType getEntityOperationType()
	{
		return entityOperationType;
	}

	public void setEntityOperationType(EntityOperationType entityOperation)
	{
		this.entityOperationType = entityOperation;
	}
}
