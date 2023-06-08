package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.QueryElementType;
import juke.querying.conditions.Condition;
import juke.querying.fields.Field;

import java.util.List;

public class ConditionNodeBuilder extends NodeBuilder<Condition>
{
    @Override
    public QueryNode build(Condition element, List<NameTarget> nameSpace)
    {
        QueryNode result = new QueryNode(NodeType.CONDITION);
        result.putValue(ValuePartType.C_TYPE, element.getConditionType());
        for(Condition c: element.getInnerConditions())
            result.addNode(NodePartType.C_CONDITION,getQueryBuilder().buildElement(c,nameSpace));
        for(Field f: element.getFields())
            result.addNode(NodePartType.C_FIELD,getQueryBuilder().buildElement(f,nameSpace));
        return result;
    }

    @Override
    public QueryElementType getQueryElementType()
    {
        return QueryElementType.CONDITION;
    }
}
