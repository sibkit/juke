package juke.jdbc.sqlbuilding;

import java.util.ArrayList;
import java.util.List;

public class SqlBuildResult
{
    private String sqlString;
    private List parameters = new ArrayList();

    public String getSqlString()
    {
        return sqlString;
    }

    public void setSqlString(String sqlString)
    {
        this.sqlString = sqlString;
    }

    public List getParameters()
    {
        return parameters;
    }
}
