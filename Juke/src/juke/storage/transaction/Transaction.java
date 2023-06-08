package juke.storage.transaction;


import juke.exceptions.JukeException;
import juke.storage.operation.EntityOperation;
import sibutils.events.EventEmitter;

import java.util.EventObject;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 07.01.12
 * Time: 2:17
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public interface Transaction
{
    EventEmitter<EventObject> getStateChangedEmitter();
    EntityOperation[] getOperations();
    void begin() throws JukeException;
    void commit() throws JukeException;
    void rollback() throws JukeException;
    TransactionState getState();
}
