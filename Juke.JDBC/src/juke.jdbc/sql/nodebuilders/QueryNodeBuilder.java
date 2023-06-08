package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.queries.Query;
import juke.querying.sorting.SortOrder;

import java.util.List;

public abstract class QueryNodeBuilder<T extends Query> extends NodeBuilder<T>
{
    public QueryNode build(T query, List<NameTarget> nameSpace)
    {
        QueryNode result = new QueryNode(NodeType.QUERY);
        if (query.getLimit() > 0)
            result.putValue(ValuePartType.Q_LIMIT,query.getLimit());

        if (query.getOffset() > 0)
            result.putValue(ValuePartType.Q_OFFSET,query.getOffset());

        if (query.getSortOrders().size() > 0)
        {
            for (SortOrder so : query.getSortOrders())
            {
                QueryNode snSortOrder = new QueryNode(NodeType.SORT_ORDER);
                snSortOrder.addNode(NodePartType.SO_FIELD,getQueryBuilder().buildElement(so.getField(), nameSpace));
                snSortOrder.putValue(ValuePartType.SO_DIRECTION,so.getOrderDirection().toString());

                result.addNode(NodePartType.Q_SORT_ORDER,snSortOrder);
            }
        }



        return result;
    }


}
