package juke;

import juke.storage.StorageDriver;


public class Configuration
{
    private StorageDriver storageDriver;
    private MappingData mappingData;

    public void setStorageDriver(StorageDriver storageDriver)
    {
        this.storageDriver = storageDriver;
    }
    public StorageDriver getStorageDriver() {
        return this.storageDriver;
    }

    public MappingData getMappingData()
    {
        return mappingData;
    }
    public void setMappingData(MappingData mappingData)
    {
        this.mappingData = mappingData;
    }

    public void configure() throws Exception
    {
        this.storageDriver.initialize(mappingData);
    }
}
