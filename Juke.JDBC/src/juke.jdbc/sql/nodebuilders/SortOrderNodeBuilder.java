package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.QueryElementType;
import juke.querying.sorting.SortOrder;
import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.QueryNode;

import java.util.List;

public class SortOrderNodeBuilder extends NodeBuilder<SortOrder>
{
    @Override
    public QueryNode build(SortOrder element, List<NameTarget> nameSpace)
    {
        QueryNode snSortOrder = new QueryNode(NodeType.SORT_ORDER);
        snSortOrder.addNode(NodePartType.SO_FIELD,getQueryBuilder().buildElement(element.getField(),nameSpace));
        snSortOrder.putValue(ValuePartType.SO_DIRECTION,element.getOrderDirection().toString());
        return snSortOrder;
    }

    @Override
    public QueryElementType getQueryElementType()
    {
        return QueryElementType.SORT_ORDER;
    }
}
