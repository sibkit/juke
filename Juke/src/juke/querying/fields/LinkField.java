package juke.querying.fields;

import juke.querying.QueryElementType;

public final class LinkField extends Field
{
    private String name;
    private String sourceAlias;

    public LinkField(String name)
    {
        this.name = name;
    }

    public LinkField(String sourceAlias, String name)
    {
        this.sourceAlias = sourceAlias;
        this.name = name;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.LINK_FIELD;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getSourceAlias()
    {
        return sourceAlias;
    }
    public void setSourceAlias(String sourceAlias)
    {
        this.sourceAlias = sourceAlias;
    }
}
