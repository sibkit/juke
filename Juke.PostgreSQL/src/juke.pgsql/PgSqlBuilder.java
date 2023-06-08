package juke.postgresql;

import juke.exceptions.JukeException;
import juke.postgresql.sqlBuilding.SqlBuildManager;
import juke.jdbc.sqlbuilding.SqlBuildResult;
import juke.jdbc.sqlbuilding.SqlBuilder;
import juke.jdbc.sql.node.QueryNode;
import juke.mapping.EntityContent;
import juke.mapping.EntityMap;
import juke.mapping.FieldMap;
import juke.storage.operation.EntityOperation;
import juke.storage.sequence.SequenceOperation;

public class PgSqlBuilder implements SqlBuilder
{
    SqlBuildManager buildManager = new SqlBuildManager();

    @Override
    public String build(QueryNode queryNode)
    {
        return buildManager.build(queryNode);
    }

    @Override
    public String build(SequenceOperation operation)
    {
        String result = "select ";
        switch (operation.getOperationType())
        {
            case CURRENT_VALUE:
                result+="currval(";
            case NEXT_VALUE:
                result+="nextval(";
        }
        result+="'"+operation.getSequenceMap().getStorageSequenceName()+"'";
        result+=")";
        return result;
    }

    @Override
    public SqlBuildResult build(EntityOperation operation)
    {
        switch (operation.getOperationType())
        {
            case INSERT:
                return buildInsertCommand(operation.getNewContent());
            case UPDATE:
                return buildUpdateCommand(operation.getNewContent(), operation.getOldContent());
            case DELETE:
                return buildDeleteCommand(operation.getNewContent());
            default:
                throw new JukeException("Invalid operation command: "+ operation.getOperationType().toString());
        }
    }

    @Override
    public String buildSetSchemeCommand(String schemeName)
    {
        return "SET search_path = "+schemeName+";";
    }


    SqlBuildResult buildInsertCommand(EntityContent content)
    {
        EntityMap map  = content.getEntityMap();
        SqlBuildResult result = new SqlBuildResult();
        String sql = "INSERT INTO "+map.getTableName()+" (";
        for(int i=0;i<map.getFieldMaps().length;i++)
        {
            sql+=map.getFieldMaps()[i].getColumnName();
            if(i<map.getFieldMaps().length-1) {
                sql+=", ";
            }
        }

        sql+=") VALUES (";

        for(int i=0;i<map.getFieldMaps().length;i++)
        {
            sql += "?";
            result.getParameters().add(content.getFieldValue(i));
            if(i<map.getFieldMaps().length-1) {
                sql+=", ";
            }
        }
        sql+=")";
        result.setSqlString(sql);
        return result;
    }

    SqlBuildResult buildUpdateCommand(EntityContent newContent, EntityContent oldContent)
    {
        EntityMap map  = newContent.getEntityMap();
        SqlBuildResult result = new SqlBuildResult();
        String sql = "UPDATE "+map.getTableName()+" SET ";

        for(int i=0;i<map.getFieldMaps().length;i++)
        {
            FieldMap fm = map.getFieldMaps()[i];
            Object ov = null;
            if(oldContent!=null)
                ov = oldContent.getFieldValue(i);
            Object nv = newContent.getFieldValue(i);

            if(ov!=nv)
            {
                sql += fm.getColumnName();
                sql += "=";
                sql += "?";//buildValue(fm.getStorageDataType(),nv);
                sql += ", ";
                if(oldContent!=null)
                    result.getParameters().add(oldContent.getFieldValue(fm.getIndex()));
                else
                    result.getParameters().add(newContent.getFieldValue(fm.getIndex()));
            }
        }

        sql = sql.substring(0,sql.length()-2);

        sql+=" WHERE ";
        int[] keyIndexes = map.getKeyIndexes();
        for(int i=0;i<keyIndexes.length;i++)
        {
            FieldMap fm = map.getFieldMaps()[keyIndexes[i]];
            sql+=fm.getColumnName();
            sql+="=";
            sql+="?";//buildValue(fm.getStorageDataType(),newContent.getFieldValue(i));
            if(oldContent!=null)
                result.getParameters().add(oldContent.getFieldValue(fm.getIndex()));
            else
                result.getParameters().add(newContent.getFieldValue(fm.getIndex()));
            if(i<keyIndexes.length-1) {
                sql+=" AND ";
            }
        }
        result.setSqlString(sql);
        return result;
    }

    SqlBuildResult buildDeleteCommand(EntityContent content)
    {
        EntityMap map  = content.getEntityMap();
        SqlBuildResult result = new SqlBuildResult();
        String sql = "DELETE FROM "+map.getTableName()+" WHERE ";

        int[] keyIndexes = map.getKeyIndexes();
        for(int i=0;i<keyIndexes.length;i++)
        {
            FieldMap fm = map.getFieldMaps()[keyIndexes[i]];
            sql+=fm.getColumnName();
            sql+="=";
            sql+="?";//buildValue(fm.getStorageDataType(),content.getFieldValue(i));
            result.getParameters().add(content.getFieldValue(fm.getIndex()));
            if(i<keyIndexes.length-1) {
                sql+=" AND ";
            }
        }
        result.setSqlString(sql);
        return result;
    }
}
