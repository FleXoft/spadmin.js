package test.tsmlang;

import java.util.Arrays;

public class MainCheck
{
	public static void main( String[] args )
	{
		createTsmCommandQueryNode();

		checkInput( args[0] );
		handleTab( args[0] );
	}

	private static void checkInput( String string )
	{
		// TODO Auto-generated method stub
	}

	private static void handleTab( String string )
	{
		// TODO Auto-generated method stub
	}

	private static void createTsmCommandQueryNode()
	{
		TsmCommandItemSequence objQueryNode = new TsmCommandItemSequence();
		int indexMainItems = 0;
		int indexItems1 = 0;
		int indexItems2 = 0;
		TsmCommandItem ci;

//		------------------
		ci = new TsmStringToken( indexMainItems++,"q","query" );
		objQueryNode.listCommandItems.add( ci );

//		------------------
		ci = new TsmStringToken( indexMainItems++,"n","node" );
		objQueryNode.listCommandItems.add( ci );

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmItemFromFixList( indexItems1++,Arrays.asList( new String[] { "node1","node2" } ) );
			seq1.listCommandItems.add( ci );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"d","domain" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			ci = new TsmItemFromFixList( indexItems2++,Arrays.asList( new String[] { "domain1","domain2" } ) );
			TsmListSeparatedItems list1 = new TsmListSeparatedItems( indexItems1++,",",ci );
			seq1.listCommandItems.add( list1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"f","format" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			TsmCommandItemSequence seq2 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"s","standard" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"d","detailed" );
			seq2.listCommandItems.add( ci );

			TsmChoice ch1 = new TsmChoice( indexItems1++,false,seq2 );
			seq1.listCommandItems.add( ch1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"auth","authentication" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			TsmCommandItemSequence seq2 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"lo","local" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"ld","ldap" );
			seq2.listCommandItems.add( ci );

			TsmChoice ch1 = new TsmChoice( indexItems1++,false,seq2 );
			seq1.listCommandItems.add( ch1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}

//		------------------
		{
			indexItems1 = 0;
			TsmCommandItemSequence seq1 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"t","type" );
			seq1.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"=","=" );
			seq1.listCommandItems.add( ci );

			indexItems2 = 0;
			TsmCommandItemSequence seq2 = new TsmCommandItemSequence();
			ci = new TsmStringToken( indexItems1++,"c","client" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"n","nas" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"s","server" );
			seq2.listCommandItems.add( ci );

			ci = new TsmStringToken( indexItems1++,"a","any" );
			seq2.listCommandItems.add( ci );

			TsmChoice ch1 = new TsmChoice( indexItems1++,false,seq2 );
			seq1.listCommandItems.add( ch1 );

			ci = new TsmChoice( indexMainItems++,true,seq1 );
			objQueryNode.listCommandItems.add( ci );
		}
	}
}
