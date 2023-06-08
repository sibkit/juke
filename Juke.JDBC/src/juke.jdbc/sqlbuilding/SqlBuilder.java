package juke.jdbc.sqlbuilding;

import juke.jdbc.sql.node.QueryNode;
import juke.storage.operation.EntityOperation;
import juke.storage.sequence.SequenceOperation;

public interface SqlBuilder
{
    String build(QueryNode queryNode);
    String build(SequenceOperation operation);
    SqlBuildResult build(EntityOperation operation);
    String buildSetSchemeCommand(String schemeName);
}
