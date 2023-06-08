package juke.jdbc;

import sibutils.events.EventEmitter;
import sibutils.events.BaseEventEmitter;
import juke.exceptions.JukeException;
import juke.jdbc.sql.node.QueryNode;
import juke.jdbc.sql.QueryBuilder;
import juke.jdbc.sqlbuilding.SqlBuildResult;
import juke.mapping.SequenceMap;
import juke.querying.queries.Query;
import juke.storage.Connection;
import juke.storage.ResultSetHandler;
import juke.storage.StorageIterator;
import juke.storage.operation.EntityOperation;
import juke.storage.operation.EntityOperationType;
import juke.storage.sequence.Sequence;
import juke.storage.sequence.SequenceOperation;
import juke.storage.transaction.Transaction;

import java.sql.*;
import java.util.List;

public class JdbcConnection implements Connection
{
    private java.sql.Connection jdbcConnection;
    private JdbcDriver driver;

    private BaseEventEmitter beforeOperationExecuteEmitter = new BaseEventEmitter();
    private BaseEventEmitter afterOperationExecuteEmitter = new BaseEventEmitter();

    public JdbcConnection(JdbcDriver driver) throws JukeException
    {
        try
        {
            this.jdbcConnection = DriverManager.getConnection(driver.getUrl(), driver.getUser(), driver.getPassword());
            this.driver = driver;
            rsHandler = new ResultSetHandler(driver.getMappingData());
        }
        catch (Exception ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }

    java.sql.Connection getJdbcConnection()
    {
        return jdbcConnection;
    }

    @Override
    public EventEmitter getAfterOperationExecuteEmitter()
    {
        return afterOperationExecuteEmitter;
    }

    @Override
    public EventEmitter getBeforeOperationExecuteEmitter()
    {
        return beforeOperationExecuteEmitter;
    }

    @Override
    public void close() throws JukeException
    {
        try
        {
            this.jdbcConnection.close();
        }
        catch(Exception ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }

    @Override
    public boolean isClosed() throws JukeException
    {
        try
        {
            return this.jdbcConnection.isClosed();
        }
        catch (SQLException e)
        {
            throw new JukeException(e.getMessage());
        }
    }

    @Override
    public Transaction getCurrentTransaction()
    {
        return transaction;  //To change body of implemented methods use File | Settings | File Templates.
    }

    JdbcTransaction transaction = null;

    @Override
    public Transaction beginTransaction() throws JukeException
    {
        transaction = new JdbcTransaction(this);
        transaction.begin();
        return transaction;
    }

    @Override
    public Sequence getSequence(SequenceMap map) throws JukeException
    {
        return new JdbcSequence(this, map);
    }

    public <T extends Number> T executeSequenceOperation(SequenceOperation sequenceOperation) throws JukeException
    {
        try
        {
            String operationString = driver.getSqlBuilder().build(sequenceOperation);
            Statement st = this.jdbcConnection.createStatement();
            ResultSet rs = st.executeQuery(operationString);
            rs.next();
            return (T)rs.getObject(1);
        }
        catch (Exception ex)
        {
            if(ex.getClass()==JukeException.class)
            {
                throw (JukeException) ex;
            }
            throw new JukeException(ex.getMessage());
        }
    }

    @Override
    public void executeOperation(EntityOperation operation) throws JukeException
    {
        try
        {
            if(operation.getNewContent().equals(operation.getOldContent()) && operation.getOperationType() == EntityOperationType.UPDATE)
                return;
            SqlBuildResult br = driver.getSqlBuilder().build(operation);

            if(br.getParameters().size()>0)
            {
                PreparedStatement pst = this.jdbcConnection.prepareStatement(br.getSqlString());
                for(int i=0;i<br.getParameters().size();i++)
                pst.setObject(i+1,br.getParameters().get(i));
                pst.execute();
            }
            else
            {
                Statement st = this.jdbcConnection.createStatement();

                System.out.println(br.getSqlString());
                st.execute(br.getSqlString());
            }

            //String operationString = driver.getSqlBuilder().build(operation);

        }
        catch (Exception ex)
        {
            if(ex.getClass()==JukeException.class)
                throw (JukeException)ex;
            throw new JukeException(ex.getMessage());
        }
    }

    @Override
    public void executeOperations(List<EntityOperation> operations) throws JukeException
    {
        for(EntityOperation eo : operations)
            executeOperation(eo);
    }

    private ResultSetHandler rsHandler;

    boolean isSchemeNameChecked=false;


    @Override
    public StorageIterator iterate(Query query) throws JukeException
    {
        try
        {
            QueryBuilder queryBuilder = new QueryBuilder(query,driver.getMappingData());
            QueryNode node = queryBuilder.build();
            String buildResult = driver.getSqlBuilder().build(node);

            if(!isSchemeNameChecked)
            {
                if(driver.getSchemeName()!=null)
                {
                    Statement snst = this.jdbcConnection.createStatement();
                    snst.execute(driver.getSqlBuilder().buildSetSchemeCommand(driver.getSchemeName()));
                }
                isSchemeNameChecked=true;
            }

            Statement st = this.jdbcConnection.createStatement();
            ResultSet rs = st.executeQuery(buildResult);
            rsHandler.setQuery(query);
            return new JdbcStorageIterator(rs,rsHandler);
        }
        catch (Exception ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }


}