package test;

import java.io.File;

public class ObjectCommandRunnerParams
{
	public final String command;
	public final File dirWorkingDir;

	public ObjectCommandRunnerParams( String command,File dirWorkingDir )
	{
		this.command = command;
		this.dirWorkingDir = dirWorkingDir;
	}
}
