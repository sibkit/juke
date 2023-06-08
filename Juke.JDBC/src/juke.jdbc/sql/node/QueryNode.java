package juke.jdbc.sql.node;

import sibutils.common.ChargingList;
import juke.exceptions.JukeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryNode
{
    QueryNode thisNode = this;

    private QueryNode parentNode;
    private NodeType type;

    private Map<NodePartType,List<QueryNode>> childByTypeMap = new HashMap<>();
    private Map<ValuePartType,Object> valueByTypeMap = new HashMap<>();

    private List<QueryNode> childNodes = new ChargingList<QueryNode>()
    {
        @Override
        protected void charge(QueryNode element)
        {
            element.parentNode = thisNode;
            if(childByTypeMap.containsKey(element.type))
                childByTypeMap.get(element.type).add(element);
        }

        @Override
        protected void discharge(QueryNode element)
        {
            element.parentNode=null;
            List<QueryNode> typeNodes = childByTypeMap.get(element.type);
            if(typeNodes.size()>1)
                typeNodes.remove(element);
            else
                childByTypeMap.remove(element.type);
        }
    };

    public QueryNode(NodeType type)
    {
        this.type = type;
    }

    public void putValue(ValuePartType partType, Object value)
    {
        valueByTypeMap.put(partType,value);
    }

    public void addNode(NodePartType partType, QueryNode node)
    {
        List<QueryNode> nodes = childByTypeMap.get(partType);
        if(nodes==null)
        {
            nodes = new ArrayList<>();
            childByTypeMap.put(partType,nodes);
        }
        nodes.add(node);
    }

    public Object getValue(ValuePartType partType)
    {
        return valueByTypeMap.get(partType);
    }

    public List<QueryNode> getNodesByType(NodePartType partType)
    {
        return childByTypeMap.get(partType);
    }

    public QueryNode getNode(NodePartType partType)
    {
        List<QueryNode> nodes = childByTypeMap.get(partType);
        if(nodes==null)
            return null;
        if(nodes.size()==1)
            return nodes.get(0);
        throw new JukeException("Nodes with part type: '"+partType+ "' more then one");
    }

    public NodeType getType()
    {
        return type;
    }

    public QueryNode getParentNode()
    {
        return parentNode;
    }
}
