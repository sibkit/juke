package juke.querying.queries;

import juke.querying.QueryElementType;
import juke.querying.fields.Field;
import sibutils.common.ChildList;

import java.util.List;

public class GroupQuery extends Query
{
    private List<Field> groupFields = new ChildList<GroupQuery,Field>(this);
    private Source source;

    public List<Field> getGroupFields()
    {
        return groupFields;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.GROUP_QUERY;
    }

    public Source getSource()
    {
        return source;
    }
    public void setSource(Source source)
    {
        this.source = source;
        source.setParent(this);
    }
}
