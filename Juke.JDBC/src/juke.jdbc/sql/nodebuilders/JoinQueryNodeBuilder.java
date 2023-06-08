package juke.jdbc.sql.nodebuilders;

import juke.jdbc.sql.LinkManager;
import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.NodePartType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.QueryElementType;

import juke.querying.fields.Field;
import juke.querying.queries.JoinQuery;
import juke.querying.queries.Source;

import java.util.ArrayList;
import java.util.List;

public class JoinQueryNodeBuilder extends QueryNodeBuilder<JoinQuery>
{

    boolean isQuery(JoinQuery jq)
    {
        if(jq.getParentElement()==null)
            return true;
        if (jq.getParentElement().getElementType()==QueryElementType.SUBQUERY_FIELD)
            return true;

        if(jq.getOffset()>0)
            return true;
        if(jq.getLimit()>0)
            return true;
        if(!jq.getSortOrders().isEmpty())
            return true;
        if(jq.getCondition()!=null)
            return true;

        return false;
    }

    @Override
    public QueryNode build(JoinQuery element, List<NameTarget> nameSpace)
    {
        if(isQuery(element))
        {
            QueryNode result = super.build(element, nameSpace);

            if (element.getFields().isEmpty())
            {
                LinkManager lm = getQueryBuilder().getLinkManager();
                List<NameTarget> nts = new ArrayList<>();
                nts.addAll(lm.getAvailableTargets(element.getLeftSource()));
                nts.addAll(lm.getAvailableTargets(element.getRightSource()));

                for (NameTarget nt : nts)
                {
                    QueryNode qnField = new QueryNode(NodeType.LINK_FIELD);

                    Source s = nt.getSource();
                    if (s.getAlias() != null)
                        qnField.putValue(ValuePartType.F_SOURCE_ALIAS, s.getAlias());
                    qnField.putValue(ValuePartType.F_NAME, nt.getStorageFieldName());
                    result.addNode(NodePartType.Q_SELECT_FIELD, qnField);
                }
            }
            else
            {
                for (Field f : element.getFields())
                    result.addNode(NodePartType.Q_SELECT_FIELD, getQueryBuilder().buildElement(f, nameSpace));
            }
            result.addNode(NodePartType.Q_FROM_SOURCE, buildJoin(element, nameSpace));
            return result;
        }
        else
            return buildJoin(element,nameSpace);
    }

    QueryNode buildJoin(JoinQuery joinQuery, List<NameTarget> nameSpace)
    {
        QueryNode joinNode = new QueryNode(NodeType.JOIN);
        joinNode.putValue(ValuePartType.J_TYPE, joinQuery.getJoinType());

        QueryNode leftSourceNode = getQueryBuilder().buildElement(joinQuery.getLeftSource(), nameSpace);
        joinNode.addNode(NodePartType.J_LEFT_SOURCE, leftSourceNode);

        QueryNode rightSourceNode = getQueryBuilder().buildElement(joinQuery.getRightSource(), nameSpace);
        joinNode.addNode(NodePartType.J_RIGHT_SOURCE, rightSourceNode);

        if(joinQuery.getOnCondition()!=null)
        {
            QueryNode onNode = getQueryBuilder().buildElement(joinQuery.getOnCondition(), nameSpace);
            joinNode.addNode(NodePartType.J_CONDITION, onNode);
        }
        return joinNode;
    }

    @Override
    public QueryElementType getQueryElementType()
    {
        return QueryElementType.JOIN_QUERY;
    }
}
