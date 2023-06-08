package juke.jdbc.sql.node;

public enum NodeType
{
    QUERY,
    TABLE,
    JOIN,

    SORT_ORDER,

    LINK_FIELD,
    SUBQUERY_FIELD,
    FUNCTION_FIELD,

    CONDITION,
}
