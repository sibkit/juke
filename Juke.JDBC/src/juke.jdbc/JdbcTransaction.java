package juke.jdbc;

import sibutils.events.EventEmitter;
import sibutils.events.BaseEventEmitter;
import juke.exceptions.JukeException;
import juke.storage.Connection;
import juke.storage.operation.EntityOperation;
import juke.storage.transaction.Transaction;
import juke.storage.transaction.TransactionState;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class JdbcTransaction implements Transaction
{
    TransactionState state;

    BaseEventEmitter<EventObject> stateChangedEmitter = new BaseEventEmitter<>();

    @Override
    public TransactionState getState() {
        return state;
    }

    @Override
    public void begin() throws JukeException
    {
        try
        {
            connection.getJdbcConnection().setAutoCommit(false);
            state = TransactionState.OPENED;
            stateChangedEmitter.emit(new EventObject(this));
        }
        catch (SQLException ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }


    JdbcConnection connection;
    public JdbcTransaction(Connection connection) throws JukeException
    {
        state = TransactionState.CLOSED;
        try
        {
            this.connection = (JdbcConnection)connection;
            //operationsHandler = new TxOperationHandler(this.transactions);
        }
        catch(Exception ex)
        {
            throw new JukeException(ex.getMessage());
        }
    }


    //TxOperationHandler operationsHandler;
    List<EntityOperation> operations = new ArrayList<>();

    @Override
    public EventEmitter<EventObject> getStateChangedEmitter()
    {
        return stateChangedEmitter;
    }

    @Override
    public EntityOperation[] getOperations()
    {
        return (EntityOperation[]) operations.toArray();
    }

    @Override
    public void commit() throws JukeException
    {
        try
        {
            this.connection.getJdbcConnection().commit();
            this.connection.getJdbcConnection().setAutoCommit(true);
            state = TransactionState.COMMITTED;
            stateChangedEmitter.emit(new EventObject(this));
            this.connection.transaction=null;
        }
        catch (SQLException e)
        {
            throw new JukeException(e.getMessage());
        }
    }

    @Override
    public void rollback() throws JukeException
    {
        try
        {
            this.connection.getJdbcConnection().rollback();
            this.state = TransactionState.ABORTED;
            this.stateChangedEmitter.emit(new EventObject(this));

            this.connection.getJdbcConnection().setAutoCommit(true);
            this.connection.transaction = null;
        }
        catch (SQLException e)
        {
            throw new JukeException(e.getMessage());
        }
    }
}
