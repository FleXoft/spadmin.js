package test;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

public class Main
{
	private static final Logger logger = Logger.getLogger( Main.class );

	public static final String FILENAME_LOG4j_CONFIG = "log4j-config.xml";

	public Main() throws Exception
	{
		ObjectMainAdmin objMainAdmin = new ObjectMainAdmin( true );
		ObjectCommandRunnerParams objCommandRunnerParams = new ObjectCommandRunnerParams( "testtsm",new File( "." ) );
		ManagerCommandRunner managerCommandRunner = new ManagerCommandRunner( objMainAdmin,objCommandRunnerParams );
		try
		{
			new Thread( managerCommandRunner ).start();
		}
		finally
		{
			managerCommandRunner.stop();
		}
	}

	public static void main( String[] args ) throws Exception
	{
		DOMConfigurator.configure( FILENAME_LOG4j_CONFIG );
		logger.debug( "Start" );

		try
		{
			new Main();
		}
		catch ( Exception exc )
		{
			logger.error( "",exc );
		}

		logger.debug( "End" );
	}
}
