package juke.postgresql.sqlBuilding;

import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;

public class LinkFieldSqlBuilder extends ElementSqlBuilder
{
    @Override
    public String build(QueryNode node)
    {
        StringBuilder sb = new StringBuilder();
        Object oSourceAlias = node.getValue(ValuePartType.F_SOURCE_ALIAS);
        if(oSourceAlias!=null)
        {
            sb.append(oSourceAlias);
            sb.append(".");
        }
        sb.append(node.getValue(ValuePartType.F_NAME));

        Object oAlias = node.getValue(ValuePartType.F_ALIAS);
        if(oAlias!=null)
        {
            sb.append(" AS ");
            sb.append(oAlias);
        }

        return sb.toString();
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.LINK_FIELD;
    }
}
