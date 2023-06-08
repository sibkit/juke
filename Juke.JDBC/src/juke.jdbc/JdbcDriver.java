package juke.jdbc;

import juke.exceptions.JukeException;
import juke.MappingData;
import juke.jdbc.sqlbuilding.SqlBuilder;
import juke.storage.Connection;
import juke.storage.StorageDriver;

public class JdbcDriver implements StorageDriver
{
    private MappingData mappingData;
    private String url;
    private String user;
    private String password;
    private String schemeName;
    private SqlBuilder sqlBuilder = null;

    @Override
    public void initialize(MappingData mappingData)
    {
        this.mappingData = mappingData;
    }

    @Override
    public Connection createConnection() throws JukeException
    {
        JdbcConnection result = new JdbcConnection(this);
        return result;
    }

    public String getUrl()
    {
        return url;
    }
    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUser()
    {
        return user;
    }
    public void setUser(String user)
    {
        this.user = user;
    }

    public String getPassword()
    {
        return password;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    public SqlBuilder getSqlBuilder()
    {
        return sqlBuilder;
    }
    public void setSqlBuilder(SqlBuilder sqlBuilder)
    {
        this.sqlBuilder = sqlBuilder;
    }

    public MappingData getMappingData()
    {
        return mappingData;
    }

    public String getSchemeName()
    {
        return schemeName;
    }
    public void setSchemeName(String schemeName)
    {
        this.schemeName = schemeName;
    }
}