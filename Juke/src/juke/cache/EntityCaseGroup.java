package juke.cache;


import juke.mapping.EntityKey;
import juke.mapping.EntityMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 01.10.12
 * Time: 17:58
 * To change this template use File | Settings | File Templates.
 */
public class EntityCaseGroup
{
	private Map<EntityKey, EntityCase> keyMap = new HashMap<>();
	private Map<Object, EntityCase> linkMap = new HashMap<>();
	private List<EntityCase> cases = new ArrayList<>();
	private EntityMapper mapper;



	public EntityCaseGroup(EntityMapper mapper)
	{
		this.mapper = mapper;
	}

	public EntityCase getByEntity(Object entity)
	{
		return linkMap.get(entity);
	}

	public EntityCase getByKey(EntityKey key)
	{
		return keyMap.get(key);
	}

	public List<EntityCase> getCases()
	{
		return cases;
	}

	public void put(EntityCase eCase)
	{
		EntitySnapShot ess = eCase.getSnapShot();
		keyMap.put(ess.getEntityKey(), eCase);
		linkMap.put(ess.getEntityKey(), eCase);
		cases.add(eCase);
	}

	public void remove(EntityCase eCase)
	{
		EntitySnapShot ess = eCase.getSnapShot();
		keyMap.remove(ess.getEntityKey());
		linkMap.remove(ess.getEntity());
		cases.remove(eCase);
	}

	public void clear()
	{
		keyMap.clear();
		linkMap.clear();
		cases.clear();
	}
}
