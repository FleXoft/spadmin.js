package test;

import java.util.UUID;

public class ObjectExternalProcessResult
{
	public enum EXTERNAL_PROCESS_RESULT
	{
		commandFinished,
		commandFinishedWithException,
	};

	public final UUID mcrid;
	public EXTERNAL_PROCESS_RESULT result = null;
	public Integer exitValue = null;
	public String commandOutput = null;

	public ObjectExternalProcessResult( UUID id )
	{
		this.mcrid = id;
	}
}
