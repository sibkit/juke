package juke.jdbc.sql.node;

public enum NodePartType
{
    Q_SELECT_FIELD,
    Q_FROM_SOURCE,
    Q_WHERE_CONDITION,
    Q_HAVING_CONDITION,

    J_LEFT_SOURCE, J_RIGHT_SOURCE,
    J_CONDITION,

    S_JOIN_SOURCE,
    S_JOIN_CONDITION,

    Q_GROUP_BY_FIELD,
    Q_SORT_ORDER,

    C_CONDITION,
    C_FIELD,

    F_FIELD,

    SO_FIELD //sort order field
}
