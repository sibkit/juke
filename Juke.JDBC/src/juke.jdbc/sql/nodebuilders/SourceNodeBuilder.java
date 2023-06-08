package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.QueryElementType;
import juke.querying.queries.Source;

import java.util.List;

public class SourceNodeBuilder extends NodeBuilder<Source>
{
    @Override
    public QueryNode build(Source element, List<NameTarget> nameSpace)
    {
        QueryNode result = getQueryBuilder().buildElement(element.getQuery(),nameSpace);

        if(element.getAlias()!=null && !element.getAlias().isEmpty())
            result.putValue(ValuePartType.S_ALIAS,element.getAlias());
        return result;
    }

    @Override
    public QueryElementType getQueryElementType()
    {
        return QueryElementType.SOURCE;
    }
}
