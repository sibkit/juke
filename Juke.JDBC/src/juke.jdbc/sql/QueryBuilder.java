package juke.jdbc.sql;

import juke.exceptions.JukeException;
import juke.MappingData;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.nodebuilders.*;
import juke.querying.QueryElement;
import juke.querying.QueryElementType;
import juke.querying.queries.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder
{
    private final Query mainQuery;
    private final MappingData mappingData;

    private final LinkManager linkManager;
    Map<QueryElementType, NodeBuilder<?>> nodeBuilders = new HashMap<>();

    private void addBuilder(NodeBuilder<?> nodeBuilder)
    {
        nodeBuilders.put(nodeBuilder.getQueryElementType(),nodeBuilder);
    }

    public QueryBuilder(Query query, MappingData mappingData)
    {
        mainQuery = query;
        this.mappingData = mappingData;
        linkManager = new LinkManager(mappingData);

        addBuilder(new JoinQueryNodeBuilder());
        addBuilder(new GroupQueryNodeBuilder());
        addBuilder(new EntityQueryNodeBuilder());
        addBuilder(new SourceNodeBuilder());

        addBuilder(new LinkFieldNodeBuilder());
        addBuilder(new EntityQueryNodeBuilder());
        addBuilder(new ConditionNodeBuilder());

        for(NodeBuilder<?> nb : nodeBuilders.values())
            nb.initialize(this);
    }

    public QueryNode build()
    {
        System.out.println("Optimization required");
        return buildElement(mainQuery, getLinkManager().getNameSpace(mainQuery));
    }

    public QueryNode buildElement(QueryElement element, List<NameTarget> nameSpace)
    {
        NodeBuilder nb = nodeBuilders.get(element.getElementType());
        if(nb==null)
            throw new JukeException("Not found nodeBuilder for element "+element.getElementType());
        return nb.build(element,nameSpace);
    }

    public MappingData getMappingData()
    {
        return mappingData;
    }

    public LinkManager getLinkManager()
    {
        return linkManager;
    }
}
