package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.QueryBuilder;
import juke.jdbc.sql.node.QueryNode;
import juke.querying.QueryElement;
import juke.querying.QueryElementType;
import juke.jdbc.sql.NameTarget;

import java.util.List;

public abstract class NodeBuilder<T extends QueryElement>
{
    private QueryBuilder queryBuilder;
    public void initialize(QueryBuilder queryBuilder)
    {
        this.queryBuilder = queryBuilder;
    }

    public abstract QueryNode build(T element, List<NameTarget> nameSpace);
    public abstract QueryElementType getQueryElementType();

    public QueryBuilder getQueryBuilder()
    {
        return queryBuilder;
    }
}
