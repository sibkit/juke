package juke.storage;

import juke.MappingData;
import juke.exceptions.JukeException;

public interface StorageDriver
{
    void initialize(MappingData mappingData);
    Connection createConnection() throws JukeException;
}
