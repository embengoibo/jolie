/***************************************************************************
 *   Copyright (C) 2008 by Fabrizio Montesi                                *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU Library General Public     *
 *   License along with this program; if not, write to the                 *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 *                                                                         *
 *   For details about the authors of this software, see the AUTHORS file. *
 ***************************************************************************/

package jolie;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import jolie.lang.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import jolie.lang.parse.Scanner;

/**
 * A parser for JOLIE's command line arguments,
 * providing methods for accessing them.
 * @author Fabrizio Montesi
 */
public class CommandLineParser
{
	private final static Pattern pathSeparatorPattern = Pattern.compile( jolie.lang.Constants.pathSeparator );

	private final int connectionsLimit;
	private final int connectionsCache;
	private final String[] includePaths;
	private final URL[] libURLs;
	private final InputStream programStream;
	private final String programFilepath;
	private final String[] arguments;
	private final Map< String, Scanner.Token > constants = new HashMap< String, Scanner.Token >();
	private final boolean verbose;

	/**
	 * Returns the arguments passed to the JOLIE program.
	 * @return the arguments passed to the JOLIE program
	 */
	public String[] arguments()
	{
		return arguments;
	}

	/**
	 * Returns the file path of the JOLIE program to execute.
	 * @return the file path of the JOLIE program to execute
	 */
	public String programFilepath()
	{
		return programFilepath;
	}

	/**
	 * Returns an InputStream for the program code to execute.
	 * @return an InputStream for the program code to execute
	 */
	public InputStream programStream()
	{
		return programStream;
	}

	/**
	 * Returns the library URLs passed by command line with the -l option.
	 * @return the library URLs passed by command line
	 */
	public URL[] libURLs()
	{
		return libURLs;
	}

	/**
	 * Returns the include paths passed by command line with the -i option.
	 * @return the include paths passed by command line
	 */
	public String[] includePaths()
	{
		return includePaths;
	}

	/**
	 * Returns the connection limit parameter
	 * passed by command line with the -c option.
	 * @return the connection limit parameter passed by command line
	 */
	public int connectionsLimit()
	{
		return connectionsLimit;
	}

	/**
	 * Returns the connection cache parameter
	 * passed by command line with the --conncache option.
	 * @return the connection cache parameter passed by command line
	 */
	public int connectionsCache()
	{
		return connectionsCache;
	}
	
	private static String getOptionString( String option, String description )
	{
		return( '\t' + option + "\t\t" + description + '\n' );
	}
	
	private String getVersionString()
	{
		return( Constants.VERSION + "  " + Constants.COPYRIGHT );
	}

	/**
	 * Returns a map containing the constants defined by command line.
	 * @return a map containing the constants defined by command line
	 */
	public Map< String, Scanner.Token > definedConstants()
	{
		return constants;
	}

	private String getHelpString()
	{
		StringBuilder helpBuilder = new StringBuilder();
		helpBuilder.append( getVersionString() );
		helpBuilder.append( "\n\nUsage: jolie [options] behaviour_file [options] [program arguments]\n\n" );
		helpBuilder.append( "Available options:\n" );
		helpBuilder.append(
				getOptionString( "-h, --help", "Display this help information" ) );
		//TODO include doc for -l and -i
		helpBuilder.append(
				getOptionString( "-C ConstantIdentifier=ConstantValue", "Sets constant ConstantIdentifier to ConstantValue before starting execution" ) );
		helpBuilder.append(
				getOptionString( "--connlimit [number]", "Set the maximum number of active connection threads" ) );
		helpBuilder.append(
				getOptionString( "--conncache [number]", "Set the maximum number of cached persistent output connections" ) );
		helpBuilder.append(
				getOptionString( "--verbose", "Activate verbose mode" ) );
		helpBuilder.append(
				getOptionString( "--version", "Display this program version information" ) );
		return helpBuilder.toString();
	}

	private void parseCommandLineConstant( String input )
		throws IOException
	{
		Scanner scanner = new Scanner( new ByteArrayInputStream( input.getBytes() ), "Command line" );
		Scanner.Token token = scanner.getToken();
		if ( token.is( Scanner.TokenType.ID ) ) {
			String id = token.content();
			token = scanner.getToken();
			if ( token.isNot( Scanner.TokenType.ASSIGN ) ) {
				throw new IOException( "expected = after constant identifier " + id + ", found token type " + token.type() );
			}
			token = scanner.getToken();
			if ( token.isValidConstant() == false ) {
				throw new IOException( "expected constant value for constant identifier " + id + ", found token type " + token.type() );
			}
			constants.put( id, token );
		} else {
			throw new IOException( "expected constant identifier, found token type " + token.type() );
		}
	}

	/**
	 * Returns <code>true</code> if the verbose option has been specified, false otherwise.
	 * @return <code>true</code> if the verbose option has been specified, false otherwise
	 */
	public boolean verbose()
	{
		return verbose;
	}

