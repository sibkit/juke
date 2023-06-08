package juke.mapping;


public class SequenceMap
{
	public SequenceMap(String sequenceName, String storageSequenceName)
	{
		this.sequenceName = sequenceName;
		this.storageSequenceName = storageSequenceName;
	}

	private String sequenceName;
	private String storageSequenceName;

	public String getSequenceName()
	{
		return sequenceName;
	}

	public String getStorageSequenceName()
	{
		return storageSequenceName;
	}
}
