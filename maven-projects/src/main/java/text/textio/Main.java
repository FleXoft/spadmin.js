package text.textio;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.system.SystemTextTerminal;

public class Main
{
	public static void main( String[] args )
	{
        SystemTextTerminal sysTerminal = new SystemTextTerminal();
        TextIO textIO = new TextIO(sysTerminal);
//		TextIO textIO = TextIoFactory.getTextIO();

		String user = textIO.newStringInputReader().withDefaultValue( "admin" ).read( "Username" );

		String password = textIO.newStringInputReader().withMinLength( 6 ).withInputMasking( true ).read( "Password" );

		int age = textIO.newIntInputReader().withMinVal( 13 ).read( "Age" );

		TextTerminal terminal = textIO.getTextTerminal();
		terminal.printf( "\nUser %s is %d years old, has the password %s.\n",user,age,password );
	}

}
