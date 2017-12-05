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
package logger;

import java.util.Iterator;
import java.util.Map;
import jolie.runtime.ValueVector;

/**
 *
 * @author maschio
 */
public class JsonLogger extends SimpleAbstractLogger {

    @Override
    protected void writeLog(LogMessage logMessage) {
         if (logMessage.getLoggerLevel() == LoggerLevel.SEVERE){
             System.out.print("{\"Level\":");
             System.out.print("\"SEVERE\",");
          } 
         
         if (logMessage.getLoggerLevel() == LoggerLevel.WARNING){
             System.out.print("{\"Level\":");
             System.out.print("\"WARNING\",");
          }   
             System.out.print("\"ErrorType\":");
             System.out.print("\""+ logMessage.getErrorType() +"\",");
             if (logMessage.getData().hasChildren()){
                 Iterator<Map.Entry<String, ValueVector>> iterator = logMessage.getData().children().entrySet().iterator();
                 String dataString = "{";
                 int counter = 0;
                 while (iterator.hasNext()){
                    
                     Map.Entry<String, ValueVector> entry = iterator.next();
                     dataString+= "\""+ entry.getKey()+"\":";
                     if (entry.getValue().get(0).strValue().contains("\\")){
                        dataString+="\"" + entry.getValue().get(0).strValue().replace("\\", "\\\\") + "\"";
                     }else{
                        dataString+="\"" + entry.getValue().get(0).strValue() + "\"";
                     }
                     counter++;
                     if (logMessage.getData().children().size() == counter ){
                       dataString+="}";
                     }else{
                        dataString+=",";
                     }
                 }
                 System.out.print("\"data\":");
                 System.out.print(dataString + "}");
             }else{
                 System.out.print("\"data\":");
                 System.out.print("\""+ logMessage.getData().strValue() + "\"}");
             }
         
    }
    
}
