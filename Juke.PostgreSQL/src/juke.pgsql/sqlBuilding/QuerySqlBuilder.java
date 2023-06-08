package juke.postgresql.sqlBuilding;

import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;

import java.util.List;

public class QuerySqlBuilder extends ElementSqlBuilder
{
    @Override
    public String build(QueryNode node)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        List<QueryNode> selectFields = node.getNodesByType(NodePartType.Q_SELECT_FIELD);

        if(selectFields==null || selectFields.isEmpty())
        {
            sb.append("*");
        }
        else
        {
            sb.append(buildNodes(selectFields, ", "));
        }
        sb.append(" FROM ");

        QueryNode fromNode = node.getNode(NodePartType.Q_FROM_SOURCE);
        sb.append(buildNode(fromNode));

        /*if(fromNode.getType()==NodeType.TABLE)
            sb.append(buildNode(fromNode));
        else
        {
            sb.append("(");
            sb.append(buildNode(fromNode));
            sb.append(")");
        }*/
        List<QueryNode> groupByFields = node.getNodesByType(NodePartType.Q_GROUP_BY_FIELD);
        if(groupByFields!=null && groupByFields.size()>0)
        {
            sb.append(" GROUP BY");
            buildNodes(groupByFields,", ");
        }


        QueryNode whereNode = node.getNode(NodePartType.Q_WHERE_CONDITION);
        if(whereNode!=null)
        {
            sb.append(" WHERE ");
            sb.append(getBuildManager().build(whereNode));
        }

        List<QueryNode> sortOrderNodes = node.getNodesByType(NodePartType.Q_SORT_ORDER);
        if(sortOrderNodes!=null && sortOrderNodes.size()>0)
        {
            sb.append(" ORDER BY ");
            sb.append(buildNodes(sortOrderNodes,", "));
        }


        return sb.toString();
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.QUERY;
    }
}
