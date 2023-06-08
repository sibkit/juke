package juke.storage.operation;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 13.01.12
 * Time: 9:59
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public class ExecuteActionEvent extends EventObject
{
	private EntityOperation operation;

    public ExecuteActionEvent(Object source, EntityOperation operation)
    {
        super(source);
    }

	public EntityOperation getOperation()
	{
		return operation;
	}
}
