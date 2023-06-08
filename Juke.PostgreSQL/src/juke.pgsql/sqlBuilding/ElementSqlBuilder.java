package juke.postgresql.sqlBuilding;

import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;

import java.util.List;

public abstract class ElementSqlBuilder
{
    private SqlBuildManager buildManager;
    public abstract String build(QueryNode node);
    public abstract NodeType getNodeType();
    public void initialize(SqlBuildManager buildManager)
    {
        this.buildManager = buildManager;
    }

    public SqlBuildManager getBuildManager()
    {
        return buildManager;
    }

    protected String buildNode(QueryNode node)
    {
        return buildManager.build(node);
    }

    protected String buildNodes(List<QueryNode> nodes, String separator)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<nodes.size();i++)
        {
            sb.append(getBuildManager().build(nodes.get(i)));
            if(i<nodes.size()-1)
                sb.append(separator);
        }
        return sb.toString();
    }
}
