package juke.jdbc;

import juke.exceptions.JukeException;
import juke.querying.queries.Query;
import juke.storage.ResultSetHandler;
import juke.storage.StorageIterator;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcStorageIterator implements StorageIterator
{
    ResultSet resultSet;
    ResultSetHandler handler;

    public JdbcStorageIterator(ResultSet resultSet, ResultSetHandler handler)
    {
        this.resultSet = resultSet;
        this.handler = handler;
    }

    boolean isEmptyChecked = false;
    boolean isEmpty = true;
    boolean isMustIgnoreNext =true;

    @Override
    public boolean hasNext() throws JukeException
    {
        try
        {
            //check for empty
            if(!isEmptyChecked)
            {
                if(resultSet.next())
                {
                    isEmpty=false;
                    isEmptyChecked =true;
                    return true;
                }
                else
                {
                    isEmpty=true;
                    isEmptyChecked =true;
                }
            }

            if(resultSet.isLast()) return false;
            if(isEmpty) return false;

            return true;
        }
        catch (SQLException e)
        {
            throw new JukeException(e.getMessage());
        }
    }

    @Override
    public Object[] next() throws JukeException
    {
        try
        {
            if(!isEmptyChecked)
            {
                throw new JukeException("call hasNext() first");
            }

            if(isMustIgnoreNext)
            {
                isMustIgnoreNext =false;
            }
            else
            {
                resultSet.next();
            }

            Object[] items = handler.handleRow(resultSet);
            return items;
        }
        catch (JukeException | SQLException ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }

    @Override
    public Query getQuery()
    {
        return handler.getQuery();
    }
}
