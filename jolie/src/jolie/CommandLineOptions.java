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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jolie.lang.Constants;
import jolie.lang.parse.Scanner.Token;
import jolie.runtime.correlation.CorrelationEngine;

/**
 *
 * @author maschio
 */
public class CommandLineOptions
{

	private final String commLimit;
	private final String connCache;
	private final int connectionLimit;
	private final int connectionsCache;
	private final CorrelationEngine correlationEngine;
	private final boolean typeCheck;
	private final boolean check;
	private final boolean trace;
	private final Level log;
	private final String charset;
	private final Map< String, Token> constants;
	private final Deque< String> includeList;
	private final List< String> libList;
	private final URL[] libURLs;
	private final JolieClassLoader jolieClassLoader;

	private CommandLineOptions( CommandLineOptionsBuilder builder )
	{
		this.commLimit = builder.commLimit;
		this.connCache = builder.connCache;
		this.connectionLimit = builder.connectionLimit;
		this.connectionsCache = builder.connectionsCache;
		this.correlationEngine = builder.correlationEngine;
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
		
	}

	public String commLimit()
	{
		return this.commLimit;
	}

	public boolean hasConnCache()
	{
		return this.connCache != null;
	}

	public String connCache()
	{
		return this.connCache;
	}

	public boolean hasCorrelationEngine()
	{
		return this.correlationEngine != null;
	}

	public CorrelationEngine correlationAlgorithm()
	{
		return this.correlationEngine;
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

	public JolieClassLoader jolieClassLoader()
	{
		return this.jolieClassLoader;
	}

	public static class CommandLineOptionsBuilder
	{
		private String commLimit;
		private String connCache;
		private int connectionLimit;
		private int connectionsCache;
		private String correlationAlgorithm;
		private CorrelationEngine correlationEngine;
		private boolean typeCheck;
		private boolean check;
		private boolean trace;
		private Level log;
		private String charset;
		private Map< String, Token> constants;
		private Deque< String> includeList;
		private List< String> libList;
		private URL[] libURLs;
		private JolieClassLoader jolieClassLoader;

		public CommandLineOptionsBuilder()
		{
			constants = new HashMap<>();
			includeList = new LinkedList<>();
			libList = new LinkedList<>();

		}

		public CommandLineOptionsBuilder commLimit( String commLimit )
		{
			this.commLimit = commLimit;
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

		public CommandLineOptionsBuilder correlationAlgorithm( String correlationAlgorithm )
		{
			this.correlationAlgorithm = correlationAlgorithm;
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
		
		public CommandLineOptionsBuilder charset ( String charset){
			this.charset = charset;
			return this;
		}
		
		public CommandLineOptionsBuilder libList ( String libString){
		 this.libList.add( libString );
		 return this;
		}
		
		public CommandLineOptionsBuilder libList ( String [] libList){
		 Collections.addAll(this.libList , libList);
		 return this;
		}
		
		public CommandLineOptionsBuilder includeList (String[] includeList){
		  Collections.addAll( this.includeList, includeList);
		  return this;
		}
		
		

		
		public CommandLineOptionsBuilder correlationEngine(CorrelationEngine correlationEngine){
		   this.correlationEngine = correlationEngine;
		  return this;
		}
		
		public CommandLineOptions build(){
			return new CommandLineOptions(this);
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
