#! /usr/bin/env node

'use strict'

// spadmin.js
//
//
//
//
//

var commands = new Object();

commands[ "query" ] = [ "node" ]

function commandCompletionEngine( line ) {

    var completions = [];
    buffer.length = 0;

    // HA VAN BENNE PONTOSVESSZŐ, AKKOR OTT CSAK A VÉGÉT, az utolsó szekciót KELL majd FELDOLGOZNI!!!

    const serverListRegexp = RegExp( /(^\s*\([\w,]+\)\s*)|(^\s*\w+\:\s*)/ );

    // keep the server list part in serverListMatch to be able reconstruct it
    var serverListMatch = line.match ( serverListRegexp );
    if ( serverListMatch ) {
        line = line.replace ( serverListRegexp, '' );
        //console.log( "serverListMatch [" + serverlistSave + "] [" + line + "]" );
    }

    // server list #1
    // ( server1, server2, ... 
    var hits = line.match( /^(\s*\((\w+,)*)(\w*)/ ); // server list
    if ( hits ) {
        serverlist.forEach( function( value ) {
            if ( value.match( RegExp( '^' + hits[3], 'i' ) ) ) { completions.push( value ); }
        } );
        if ( completions.length == 1 ) { 
            completions[0] = hits[1] + completions[0];
        };

        return [ completions, line ];
    }

    // level 0
    var commandsBaseLevel = [ "Query", "DEFine", "UPdate", "QUit", "Exit", "Bye", "Logout", "Ruler" ];

    if ( line.match( /^\w+$/ ) ) {

        if ( debug ) { console.log( "LEVEL: 0" ); }

        // server list #2, command list level 0
        // serverList 
        // commandsBaseLevel
        ( serverListMatch ? commandsBaseLevel : serverlist.concat( commandsBaseLevel ) ).forEach( function( value ) {
            if ( value.match( RegExp ( line, 'i' ) ) ) { completions.push( value ) };
        } );

        // : or "" completion
        if ( completions.length == 1 ) {

// ezt itt rendbe kell tenni majd egy picit !
            serverlist.forEach( function( value ) {
                if ( value.match( RegExp( completions[0], 'i' ) ) ) {
                    completions[0] = completions[0] + ": ";
                };
            } );

            commandsBaseLevel.forEach( function( value ) {
                if ( value.match( RegExp( completions[0], 'i' ) ) ) {
                    completions[0] = completions[0] + " ";
                };
            } );

        };

        return [ completions, line ];

    };

// level 2
    hits = line.match( /(q|qu|que|quer|query)(\s+)(n|no|nod|node)(\s+)(.*)/i );
    if ( hits ) { 
        if ( debug ) { console.log( "LEVEL: 2" ); }

        asyncTSMSelect( nodelist, "select node_name from nodes", " where node_name like '%'" );

        completions = [];

        if ( debug ) { console.log( "hit: " + hits[5] ) }

        nodelist.forEach( function( value ) {
            if ( value.match( RegExp( '^' + hits[5], 'i' ) ) ) { completions.push( value ); }
        } );

        if ( completions.length == 1 ) { 
            completions[0] = hits[1] + hits[2] + hits[3] + hits[4] + completions[0] + " ";
        }
        else if ( completions.length > 1 ) {

        }
        else {
            completions = nodelist;
        }

        return [ completions, line ];
     }

// level 1
    var commandsFirstLevel = [ "node", "server", "path", "drive" ];
    hits = line.match( /(q|qu|que|quer|query)(\s+)(\w+)/i );
    if ( hits ) { 

        if ( debug ) { console.log( "LEVEL: 1" ); }

        completions = [];

        commandsFirstLevel.forEach( function( value ) {
            if ( value.match( RegExp( '^' + hits[3], 'i' ) ) ) { completions.push( value ); }
        } );

        if ( completions.length == 1 ) {
            completions[0] = hits[1] + hits[2] + completions[0] + " ";
            return [ completions, line ];
        }
     }
     else {
        completions = commandsFirstLevel;
        return [ completions, line ];
     }

// END

    // reconstruct line if needed
    if ( serverListMatch ) {
        line = serverListMatch[0] + line;
        if ( completions.length == 1 ) { completions[0] = serverListMatch[0] + completions[0] };
    }

    return [ completions, line ];
}

function prompt() {
  var arrow  = '[' + currentTSMserver + ']' + ' > ', 
      length = arrow.length;
      arrow  = '[' + currentTSMserver.bold.green + ']' + ' >'.bold.red;
  rl.setPrompt( arrow, length );
  rl.prompt();
}

function help() {

  process.stdout.write( 'Help!\n'.bold.yellow );

}

function quit() {

  process.stdout.write( '\nGoodbye! Thanks for using it!'.green );
  process.exit( 0 );

}

function exec( command ) {

    subs.updateScreenXY();
    if ( debug ) process.stdout.write( "DEBUG: Received command [".bold.green + command.bold.yellow + "]\n".bold.green );

    switch ( command.toLowerCase() ) {

        case 'select':
            test();
            prompt()
            break;

        case 'print':
            buffer.forEach( function( s ) {
                console.log( s );
            } );
            prompt()
            break;

        case 'load':
            asyncTSMSelect( nodelist, "select node_name from nodes", " where node_name like '%'" );
            prompt()
            break;

        case 'nodelist':
            nodelist.forEach( function( s ) {
                console.log( s );
            } );
            prompt()
            break;

        case 'clear':
            buffer.length = 0;
            process.stdout.write( "The buffer cleared!\n" );
            prompt()
            break;

        case 'help':
            help();

            prompt();
            break;

        case 'ruler':
            subs.showRuler();

            prompt();
            break;

        case 'error':
            console.log("Here's what an error might look like");
            JSON.parse( '{ a: "bad JSON" }' );
            break;

        case 'exit':
        case 'quit':
        case 'q':
            quit();
            break;
   
        default:
            buffer2.length = 0;
            stdoutDisplayEnable = true;
            child2.stdin.write( command + "\n" );

    }

}

// main()

// Declare the app
var spadmin = {};

// Dependencies
var os       = require( 'os' );
var colors   = require( 'colors' );
var readline = require( 'readline' );
var child_pr = require( 'child_process' );

const CFonts = require( 'cfonts' );

var subs     = require( './lib/subs.js' );

//var screenColumns = 80, screenRows = 25;

var serverlist = [ "alma", "korte", "szilva" ];

var buffer = [];
var bufferReady = false;

var buffer2 = [];
var bufferReady2 = false;

var nodelist = [];

var stdoutDisplayEnable = false;

var debug   = false;

var currentTSMserver = "";

var currentCommaDSMADMCPID  = 0;
var currentNormalDSMADMCPID = 0;

// https://nodejs.org/api/child_process.html
// https://github.com/nodejs/help/issues/1183
// https://www.ibm.com/support/knowledgecenter/SSEQVQ_8.1.6/srv.reference/r_cmdline_adclient_options.html
const child = child_pr.spawn( 'dsmadmc', [ '-ID=support', '-PA=asdpoi123', '-comma', '-ALWAYSPrompt' ], { detached:true } );
// '-NEWLINEAFTERPrompt'
currentCommaDSMADMCPID = child.pid;

const child2 = child_pr.spawn( 'dsmadmc', [ '-ID=support', '-PA=asdpoi123', '-ALWAYSPrompt' ], { detached:true } );
// '-NEWLINEAFTERPrompt'
currentNormalDSMADMCPID = child2.pid;

child.once( "exit", function ( errorcode, signal ) {
    console.log( "DSMADMC: exited ".red + "with code: " + errorcode + "SIGNAL: " + signal ? signal : "NULL" );
} );

child.once( "close", function ( errorcode, signal ) {
    console.log( "DSMADMC2: closed ".red + "with code: " + errorcode + " SIGNAL: " + signal ? signal : "NULL" );
} );

child.stderr.once( "data", function ( data ) { console.log( String( data ) ) } );

const wait = () => new Promise( res => {
    const f = () => {
        if( bufferReady ) { 
            res( bufferReady = false );
        }
        else { 
            setTimeout( f );
        }
    };
    f();
} );

const wait2 = () => new Promise( res2 => {
    const f2 = () => {
        if( bufferReady2 ) { 
            res2( bufferReady2 = false );
        }
        else { 
            setTimeout( f2 );
        }
    };
    f2();
} );

child.stdout.on( "data", function( data ) {

    var s = String( data );
    buffer = buffer.concat( s.split( os.EOL ) );

// process.stdout.write( "DEBUG: ".bold.green + "-".repeat( screenColumns - 7 ).grey );
// buffer.forEach( function( s ) {
//     process.stdout.write( "DEBUG: [".bold.green + s.grey + "]\n".bold.green );
// } );
// process.stdout.write( "DEBUG: ".bold.green + "-".repeat( screenColumns - 7 ).grey );

  // if( /^Protect: (\w+)>/.test( buffer.slice( -1 )[0] ) ){
  //     buffer = buffer.filter( ( s, i ) => s.length !== 0 && i !== buffer.length - 1 );
  //     buffer.forEach( s => console.log( `INSIDE: [${s}]` ) );
  //     bufferReady = true;
    // }

    var match = buffer[buffer.length - 1].match( /^Protect: (\w+)>/ );
    if ( match ) {

        buffer.pop();                       // remove it we dont need

        buffer = buffer.filter( n => n );   // clean empty lines

        currentTSMserver = match[1];        // update the current server name

if ( debug ) process.stdout.write( "DEBUG: TSM server name: ".bold.green + currentTSMserver + "\n" );
var line = "-".repeat( subs.getScreenColumns() - 4 );
if ( debug ) process.stdout.write( "\rTSM " + line.grey + "\n" );

        buffer.forEach( function( s ) {

            if ( s.match( /^(\s*|IBM Spectrum Protect|\(c\) Copyright by IBM Corporation and other\(s\) 1990, \d\d\d\d\. All Rights Reserved\.|\s+Server Version \d, Release \d, Level \d\.\d+|\s+Server date\/time.*|Command Line Administrative Interface - Version \d, Release \d, Level \d\.\d+)$/ ) ) {
                // skip it      
            }
            else {

                match = s.match ( /^ANS8001I.*\s+(\d+)\.$/ )
                if ( match ) {
                    process.stdout.write( "DEBUG: LAST ERROR [".bold.green + match[1].bold.red + "]\n".bold.green );
                }
                else {
                    // output 
                    if ( stdoutDisplayEnable ) process.stdout.write( s + "\n" );
                }
            }

        } );

if ( debug ) process.stdout.write( line.grey + " TSM\n" );
        
        bufferReady = true;
        
        if ( stdoutDisplayEnable ) { prompt(); }

    }

} );

child2.stdout.on( "data", function( data ) {

    var s = String( data );
    buffer2 = buffer2.concat( s.split( os.EOL ) );

// process.stdout.write( "DEBUG: ".bold.green + "-".repeat( screenColumns - 7 ).grey );
// buffer.forEach( function( s ) {
//     process.stdout.write( "DEBUG: [".bold.green + s.grey + "]\n".bold.green );
// } );
// process.stdout.write( "DEBUG: ".bold.green + "-".repeat( screenColumns - 7 ).grey );

  // if( /^Protect: (\w+)>/.test( buffer.slice( -1 )[0] ) ){
  //     buffer = buffer.filter( ( s, i ) => s.length !== 0 && i !== buffer.length - 1 );
  //     buffer.forEach( s => console.log( `INSIDE: [${s}]` ) );
  //     bufferReady = true;
    // }

    var match = buffer2[buffer2.length - 1].match( /^Protect: (\w+)>/ );
    if ( match ) {

        buffer2.pop();                       // remove it we dont need

        buffer2 = buffer2.filter( n => n );   // clean empty lines

        currentTSMserver = match[1];        // update the current server name

if ( debug ) process.stdout.write( "DEBUG: TSM server name: ".bold.green + currentTSMserver + "\n" );
var line = "-".repeat( subs.getScreenColumns() - 4 );
if ( debug ) process.stdout.write( "\rTSM " + line.grey + "\n" );

        buffer2.forEach( function( s ) {

            if ( s.match( /^(\s*|IBM Spectrum Protect|\(c\) Copyright by IBM Corporation and other\(s\) 1990, \d\d\d\d\. All Rights Reserved\.|\s+Server Version \d, Release \d, Level \d\.\d+|\s+Server date\/time.*|Command Line Administrative Interface - Version \d, Release \d, Level \d\.\d+)$/ ) ) {
                // skip it      
            }
            else {

                match = s.match ( /^ANS8001I.*\s+(\d+)\.$/ )
                if ( match ) {
                    process.stdout.write( "DEBUG: LAST ERROR [".bold.green + match[1].bold.red + "]\n".bold.green );
                }
                else {
                    // output 
                    if ( stdoutDisplayEnable ) process.stdout.write( s + "\n" );
                }
            }

        } );

if ( debug ) process.stdout.write( line.grey + " TSM\n" );
        
        bufferReady2 = true;
        
        if ( stdoutDisplayEnable ) { prompt(); }

    }

} );

// Child end
child.stdout.on( 'end', function( output ) {
    console.log( 'CHILD STDOUT end 1.!'.bold.red );
} );

child.stdout.on( 'end', function() {
    console.log( "CHILD end 2.!".bold.red );
} );

child.on( 'close', function( code ) {
    console.log( "CHILD close 3.!".bold.red );    
} );

async function asyncTSMSelect( destination, select, parameter ) {

    var param = parameter ? parameter : "";

    stdoutDisplayEnable = false;

    buffer.length = 0;
    bufferReady = false;

    child.stdin.write( select + param + "\n" );
    await wait();

    destination.length = 0;
    buffer.forEach( function( s ) { destination.push( s ) } )

}

var rl = readline.createInterface( {
    input: process.stdin,
    output: process.stdout,
    completer: commandCompletionEngine
} );

rl.on( 'line', function( cmd ) {

    if ( cmd.match( /^\s*$/ ) ) {
        prompt();
    }
    else {
        exec( cmd.trim() );
    }

} );

rl.on( 'close', function() {

buffer.forEach( function(s) { console.log*( s ) } );
  
//    quit();

} );

CFonts.say( "_flex", {
    font:           'block',        // define the font face
    align:          'left',         // define text alignment
    colors:         [ 'system' ],   // define all colors
    background:     'transparent',  // define the background color, you can also use `backgroundColor` here as key
    letterSpacing:  1,              // define letter spacing
    lineHeight:     1,              // define the line height
    space:          true,           // define if the output text should have empty lines on top and on the bottom
    maxLength:      '0',            // define how many character can be on one line
} );

subs.welcome();

console.log ( 'dsmadmc PID#s: [' + currentCommaDSMADMCPID.toString().bold.red + ', ' + currentNormalDSMADMCPID.toString().bold.red +  ']\n' );

console.log ( 'Press ENTER to start!' );