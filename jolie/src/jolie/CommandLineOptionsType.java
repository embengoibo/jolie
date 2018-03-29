/*
 * Copyright (C) 2018 Balint Maschio.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package jolie;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jolie.jap.JapURLConnection;
import jolie.lang.Constants;
import jolie.lang.parse.Scanner.Token;
import jolie.runtime.correlation.CorrelationEngine;

/**
 *
 * @author maschio
 */
public class CommandLineOptionsType implements Closeable
{

	private final String commLimit;
	private final String connCache;
	private final int connectionLimit;
	private final int connectionsCache;
	private final CorrelationEngine.Type correlationEngineType;
	private final String csetAlgorithmName = "simple";
	private final boolean typeCheck;
	private final boolean check;
	private final boolean trace;
	private final Level log;
	private final String charset;
	private final Map< String, Token> constants;
	private final Deque< String> includeList;
	private final List< String> libList;
	private final URL[] libURLs;
	private final ClassLoader parentClassLoader;
	private final JolieClassLoader jolieClassLoader;
	private final File programDirectory;
	private final List< String> programArgumentsList;
	private final String olFilepath = null;
	private final boolean ignoreFile = false;
	private final String source;
	private final InputStream programStream;
	private final boolean isProgramCompiled;
	private final File programFilepath;
	private final String[] includePaths;

	private CommandLineOptionsType( CommandLineOptionsBuilder builder )
	{
		this.commLimit = builder.commLimit;
		this.connCache = builder.connCache;
		this.connectionLimit = builder.connectionLimit;
		this.connectionsCache = builder.connectionsCache;
		this.correlationEngineType = builder.correlationEngineType;
		this.typeCheck = builder.typeCheck;
		this.check = builder.check;
		this.log = builder.log;
		this.trace = builder.trace;
		this.charset = builder.charset;
		this.constants = builder.constants;
		this.includeList = builder.includeList;
		this.libList = builder.libList;
		this.libURLs = builder.libURLs;
		this.jolieClassLoader = builder.jolieClassLoader;
		this.programDirectory = builder.programDirectory;
		this.parentClassLoader = builder.parentClassLoader;
		this.programArgumentsList = builder.programArgumentsList;
		this.source = builder.source;
		this.programStream = builder.programStream;
		this.isProgramCompiled = builder.isProgramCompiled;
		this.programFilepath = builder.programFilepath;
		this.includePaths = builder.includePaths;

	}
	
	
	
	public List<String> programArgumentsList(){
	   return this.programArgumentsList;
	}

	public String commLimit()
	{
		return this.commLimit;
	}
	
	
	public InputStream programStream(){
	  return this.programStream;
	}
	
	public String[] includePaths(){
	   return this.includePaths;
	}
	public File programFilepath(){
	  
		return this.programFilepath;
	
	}
	public JolieClassLoader classLoader(){
	  return this.jolieClassLoader;
	}
	
	public Map<String, Token> definedConstants(){
	  return this.constants;
	}

	public boolean hasConnCache()
	{
		return this.connCache != null;
	}

	public String connCache()
	{
		return this.connCache;
	}

	public boolean hasCorrelationAlgorithmType()
	{
		return this.correlationEngineType != null;
	}

	public CorrelationEngine.Type correlationEngine()
	{
		return this.correlationEngineType;
	}

	public boolean typeCheck()
	{
		return this.typeCheck;
	}

	public boolean check()
	{
		return this.check;
	}

	public boolean trace()
	{
		return this.trace;
	}

	public Level log()
	{
		return this.log;
	}

	public boolean hasCharset()
	{
		return this.charset != null;
	}

	public String charset()
	{
		return this.charset;
	}

	public boolean isProgramCompiled()
	{
		return this.isProgramCompiled;

	}

	public void constant( String id, Token token )
	{
		constants.put( id, token );
	}

	public boolean hasConstants()
	{
		return !this.constants.isEmpty();
	}

	public Map< String, Token> constants()
	{
		return this.constants;
	}

	public void includeList( String[] includeList )
	{
		Collections.addAll( this.includeList, includeList );
	}

	public void includeList( String include )
	{
		this.includeList.add( include );
	}

	public String[] includeList()
	{
		return includeList.toArray( new String[]{} );
	}

	public boolean hasIncludeList()
	{
		return !this.includeList.isEmpty();
	}

	public int connectionLimit()
	{
		return this.connectionLimit;
	}

	public int connectionsCache()
	{
		return this.connectionsCache;
	}

	public void libList( String lib )
	{
		libList.add( lib );
	}

	public URL[] libList() throws IOException
	{
		return libURLs;
	}

