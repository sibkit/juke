package juke.postgresql.sqlBuilding;

import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;

public class TableSqlBuilder extends ElementSqlBuilder
{
    @Override
    public String build(QueryNode node)
    {
        String result = node.getValue(ValuePartType.T_NAME).toString();
        String alias = (String)node.getValue(ValuePartType.S_ALIAS);
        if(alias!=null)
            result+=" AS "+alias;
        return result;
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.TABLE;
    }
}
