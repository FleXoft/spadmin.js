package test;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.log4j.Logger;

import test.ObjectExternalProcessResult.EXTERNAL_PROCESS_RESULT;


public class ManagerCommandRunner implements Runnable
{
	private static final Logger logger = Logger.getLogger( ManagerCommandRunner.class );

	private final ObjectCommandRunnerParams objCommandRunnerParams;
	private final UUID id;
	private final ObjectMainAdmin objMainAdmin;
	private boolean bStopping = false;
	private boolean bFinished = false;


	public ManagerCommandRunner( ObjectMainAdmin objMainAdmin,ObjectCommandRunnerParams objCommandRunnerParams )
	{
		this.objMainAdmin = objMainAdmin;
		this.objCommandRunnerParams = objCommandRunnerParams;
		this.id = UUID.randomUUID();
		logger.info( String.format( "ManagerCommandRunner created:(MCRID=%s)",this.id.toString() ) );
	}

	public void stop()
	{
		bStopping = true;
	}

	@Override
	public void run()
	{
		ObjectExternalProcessResult objResult = new ObjectExternalProcessResult( this.id );
		try
		{
			runMain( objResult );
		}
		catch ( Exception exc )
		{
			objResult.result = EXTERNAL_PROCESS_RESULT.commandFinishedWithException;
			logger.error( "",exc );
		}
		finally
		{
			bFinished = true;
			objMainAdmin.clqThreadResults.offer( objResult );
		}
	}

	private void runMain( ObjectExternalProcessResult objResult ) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
		final ExecuteWatchdog watchdog = new ExecuteWatchdog( ExecuteWatchdog.INFINITE_TIMEOUT );

		runCommand( baos,handler,watchdog );

		while ( bStopping==false )
		{
			if ( handler.hasResult()==true )
				break;

			logger.debug( String.format( "waitFor 1000..." ) );
			handler.waitFor( 1000 );
		}
		if ( bStopping==true )
		{
			watchdog.destroyProcess();
			handler.waitFor();
		}

		objResult.exitValue = handler.getExitValue();
		objResult.result = EXTERNAL_PROCESS_RESULT.commandFinished;
		logger.debug( String.format( "exitValue=%d",objResult.exitValue ) );

		ExecuteException executeException = handler.getException();
		if ( executeException!=null )
		{
			objResult.exitValue = executeException.getExitValue();
			objResult.result = EXTERNAL_PROCESS_RESULT.commandFinishedWithException;
			logger.debug( String.format( "executeException exitValue=%d,%s",objResult.exitValue,executeException.getMessage() ) );
		}

		objResult.commandOutput = baos.toString();
		logger.debug( String.format( "output=\n%s",objResult.commandOutput ) );
	}

	private void runCommand( ByteArrayOutputStream baos,DefaultExecuteResultHandler handler,ExecuteWatchdog watchdog ) throws Exception
	{
		CommandLine cmdLine = CommandLine.parse( objCommandRunnerParams.command );

		final PumpStreamHandler pumpStreamHandler = new PumpStreamHandler( baos );

		final DefaultExecutor executor = new DefaultExecutor();
		executor.setStreamHandler( pumpStreamHandler );
		executor.setWatchdog( watchdog );
		String strWorkingDir = null;
		if ( objCommandRunnerParams.dirWorkingDir!=null )
		{
			executor.setWorkingDirectory( objCommandRunnerParams.dirWorkingDir );
			strWorkingDir = objCommandRunnerParams.dirWorkingDir.getPath();
		}

		Map<String,String> env = new HashMap<String,String>( System.getenv() );	//new HashMap<String,String>();

// NEM MŰKÖDIK a PATH kezelés!!! A FUTTATANDÓ COMMAND MINDENKÉPPEN PATH-ON KELL LEGYEN, GÉP RESTART KELL!!!
//		String envPath = env.get( "Path" );
//		if ( envPath==null )
//			envPath = "";
//		String strOnDemandCommandPath = objCommandRunnerParams.objTask.getObjMainAdmin().config.getOndemand().getOnDemandCommandPath();
//		envPath = strOnDemandCommandPath + ";" + envPath;
//		logger.debug( String.format( "envPath=%s=",envPath ) );
//		env.put( "Path",envPath );
//
//		{
//			Set<Entry<String,String>> entrySet = env.entrySet();
//			for ( Entry<String,String> entry : entrySet )
//				logger.debug( String.format( "env:%s=%s:",entry.getKey(),entry.getValue() ) );
//			String[] strings = EnvironmentUtils.toStrings( env );
//			for ( String strEnv : strings )
//				logger.debug( String.format( "strings:%s:",strEnv ) );
//
//			try
//			{
//				logger.debug( String.format( "start arsload.bat..." ) );
//				Process exec = Runtime.getRuntime().exec( "arsload1.bat haho",strings,new File( "C:\\Temp" ) );
//				logger.debug( String.format( "start arsload.bat...end:%d",exec.waitFor() ) );
//			}
//			catch ( Exception exc )
//			{
//				logger.error( "rt",exc );
//			}
//		}

		executor.execute( cmdLine,env,handler );

		logger.debug( String.format( "executing command(%s) workdir(%s)",objCommandRunnerParams.command,strWorkingDir ) );
	}

	public boolean isbFinished()
	{
		return bFinished;
	}
	public UUID getId()
	{
		return id;
	}
	public ObjectCommandRunnerParams getObjCommandRunnerParams()
	{
		return objCommandRunnerParams;
	}

	public String toInfoString()
	{
		return String.format( "process id=(%s) command(%s)",this.id.toString(),this.objCommandRunnerParams.command );
	}
}
