package juke.storage.sequence;

import juke.mapping.SequenceMap;

/**
 * Created with IntelliJ IDEA.
 * User: chelovek
 * Date: 24.09.12
 * Time: 1:35
 * To change this template use File | Settings | File Templates.
 */
public class SequenceOperation
{
	public SequenceOperation(SequenceOperationType operationType, SequenceMap sequenceMap)
	{
		this.operationType = operationType;
		this.sequenceMap = sequenceMap;
	}

	private SequenceOperationType operationType;
	private SequenceMap sequenceMap;

	public SequenceOperationType getOperationType()
	{
		return operationType;
	}

	public SequenceMap getSequenceMap()
	{
		return sequenceMap;
	}
}
