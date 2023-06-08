package juke;

import juke.exceptions.JukeException;
import juke.cache.Repository;

import java.util.ArrayList;
import java.util.List;


public class SessionManager
{
	private Configuration configuration;

	public SessionManager(Configuration cfg)
	{
		this.configuration = cfg;
	}

	private List<Session> activeSessions = new ArrayList<>();

	public Repository createRepository() throws JukeException
	{
		validateState();
		return new Repository(this.configuration.getMappingData());
	}

	void validateState() throws JukeException
	{
		if (this.configuration == null)
		{
			throw new JukeException("Configuration is null");
		}
		if (this.configuration.getStorageDriver() == null)
		{
			throw new JukeException("Storage driver is null");
		}
	}

	public Session createSession() throws JukeException
	{
		validateState();
		Session result = new Session(getConfiguration().getStorageDriver().createConnection(), this);
		activeSessions.add(result);
		return result;
	}

	public List<Session> getActiveSessions()
	{
		return activeSessions;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}
}
