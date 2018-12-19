package test;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;


public class ObjectMainAdmin
{
	private static final Logger logger = Logger.getLogger( ObjectMainAdmin.class );

	public final boolean bTestMode;
	public final ConcurrentLinkedQueue<ObjectExternalProcessResult> clqThreadResults;


	public ObjectMainAdmin( boolean bTestMode ) throws Exception
	{
		this.bTestMode = bTestMode;
		clqThreadResults = new ConcurrentLinkedQueue<ObjectExternalProcessResult>();
	}

	public void processThreadResults()
	{
		while ( true )
		{
			ObjectExternalProcessResult objResult = clqThreadResults.poll();
			if ( objResult==null )
				break;

//			logger.debug( String.format( "ARRIVED result taskid=%s,type=%s,result=%s:",objResult.objTask.getTaskId().toString(),objResult.objTask.getObjArchiveType().getArchiveTypeId(),objResult.result.name() ) );
//			objResult.objTask.longRunningProcessCompleted( objResult );
		}
	}
}