	/**
	 * Constructor
	 * @param args the command line arguments
	 * @param classLoader the ClassLoader to use for finding resources
	 * @throws jolie.CommandLineException if the command line is not valid or asks for simple information. (like --help and --version)
	 */
	public CommandLineParser( String[] args, ClassLoader classLoader )
		throws CommandLineException, IOException
	{
		boolean bVerbose = false;
		List< String > argumentsList = new ArrayList< String >();
		LinkedList< String > includeList = new LinkedList< String >();
		List< String > libList = new ArrayList< String >();
		int cLimit = -1;
		int cCache = 100;
		String pwd = new File( "" ).getCanonicalPath();
		includeList.add( pwd );
		includeList.add( "include" );
		libList.add( pwd );
		libList.add( "ext" );
		libList.add( "lib" );
		String olFilepath = null;
		for( int i = 0; i < args.length; i++ ) {
			if ( "--help".equals( args[ i ] ) || "-h".equals( args[ i ] ) ) {
				throw new CommandLineException( getHelpString() );
			} else if ( "-C".equals( args[ i ] ) ) {
				i++;
				try {
					parseCommandLineConstant( args[ i ] );
				} catch( IOException e ) {
					throw new CommandLineException( "Invalid constant definition, reason: " + e.getMessage() );
				}
			} else if ( "-i".equals( args[ i ] ) ) {
				i++;
				String[] tmp = pathSeparatorPattern.split( args[ i ] );
				for( String s : tmp ) {
					includeList.add( s );
				}
			} else if ( "-l".equals( args[ i ] ) ) {
				i++;
				String[] tmp = pathSeparatorPattern.split( args[ i ] );
				for( String s : tmp ) {
					libList.add( s );
					if ( s.endsWith( ".jap" ) ) {
						parseJapFile( new File( s ), libList );
					}
				}
			} else if ( "--connlimit".equals( args[ i ] ) ) {
				i++;
				cLimit = Integer.parseInt( args[ i ] );
			} else if ( "--conncache".equals( args[ i ] ) ) {
				i++;
				cCache = Integer.parseInt( args[ i ] );
			} else if ( "--verbose".equals( args[ i ] ) ) {
				bVerbose = true;
			} else if ( "--version".equals( args[ i ] ) ) {
				throw new CommandLineException( getVersionString() );
			} else if ( args[ i ].endsWith( ".ol" ) ) {
				if ( olFilepath == null ) {
					olFilepath = args[ i ];
				} else {
					throw new CommandLineException( "You can specify only an input file." );
				}
			} else if ( args[ i ].endsWith( ".jap" ) ) {
				if ( olFilepath == null ) {
					File japFile = new File( args[ i ] );
					String name = japFile.getName();
					olFilepath = new StringBuilder()
						.append( name.subSequence( 0, name.lastIndexOf( ".jap" ) ) )
						.append( ".ol" )
						.toString();
					libList.add( args[ i ] );
					parseJapFile( japFile, libList );
				} else {
					throw new CommandLineException( "You can specify only an input file." );
				}
			} else {
				for( int j = i; j < args.length; j++ ) {
					argumentsList.add( args[ j ] );
				}
			}/* else
				throw new CommandLineException( "Unrecognized command line token: " + args[ i ] );*/
		}

		verbose = bVerbose;
		
		arguments = argumentsList.toArray( new String[ argumentsList.size() ] );
		
		if ( olFilepath == null ) {
			throw new CommandLineException( "Input file not specified." );
		}
		
		programFilepath = olFilepath;
		
		connectionsLimit = cLimit;
		connectionsCache = cCache;
		
		List< URL > urls = new ArrayList< URL >();
		for( String path : libList ) {
			if ( path.endsWith( ".jar" ) || path.endsWith( ".jap" ) ) {
				urls.add( new URL( "jar:file:" + path + "!/" ) );
			} else if ( new File( path ).isDirectory() ) {
				urls.add( new URL( "file:" + path + "/" ) );
			} else if ( path.endsWith( Constants.fileSeparator + "*" ) ) {
				File dir = new File( path.substring( 0, path.length() - 2 ) );
				String jars[] = dir.list( new FilenameFilter() {
					public boolean accept( File dir, String filename ) {
						return filename.endsWith( ".jar" );
					}
				} );
				if ( jars != null ) {
					for( String jarPath : jars ) {
						urls.add( new URL( "jar:file:" + dir.getCanonicalPath() + Constants.fileSeparator + jarPath + "!/" ) );
					}
				}
			}
		}
		libURLs = urls.toArray( new URL[]{} );
		
		programStream = getOLStream( olFilepath, includeList, classLoader );
		if ( programStream == null ) {
			throw new FileNotFoundException( olFilepath );
		}

		includePaths = includeList.toArray( new String[]{} );
	}

	private void parseJapFile( File japFile, Collection< String > libList )
		throws IOException
	{
		JarFile jap = new JarFile( japFile );
		Manifest manifest = jap.getManifest();
		if ( manifest != null ) {
			Attributes attrs = manifest.getMainAttributes();
			String libs = attrs.getValue( Constants.Manifest.Libraries );
			String[] tmp = pathSeparatorPattern.split( libs );
			for( String s : tmp ) {
				libList.add( s );
			}
		}
	}
	
	private InputStream getOLStream( String olFilepath, LinkedList< String > includePaths, ClassLoader classLoader )
		throws FileNotFoundException, IOException
	{
		InputStream olStream = null;
		File f = new File( olFilepath );
		if ( f.exists() ) {
			olStream = new FileInputStream( f );
		} else {
			for( int i = 0; i < includePaths.size() && olStream == null; i++ ) {
				f = new File(
							includePaths.get( i ) +
							jolie.lang.Constants.fileSeparator +
							olFilepath
						);
				if ( f.exists() ) {
					olStream = new BufferedInputStream( new FileInputStream( f ) );
				}
			}
			if ( olStream == null ) {
				URL olURL = classLoader.getResource( olFilepath );
				if ( olURL != null ) {
					olStream = olURL.openStream();
				}
			}
		}
		if ( olStream != null && f.getParent() != null ) {
			includePaths.addFirst( f.getParent() );
		}
		return olStream;
	}
}
