package juke.postgresql.sqlBuilding;

import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlBuildManager
{
    Map<NodeType,ElementSqlBuilder> buildersMap = new HashMap<>();

    public SqlBuildManager()
    {
        List<ElementSqlBuilder> elementBuilders = new ArrayList<>();
        elementBuilders.add(new QuerySqlBuilder());
        elementBuilders.add(new LinkFieldSqlBuilder());
        elementBuilders.add(new TableSqlBuilder());
        elementBuilders.add(new ConditionSqlBuilder());
        elementBuilders.add(new JoinSqlBuilder());

        for(ElementSqlBuilder esb : elementBuilders)
        {
            buildersMap.put(esb.getNodeType(),esb);
            esb.initialize(this);
        }
    }

    public String build(QueryNode node)
    {
        ElementSqlBuilder elementBuilder = buildersMap.get(node.getType());
        return elementBuilder.build(node);
    }
}
