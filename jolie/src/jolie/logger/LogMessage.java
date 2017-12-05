/*
 * Copyright (C) 2017 maschio.
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
package jolie.logger;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import jolie.runtime.Value;

/**
 *
 * @author maschio
 */
public class LogMessage {
     private LoggerLevel loggerLevel;
     private Value data;
     private String errorCode = null;
     private String errorType = null;
     
    public  LogMessage (String message){
      data = Value.create(message);
    } 
    public  LogMessage (LoggerLevel loggerLevel, Value data ){
      this.loggerLevel = loggerLevel;
      this.data = data;
    
    }
    
    public LogMessage (LoggerLevel loggerLevel, Value data , String errorCode , String errorType){
      this.loggerLevel = loggerLevel;
      this.data = data;
      this.errorCode = errorCode;
    }
    
    public LogMessage (Throwable t ){
       ByteArrayOutputStream bs = new ByteArrayOutputStream();
       t.printStackTrace( new PrintStream( bs ) ); 
       this.data = Value.create (bs.toString());
       this.errorType = t.getClass().getName();
    }
    
    public void setLoggerLevel (LoggerLevel loggerLevel){
      this.loggerLevel = loggerLevel;
    }
    public LoggerLevel getLoggerLevel (){
       return this.loggerLevel;
    }
    
    public boolean hasErrorCode (){
      if (errorCode == null){
        return false;
      } else{
        return true;
      }
    }
    
    public boolean hasErrorType(){
     if (errorType == null){
        return false;
      } else{
        return true;
      }
    }
    
   public void setErrorCode(String errorCode){
       this.errorCode = errorCode;
   }
    
   public String getErrorCode (){
      return this.errorCode;
    } 
   
   public void setErrorType (String errorType){
     this.errorType = errorType;
   }
   
   public String getErrorType (){
      return errorType;
   }
    
    public Value getData(){
      return this.data;
    } 
    
    public void setData(Value data){
      this.data = data;
    }    
}
