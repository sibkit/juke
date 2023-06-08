package juke.storage;


import juke.exceptions.JukeException;
import juke.mapping.SequenceMap;
import juke.querying.queries.Query;
import juke.storage.operation.EntityOperation;
import juke.storage.sequence.Sequence;
import juke.storage.transaction.Transaction;
import sibutils.events.EventEmitter;

import java.util.EventObject;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: chelovek
 * Date: 07.01.12
 * Time: 10:16
 * To change this juke.sqliteAndroid.templating use File | Settings | File Templates.
 */
public interface Connection
{
    EventEmitter<EventObject> getAfterOperationExecuteEmitter();
    EventEmitter<EventObject> getBeforeOperationExecuteEmitter();

    void close() throws JukeException;
    boolean isClosed() throws JukeException;
	Transaction getCurrentTransaction();
    Transaction beginTransaction() throws JukeException;

	Sequence<?> getSequence(SequenceMap map) throws JukeException;
    void executeOperation(EntityOperation operation) throws JukeException;
    void executeOperations(List<EntityOperation> operation) throws JukeException;
    StorageIterator iterate(Query query) throws JukeException;
}
