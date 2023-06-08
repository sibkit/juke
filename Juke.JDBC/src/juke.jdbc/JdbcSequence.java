package juke.jdbc;

import juke.exceptions.JukeException;
import juke.mapping.SequenceMap;
import juke.storage.sequence.Sequence;
import juke.storage.sequence.SequenceOperation;
import juke.storage.sequence.SequenceOperationType;

public class JdbcSequence<T extends Number> implements Sequence<T>
{
    SequenceMap sequenceMap;
    JdbcConnection connection;

    public JdbcSequence(JdbcConnection connection, SequenceMap sequenceMap)
    {
        this.sequenceMap = sequenceMap;
        this.connection = connection;
    }
    @Override
    public String getName()
    {
        return sequenceMap.getSequenceName();
    }

    SequenceOperation nextOperation;
    SequenceOperation getNextOperation()
    {
        if(nextOperation==null)
        {
            nextOperation = new SequenceOperation(SequenceOperationType.NEXT_VALUE, sequenceMap);
        }
        return nextOperation;
    }

    SequenceOperation currentOperation;
    SequenceOperation getCurrentOperation()
    {
        if(currentOperation==null)
        {
            currentOperation = new SequenceOperation(SequenceOperationType.CURRENT_VALUE, sequenceMap);
        }
        return currentOperation;
    }

    @Override
    public T getNextValue() throws JukeException
    {

        return connection.executeSequenceOperation(getNextOperation());
    }

    @Override
    public T getCurrentValue() throws JukeException
    {
        return connection.executeSequenceOperation(getCurrentOperation());
    }
}