package juke.querying.queries;

import juke.querying.QueryElement;
import juke.querying.QueryElementType;

public class Source extends QueryElement
{
    private Query query;
    private String alias;

    public Source(Query query)
    {
        setQuery(query);
    }

    public Source(Query query, String alias)
    {
        this(query);
        this.alias = alias;
    }

    public Query getQuery()
    {
        return query;
    }

    public void setQuery(Query query)
    {
        this.query = prepareChild(this.query,query);
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(String alias)
    {
        this.alias = alias;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.SOURCE;
    }
}
