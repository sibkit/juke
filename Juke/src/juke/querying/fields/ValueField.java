package juke.querying.fields;

import juke.querying.QueryElementType;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 01.02.12
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
public class ValueField extends Field
{
    private Object value;

    public ValueField(Object value)
    {
        setValue(value);
    }

    public Object getValue()
    {
        return value;
    }
    public void setValue(Object value)
    {
        this.value = value;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.VALUE_FIELD;
    }
}
