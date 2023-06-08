package juke.storage.operation;

import juke.mapping.EntityContent;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 01.10.12
 * Time: 22:06
 * To change this template use File | Settings | File Templates.
 */
public class EntityOperation
{
	private EntityContent newContent;
	private EntityContent oldContent;
	private EntityOperationType operationType;

	public EntityOperationType getOperationType()
	{
		return operationType;
	}

	public EntityContent getNewContent()
	{
		return newContent;
	}

	public void setNewContent(EntityContent content)
	{
		this.newContent = content;
	}

	public void setOperationType(EntityOperationType operationType)
	{
		this.operationType = operationType;
	}

	public EntityContent getOldContent()
	{
		return oldContent;
	}

	public void setOldContent(EntityContent oldContent)
	{
		this.oldContent = oldContent;
	}
}
