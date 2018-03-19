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

	private String commLimit;
	private String connCache;
	private int connectionLimit = -1;
	private int connectionsCache = 100;
	private String correlationAlgorithm;
	private boolean typeCheck = false;
	private boolean check = false;
	private boolean trace = false;
	private Level log = Level.OFF;
	private String charset;
	private final Map< String, Token> constants = new HashMap<>();
	private final Deque< String> includeList = new LinkedList<>();
	private List< String> libList = new ArrayList<>();
	private URL[] libURLs;
	private JolieClassLoader jolieClassLoader;

	public void commLimit( String commLimit )
	{
		this.commLimit = commLimit;
	}

	public boolean hasCommitLimit()
	{
		return this.commLimit != null;
	}

	public String commLimit()
	{
		return this.commLimit;
	}

	public void connCache( String connCache )
	{
		this.connCache = connCache;
	}

	public boolean hasConnCache()
	{
		return this.connCache != null;
	}

	public String connCache()
	{
		return this.connCache;
	}

	public void correlationAlgorithm( String correlationAlgorithm )
	{
		this.correlationAlgorithm = correlationAlgorithm;
	}

	public boolean hasCorrelationAlgorithm()
	{
		return this.correlationAlgorithm != null;
	}

	public CorrelationEngine.Type correlationAlgorithm()
	{
		return CorrelationEngine.Type.fromString( this.correlationAlgorithm );
	}

	public void typeCheck( boolean typeCheck )
	{
		this.typeCheck = typeCheck;
	}

	public boolean typeCheck()
	{
		return this.typeCheck;
	}

	public void check( boolean check )
	{
		this.check = check;
	}

	public boolean check()
	{
		return this.check;
	}

	public void trace( boolean trace )
	{
		this.trace = trace;
	}

	public boolean trace()
	{
		return this.trace;
	}

	public void log( Level log )
	{
		this.log = log;
	}

	public Level log()
	{
		return this.log;
	}

	public void charset( String charset )
	{
		this.charset = charset;
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

	public void connectionLimit( int connectionLimit )
	{
		this.connectionLimit = connectionLimit;
	}

	public int connectionLimit()
	{
		return this.connectionLimit;
	}

	public void connectionsCache( int connectionsCache )
	{
		this.connectionsCache = connectionsCache;

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
	  toUrlLibList();	
      return libURLs;
	}
	
	public void jolieClassLoader(ClassLoader parentClassLoader) throws IOException{
	  toUrlLibList();	
	  jolieClassLoader = new JolieClassLoader( libURLs, parentClassLoader );
	}
	
	public JolieClassLoader jolieClassLoader(){
	  return this.jolieClassLoader;
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
