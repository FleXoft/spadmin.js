
'use strict'

// Dependencies
var os       = require( 'os' );
var colors   = require( 'colors' );

const CFonts = require( 'cfonts' );

var screenColumns = 80, screenRows = 25;

// Declare the subs
var subs = {};

subs.updateScreenXY = function() {
    screenColumns = os.platform() != "win32" ? process.stdout.columns : process.stdout.columns - 1; 
    screenRows    = process.stdout.rows;
}

subs.getScreenColumns = function() {
    return screenColumns;
}

subs.welcome = function() {

    // clear the screen
    process.stdout.write( '\u001B[2J\u001B[0;0f' );
    subs.updateScreenXY();

    // https://github.com/dominikwilkowski/cfonts
    process.stdout.write( '\n' );
    process.stdout.write( '  ███████╗ ██████╗   █████╗  ██████╗  ███╗   ███╗ ██╗ ███╗   ██╗          ██╗ ███████╗\n'.bold.white );
    process.stdout.write( '  ██╔════╝ ██╔══██╗ ██╔══██╗ ██╔══██╗ ████╗ ████║ ██║ ████╗  ██║          ██║ ██╔════╝\n'.bold.white );
    process.stdout.write( '  ███████╗ ██████╔╝ ███████║ ██║  ██║ ██╔████╔██║ ██║ ██╔██╗ ██║          ██║ ███████╗\n'.bold.white );
    process.stdout.write( '  ╚════██║ ██╔═══╝  ██╔══██║ ██║  ██║ ██║╚██╔╝██║ ██║ ██║╚██╗██║     ██   ██║ ╚════██║\n'.bold.white );
    process.stdout.write( '  ███████║ ██║      ██║  ██║ ██████╔╝ ██║ ╚═╝ ██║ ██║ ██║ ╚████║ ██╗ ╚█████╔╝ ███████║\n'.bold.white );
    process.stdout.write( '  ╚══════╝ ╚═╝      ╚═╝  ╚═╝ ╚═════╝  ╚═╝     ╚═╝ ╚═╝ ╚═╝  ╚═══╝ ╚═╝  ╚════╝  ╚══════╝\n'.bold.grey );
    process.stdout.write( '  Powerful CLI administration tool for IBM Spectrum Protect aka Tivoli Storage Manager\n'.bold.white );
    process.stdout.write( '\n' );

    process.stdout.write( [ "= My node.js readline DEMO",
    "= Welcome, enter TSM commands if you're lost type help", 
    "= Your current platform is: " + os.platform() + " " + os.type() + " " + process.arch + " (" + os.release() + ")",
    "= Terminal properties: [" + screenColumns + 'x' + screenRows + "]",
    "=\n" ].join( '\n' ).grey );
}

subs.showRuler = function () {

    var x, c;

    // 
    var mirrorFlag = false;
    
    if ( ! mirrorFlag ) {
      rulerModulo( 100 );
      rulerModulo( 10 );
    }

    //1
    for ( x = 1; screenColumns  >= x; x++ ) {

    c = x % 10;
    ( c == 0 ) ? process.stdout.write( c.toString().bold.green ) : process.stdout.write( c.toString() ) ;
    }
    process.stdout.write( "\n" );

    if ( mirrorFlag ) {
      rulerModulo( 10 );
      rulerModulo( 100 );
    }
    
}

function rulerModulo( modulo ) {

    var x, c;

    //
    for ( x = 1, c = 1; screenColumns >= x; x++ ) {
        if ( x % modulo == 0 ) {
            if ( c == modulo ) { c = 0 };
            process.stdout.write( c.toString().bold.green );
            c++;
        }
        else {
            process.stdout.write( ' ' );
        }
    }
    process.stdout.write( "\n" );

}

//
module.exports = subs;