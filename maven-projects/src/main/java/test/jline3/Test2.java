package test.jline3;

import java.io.File;

import org.jline.builtins.Completers.DirectoriesCompleter;
import org.jline.builtins.Completers.FilesCompleter;
import org.jline.builtins.Completers.TreeCompleter;
import org.jline.builtins.Completers.TreeCompleter.Node;
import org.jline.reader.Candidate;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

	
public class Test2
{
	public static void main( String[] args ) throws Exception
	{
		StringsCompleter machineCompleter = new StringsCompleter( "machine1","machine2" );
		DirectoriesCompleter directoriesCompleter = new DirectoriesCompleter( new File( "." ) );
		FilesCompleter filesCompleter = new FilesCompleter( new File( "." ) );
		Candidate remoteFileCandidate = new Candidate( "","",null,"destination in remote machine",null,null,false );

		// SEND --file D:\1.txt --machineName machine1 --to E:\2.txt
		Node sendNode = TreeCompleter.node( 
				"SEND",
				TreeCompleter.node( 
					"--file",
					TreeCompleter.node( 
						filesCompleter,
						TreeCompleter.node( 
							"--machineName",
							TreeCompleter.node( 
								machineCompleter,
								TreeCompleter.node( 
									"--to",
									TreeCompleter.node( remoteFileCandidate ) ) ) ) ) ),
		// SEND --dir D:\folder --machineName machine1 --to E:\folder
				TreeCompleter.node( 
					"--dir",
					TreeCompleter.node( 
						directoriesCompleter,
						TreeCompleter.node( 
							"--machineName",
							TreeCompleter.node( 
								machineCompleter,
								TreeCompleter.node( 
									"--to",
									TreeCompleter.node( remoteFileCandidate ) ) ) ) ) ) );
		TreeCompleter treeCompleter = new TreeCompleter( sendNode );
		Terminal terminal = TerminalBuilder.terminal();
		LineReader lineReader = LineReaderBuilder.builder().completer( treeCompleter ).terminal( terminal ).build();
		System.out.println( "Output :: " + lineReader.readLine( "prompt>" ) );
	}
}
