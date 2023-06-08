package juke.querying.queries;

import sibutils.common.ChildList;
import juke.querying.QueryElement;
import juke.querying.conditions.Condition;
import juke.querying.fields.Field;
import juke.querying.sorting.SortOrder;

import java.io.*;

import java.util.List;


public abstract class Query extends QueryElement
{
	private final List<Field> fields = new ChildList<Query,Field>(this);
	private int offset;
	private int limit;
	private Condition condition;
	private final List<SortOrder> sortOrders = new ChildList<Query,SortOrder>(this);

	public List<Field> getFields()
	{
		return fields;
	}

	public int getOffset()
	{
		return offset;
	}
	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getLimit()
	{
		return limit;
	}
	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public Condition getCondition()
	{
		return condition;
	}
	public void setCondition(Condition condition)
	{
		this.condition = prepareChild(this.condition, condition);
	}

	public List<SortOrder> getSortOrders()
	{
		return sortOrders;
	}

	public Query clone()
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (Query) ois.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
