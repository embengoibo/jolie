/*
 * Copyright (C) 2018 maschio.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import jolie.lang.parse.Scanner;
import jolie.lang.parse.Scanner.Token;

/**
 *
 * @author maschio
 */
public class CommandLineOptions {
    private String commLimit;
    private String connCache;
    private String correlationAlgorithm;
    private boolean typeCheck = false;
    private boolean check = false;
    private boolean trace = false ;
    private Level log = Level.OFF;
    private String charset;
    private final Map< String, Token > constants = new HashMap<>();
    private final Deque< String > includeList = new LinkedList<>();
    private List< String > libList = new ArrayList<>();
    
    
    public void commLimit(String commLimit){
      this.commLimit = commLimit;
    }
    public boolean hasCommitLimit(){
      return this.commLimit != null;
    }
    public String commLimit(){
        return this.commLimit;
    }
    
    public void connCache(String connCache){
      this.connCache = connCache;
    }
    public boolean hasConnCache(){
      return this.connCache != null;
    }
    public String connCache(){
        return this.connCache;
    }
    
    public void correlationAlgorithm (String correlationAlgorithm){
      this.correlationAlgorithm = correlationAlgorithm;
    }
    public boolean hasCorrelationAlgorithm(){
      return this.correlationAlgorithm != null;
    }
    
    public String correlationAlgorithm(){
        return this.correlationAlgorithm;
    }
    
    public void typeCheck (boolean typeCheck){
      this.typeCheck = typeCheck;
    }
    
    public boolean typeCheck(){
        return this.typeCheck;
    }
    
    public void check (boolean check){
      this.check = check;
    }
    
    public boolean check(){
        return this.check;
    }
    
    public void trace (boolean trace){
      this.trace = trace;
    }
    
    public boolean trace(){
        return this.trace;
    }
    
    
    public void log (Level log){
      this.log = log;
    }
    
    public Level log(){
        return this.log;
    }
    
    
    public void charset (String charset){
      this.charset = charset;
    }
    public boolean hasCharset(){
      return this.charset != null;
    }
    
    public String charset(){
        return this.charset;
    }
    
    public void constant(String id , Token token){
       constants.put (id , token);
    }
    
    public boolean hasConstants(){
      return !this.constants.isEmpty();
    }
    
    public Map< String, Token > constants(){
       return this.constants;
    }
    
    public void includeList (String [] includeList ){
       Collections.addAll( this.includeList, includeList);
    }
    
     public void includeList (String include){
       this.includeList.add(include);
    }
     
     public String[] includeList (){
         return includeList.toArray( new String[]{});
     }
     
     public boolean hasIncludeList(){
        return !this.includeList.isEmpty();
     }
     

}
