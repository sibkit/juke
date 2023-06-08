package juke.postgresql.sqlBuilding;

import juke.exceptions.JukeException;
import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.queries.JoinType;

public class JoinSqlBuilder extends ElementSqlBuilder
{
    @Override
    public String build(QueryNode node)
    {
        StringBuilder sb = new StringBuilder();

        sb.append(buildNode(node.getNode(NodePartType.J_LEFT_SOURCE)));
        /*    CROSS,
    INNER,
    LEFT_OUTER,
    RIGHT_OUTER,
    FULL_OUTER*/

        String joinType;

        JoinType jt = (JoinType) node.getValue(ValuePartType.J_TYPE);

        switch (jt)
        {
            case LEFT_OUTER:
                joinType = "LEFT JOIN";
                break;
            case RIGHT_OUTER:
                joinType = "RIGHT JOIN";
                break;
            case CROSS:
                joinType = "CROSS JOIN";
                break;
            case INNER:
                joinType = "INNER JOIN";
            case FULL_OUTER:
                joinType = "FULL OUTER JOIN";
            default:
                throw new JukeException("JoinSqlBuilder: build (Unknown join type");
        }



        sb.append(" "+joinType+" ");

        sb.append(buildNode(node.getNode(NodePartType.J_RIGHT_SOURCE)));

        QueryNode onNode = node.getNode(NodePartType.J_CONDITION);
        if(onNode!=null)
        {
            sb.append(" ON ");
            sb.append(buildNode(onNode));
        }

        return sb.toString();
    }

    @Override
    public NodeType getNodeType()
    {
        return NodeType.JOIN;
    }
}