	public File programDirectory()
	{
		return this.programDirectory;
	}

	public JolieClassLoader jolieClassLoader()
	{
		return this.jolieClassLoader;
	}

	@Override
	public void close() throws IOException
	{
		programStream.close();
	}



	public static class CommandLineOptionsBuilder
	{
		private String commLimit;
		private String connCache;
		private int connectionLimit;
		private int connectionsCache;
		private CorrelationEngine.Type correlationEngineType;
		private String csetAlgorithmName = "simple";
		private boolean typeCheck;
		private boolean check;
		private boolean trace;
		private Level log;
		private String charset;
		private Map< String, Token> constants;
		private Deque< String> includeList;
		private List< String> libList;
		private URL[] libURLs;
		private ClassLoader parentClassLoader;
		private JolieClassLoader jolieClassLoader;
		private File programDirectory;
		private List< String> programArgumentsList;
		private String olFilepath = null;
		private boolean ignoreFile = false;
		private String source;
		private InputStream programStream;
		private boolean isProgramCompiled;
		private File programFilepath;
		private String[] includePaths;

		public CommandLineOptionsBuilder()
		{
			constants = new HashMap<>();
			includeList = new LinkedList<>();
			libList = new LinkedList<>();
			programArgumentsList = new ArrayList<>();

		}

		public CommandLineOptionsBuilder commLimit( String commLimit )
		{
			this.commLimit = commLimit;
			return this;
		}

		public CommandLineOptionsBuilder constants( String id, Token token )
		{
			this.constants.put( id, token );
			return this;
		}

		public CommandLineOptionsBuilder connCache( String connCache )
		{
			this.connCache = connCache;
			return this;
		}

		public CommandLineOptionsBuilder connectionLimit( int connectionLimit )
		{
			this.connectionLimit = connectionLimit;
			return this;
		}

		public CommandLineOptionsBuilder connectionsCache( int connectionsCache )
		{
			this.connectionsCache = connectionsCache;
			return this;
		}

		public CommandLineOptionsBuilder correlationEngineType( CorrelationEngine.Type correlationEngineType )
		{
			this.correlationEngineType = correlationEngineType;
			return this;
		}

		public CommandLineOptionsBuilder typeCheck( boolean typeCheck )
		{
			this.typeCheck = typeCheck;
			return this;
		}

		public CommandLineOptionsBuilder check( boolean check )
		{
			this.check = check;
			return this;
		}

		public CommandLineOptionsBuilder trace( boolean trace )
		{
			this.trace = trace;
			return this;
		}

		public CommandLineOptionsBuilder log( Level log )
		{
			this.log = log;
			return this;
		}

		public CommandLineOptionsBuilder charset( String charset )
		{
			this.charset = charset;
			return this;
		}

		public CommandLineOptionsBuilder libList( String libString )
		{
			this.libList.add( libString );
			return this;
		}

		public CommandLineOptionsBuilder libList( String[] libList )
		{
			Collections.addAll( this.libList, libList );
			return this;
		}

		public CommandLineOptionsBuilder includeList( String[] includeList )
		{
			Collections.addAll( this.includeList, includeList );
			return this;
		}

		public CommandLineOptionsBuilder includeList( String includeList )
		{
			this.includeList.add( includeList );
			return this;
		}

		public CommandLineOptionsBuilder correlationAlgorithmType( CorrelationEngine.Type correlationAlgorithmType )
		{
			this.correlationEngineType = correlationAlgorithmType;
			return this;
		}

		public CommandLineOptionsBuilder programDirectory( File programDirectory )
		{
			this.programDirectory = programDirectory;
			return this;
		}

		public void programArgumentsList( String programArgument )
		{
			programArgumentsList.add( programArgument );
		}

		public void olFilepath( String olFilepath )
		{
			this.olFilepath = olFilepath;
		}

		public boolean hasOlFilepath()
		{

			return olFilepath != null;

		}

		public void csetAlgorithmName( String csetAlgorithmName )
		{
			this.csetAlgorithmName = csetAlgorithmName;
		}

		public void parentClassLoader( ClassLoader parentClassLoader )
		{
			this.parentClassLoader = parentClassLoader;
		}

