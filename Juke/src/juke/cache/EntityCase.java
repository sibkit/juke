package juke.cache;


import juke.mapping.EntityContent;
import juke.mapping.EntityMapper;
import juke.storage.operation.EntityOperationType;

import java.util.ArrayList;
import java.util.List;

public class EntityCase
{
	private EntityMapper mapper;
	List<EntitySnapShot> snapShoots = new ArrayList<>();

	EntitySnapShot getSourceSnapShot()
	{
		for (int i = snapShoots.size() - 1; i >= 0; i--)
		{
			EntitySnapShot ss = snapShoots.get(i);
			if (ss.getSavePoint() == EntitySnapShot.FLUSH_POINT || ss.getPutAction()==EntityPutAction.LOAD)
				return ss;
		}
		return null;
	}

	public EntityCase(EntityMapper mapper)
	{
		this.mapper = mapper;
	}

	public void putSnapShoot(EntityPutAction putAction, Object savePoint)
	{
		putSnapShoot(putAction,savePoint,getSnapShot().getEntity());
	}

	public void putSnapShot(EntityPutAction putAction)
	{
		putSnapShoot(putAction,null);
	}

	public void putSnapShoot(EntityPutAction putAction, Object savePoint, Object entity)
	{
		EntityContent ec = new EntityContent(getMapper().getMap());
		getMapper().bindToContent(entity, ec);

		EntitySnapShot ss = new EntitySnapShot();
		ss.setContent(ec);
		ss.setSavePoint(savePoint);
		ss.setPutAction(putAction);
		ss.setEntity(entity);
		ss.setEntityKey(ec.excludeKey());
		ss.setEntityOperationType(calculateExecuteAction(putAction));

		snapShoots.add(ss);
	}

	public EntityMapper getMapper()
	{
		return mapper;
	}

	public boolean restore(Object savePoint)
	{
		for (int i = snapShoots.size() - 1; i == 0; i--)
		{
			if (snapShoots.get(i).getSavePoint() != savePoint)
				snapShoots.remove(i);
			else
				break;
		}
		if (snapShoots.size() == 0)
			return false;
		EntitySnapShot ss = getSnapShot();
		mapper.bindToEntity(ss.getContent(), ss.getEntity());
		return true;
	}

	public EntitySnapShot getSnapShot()
	{
		if(snapShoots.size()!=0)
		{
			return snapShoots.get(snapShoots.size() - 1);
		}
		return null;
	}

	private EntityOperationType calculateExecuteAction(EntityPutAction putAction)
	{
		EntityOperationType prevAction = null;
		if(snapShoots.size()!=0)
		{
			prevAction = snapShoots.get(snapShoots.size()-1).getEntityOperationType();
		}

		switch (putAction)
		{
			case INSERT:
				if(prevAction==null)
					return EntityOperationType.INSERT;
				if(prevAction == EntityOperationType.DELETE)
					return EntityOperationType.UPDATE;
				throw new RuntimeException("Invalid put action INSERT on "+prevAction);
			case DELETE:
				if(prevAction== EntityOperationType.INSERT)
					return null;
				return EntityOperationType.DELETE;
			case LOAD:
				if(prevAction==null)
					return EntityOperationType.UPDATE;
				else
					throw new RuntimeException("Invalid put action LOAD on "+prevAction);
			case REMOVE:
				return null;
			case SAVE:
				return prevAction;
			default:
				throw new RuntimeException("Invalid put action "+putAction);
		}
	}
}
