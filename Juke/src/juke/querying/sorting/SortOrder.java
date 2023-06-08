package juke.querying.sorting;

import juke.querying.QueryElement;
import juke.querying.QueryElementType;
import juke.querying.fields.Field;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 17.01.12
 * Time: 1:07
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public class SortOrder extends QueryElement
{
	public SortOrder(){}

	public SortOrder(Field field, SortOrderDirection direction)
	{
		setField(field);
		setOrderDirection(direction);
	}

     private Field field;

    public Field getField()
    {
        return field;
    }

    public void setField(Field field)
    {
        this.field = field;
	    field.setParent(this);
    }

    private SortOrderDirection sortOrderDirection;

    public SortOrderDirection getOrderDirection()
    {
        return sortOrderDirection;
    }

    public void setOrderDirection(SortOrderDirection sortOrderDirection)
    {
        this.sortOrderDirection = sortOrderDirection;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.SORT_ORDER;
    }
}
