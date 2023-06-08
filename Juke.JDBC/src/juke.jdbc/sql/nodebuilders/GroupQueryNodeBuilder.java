package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.QueryNode;
import juke.querying.QueryElementType;
import juke.querying.queries.GroupQuery;

import java.util.List;

public class GroupQueryNodeBuilder extends NodeBuilder<GroupQuery>
{
    @Override
    public QueryNode build(GroupQuery element, List<NameTarget> nameSpace)
    {
        return null;
    }

    @Override
    public QueryElementType getQueryElementType()
    {
        return null;
    }
}