		public CommandLineOptionsType build() throws IOException, CommandLineException
		{
			correlationEngine();
			toUrlLibList();

			if ( olFilepath == null && !ignoreFile ) {
				throw new CommandLineException( "Input file not specified." );
			}

			jolieClassLoader = new JolieClassLoader( libURLs, parentClassLoader );

			openStrean();

			if ( this.programStream == null ) {
				if ( ignoreFile ) {
					this.source = olFilepath;
					this.programStream = new ByteArrayInputStream( new byte[]{} );
				} else if ( olFilepath.endsWith( ".ol" ) ) {
					// try to read the compiled version of the ol file
					olFilepath += "c";
					openStrean();
					if ( this.programStream == null ) {
						throw new FileNotFoundException( olFilepath );
					}
				} else {
					throw new FileNotFoundException( olFilepath );
				}
			}
			isProgramCompiled = olFilepath.endsWith( ".olc" );
			trace = trace && !isProgramCompiled;
			check = check && !isProgramCompiled;
			programFilepath = new File( this.source );
			includePaths = includeList.toArray( new String[]{} );
			return new CommandLineOptionsType( this );
		}

		private void correlationEngine() throws CommandLineException
		{
			correlationEngineType = CorrelationEngine.Type.fromString( csetAlgorithmName );
			if ( correlationEngineType == null ) {
				throw new CommandLineException( "Unrecognized correlation algorithm: " + csetAlgorithmName );
			}
		}

		private void openStrean() throws FileNotFoundException, IOException
		{

			URL olURL = null;
			File f = new File( olFilepath ).getAbsoluteFile();
			if ( f.exists() ) {
				this.programStream = new FileInputStream( f );
				this.source = f.toURI().getSchemeSpecificPart();
				programDirectory = f.getParentFile();
			} else {
				for( String includePath : includeList ) {
					f = new File(
						includePath
						+ jolie.lang.Constants.fileSeparator
						+ olFilepath
					);
					if ( f.exists() ) {
						f = f.getAbsoluteFile();
						this.programStream = new FileInputStream( f );
						this.source = f.toURI().getSchemeSpecificPart();
						programDirectory = f.getParentFile();
						break;
					}
				}

				if ( this.programStream == null ) {
					try {
						olURL = new URL( olFilepath );
						this.programStream = olURL.openStream();
						this.source = olFilepath;
						if ( this.programStream == null ) {
							throw new MalformedURLException();
						}
					} catch( MalformedURLException e ) {
						olURL = jolieClassLoader.getResource( olFilepath );
						if ( olURL != null ) {
							this.programStream = olURL.openStream();
							this.source = olFilepath;
						}
					}
					if ( programDirectory == null && olURL != null && olURL.getPath() != null ) {
						// Try to extract the parent directory of the JAP/JAR library file
						try {
							File urlFile = new File( JapURLConnection.nestingSeparatorPattern.split( new URI( olURL.getPath() ).getSchemeSpecificPart() )[ 0 ] ).getAbsoluteFile();
							if ( urlFile.exists() ) {
								programDirectory = urlFile.getParentFile();
							}
						} catch( URISyntaxException e ) {
						}
					}
				}
			}
			if ( this.programStream != null ) {
				if ( f.exists() && f.getParent() != null ) {
					includeList.addFirst( f.getParent() );
				} else if ( olURL != null ) {
					String urlString = olURL.toString();
					includeList.addFirst( urlString.substring( 0, urlString.lastIndexOf( '/' ) + 1 ) );
				}

				this.programStream = new BufferedInputStream( this.programStream );
			}
		}

		private void toUrlLibList() throws IOException
		{
			List< URL> urls = new ArrayList<>();

			for( String path : libList ) {
				try {
					if ( path.contains( "!/" ) && !path.startsWith( "jap:" ) && !path.startsWith( "jar:" ) ) {
						path = "jap:file:" + path;
					}
					if ( path.endsWith( ".jar" ) || path.endsWith( ".jap" ) ) {
						if ( path.startsWith( "jap:" ) ) {
							urls.add( new URL( path + "!/" ) );
						} else {
							urls.add( new URL( "jap:file:" + path + "!/" ) );
						}
					} else if ( new File( path ).isDirectory() ) {
						urls.add( new URL( "file:" + path + "/" ) );
					} else if ( path.endsWith( Constants.fileSeparator + "*" ) ) {
						File dir = new File( path.substring( 0, path.length() - 2 ) );
						String jars[] = dir.list( ( File directory, String filename ) -> filename.endsWith( ".jar" ) );
						if ( jars != null ) {
							for( String jarPath : jars ) {
								urls.add( new URL( "jar:file:" + dir.getCanonicalPath() + '/' + jarPath + "!/" ) );
							}
						}
					} else {

						urls.add( new URL( path ) );

					}
				} catch( MalformedURLException e ) {
//					e.printStackTrace();
				}
			}

			urls.add( new URL( "file:/" ) );
			libURLs = urls.toArray( new URL[]{} );

		}
	}

}
