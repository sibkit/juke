package juke.postgresql.sqlBuilding;

import juke.exceptions.JukeException;
import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.conditions.ConditionType;

import java.util.List;

public class ConditionSqlBuilder extends ElementSqlBuilder
{
    @Override
    public String build(QueryNode node)
    {
        ConditionType ct = (ConditionType) node.getValue(ValuePartType.C_TYPE);

        switch (ct)
        {
            case EQUALS:
                return buildLeftRightCondition(node, "=");
            case LESS:
                return buildLeftRightCondition(node, "<");
            case LESS_OR_EQUALS:
                return buildLeftRightCondition(node, "<=");
            case GREATER:
                return buildLeftRightCondition(node, ">");
            case GREATER_OR_EQUALS:
                return buildLeftRightCondition(node, ">=");
            case NOT_EQUALS:
                return buildLeftRightCondition(node, "<>");
            case LIKE:
                return buildLeftRightCondition(node, " LIKE ");
            case AND:
                return buildMultiCondition(node, " AND ");
            case OR:
                return buildMultiCondition(node, " OR ");
            case NOT:
                return "NOT " + buildNode(node.getNodesByType(NodePartType.C_CONDITION).get(0));
            case IN:
                return buildInCondition(node);
            default:
                throw new JukeException("Unknown condition type");
        }
    }

    String buildInCondition(QueryNode node)
    {
        List<QueryNode> fields = node.getNodesByType(NodePartType.C_FIELD);

        StringBuilder sb = new StringBuilder();
        sb.append(buildNode(fields.get(0)));
        sb.append(" IN ");

        for(int i=1;i<fields.size();i++)
        {
            sb.append(buildNode(fields.get(i)));
            if(i<fields.size()-1)
                sb.append(",");
        }
        return sb.toString();
    }

    String buildLeftRightCondition(QueryNode node, String sign)
    {
        List<QueryNode> fields = node.getNodesByType(NodePartType.C_FIELD);
        if(fields.size()!=2)
            throw new JukeException("ConditionSqlBuilder : buildLeftRightCondition");

        StringBuilder sb = new StringBuilder();
        sb.append(buildNode(fields.get(0)));
        sb.append(sign);
        sb.append(buildNode(fields.get(1)));
        return sb.toString();
    }

    String buildMultiCondition(QueryNode node, String sign)
    {
        StringBuilder sb = new StringBuilder();

        List<QueryNode> conditions = node.getNodesByType(NodePartType.C_CONDITION);
        for(int i=0;i<conditions.size();i++)
        {
            sb.append(buildNode(conditions.get(i)));
            if(i<conditions.size()-1)
                sb.append(sign);
        }
        return sb.toString();
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.CONDITION;
    }
}
