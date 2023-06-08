package juke.jdbc.sql.nodebuilders;

import juke.exceptions.JukeException;
import juke.jdbc.sql.NameTarget;
import juke.jdbc.sql.node.NodeType;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.node.ValuePartType;
import juke.querying.QueryElementType;
import juke.querying.fields.LinkField;

import java.util.List;

public class LinkFieldNodeBuilder extends NodeBuilder<LinkField>
{
    @Override
    public QueryNode build(LinkField element, List<NameTarget> nameSpace)
    {
        QueryNode result = new QueryNode(NodeType.LINK_FIELD);

        NameTarget nameTarget=null;
        for(NameTarget nt : nameSpace)
        {
            if(nt.getFieldName().equals(element.getName()) && nt.getSource().getAlias()==element.getSourceAlias())
            nameTarget=nt;
        }

        if(nameTarget==null)
            throw new JukeException("LinkFieldNodeBuilder: build (Target for field \\\""+element.getName()+"\\\" not found)");



        result.putValue(ValuePartType.F_NAME,nameTarget.getStorageFieldName());
        if(element.getSourceAlias()!=null)
        result.putValue(ValuePartType.F_SOURCE_ALIAS,element.getSourceAlias());
        result.putValue(ValuePartType.F_ALIAS,element.getAlias());
        return result;
    }

    @Override
    public QueryElementType getQueryElementType()
    {
        return QueryElementType.LINK_FIELD;
    }
}
