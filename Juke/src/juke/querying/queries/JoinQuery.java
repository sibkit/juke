package juke.querying.queries;

import juke.querying.QueryElementType;
import juke.querying.conditions.Condition;

public class JoinQuery extends Query
{
    private JoinType joinType;
    private Condition onCondition;
    private Source leftSource;
    private Source rightSource;

    public JoinQuery(JoinType joinType)
    {
        this.joinType = joinType;
    }

    @Override
    public QueryElementType getElementType()
    {
        return QueryElementType.JOIN_QUERY;
    }

    public JoinType getJoinType()
    {
        return joinType;
    }
    public void setJoinType(JoinType joinType)
    {
        this.joinType = joinType;
    }

    public Condition getOnCondition()
    {
        return onCondition;
    }
    public void setOnCondition(Condition onCondition)
    {
        this.onCondition = prepareChild(this.onCondition, onCondition);
    }

    public Source getLeftSource()
    {
        return leftSource;
    }

    public void setLeftSource(Source leftSource)
    {
        this.leftSource = prepareChild(this.leftSource,leftSource);
    }

    public Source getRightSource()
    {
        return rightSource;
    }
    public void setRightSource(Source rightSource)
    {
        this.rightSource = prepareChild(this.rightSource, rightSource);
    }
}
