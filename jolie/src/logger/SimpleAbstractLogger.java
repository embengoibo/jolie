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

import java.util.logging.Level;

/**
 *
 * @author maschio
 */
public abstract class SimpleAbstractLogger {
    
    private Level level ; 
    protected abstract void writeLog (LogMessage logMessage);
    public void severe (LogMessage logMessage){
        logMessage.setLoggerLevel(LoggerLevel.SEVERE);
        writeLog(logMessage);
    };
    
    public void warning (LogMessage logMessage){
        logMessage.setLoggerLevel(LoggerLevel.WARNING);
        writeLog(logMessage);
    };
    
    
    public void trace (LogMessage logMessage){
        logMessage.setLoggerLevel(LoggerLevel.WARNING);
        writeLog(logMessage);
    };
    
    public void setLevel ( Level level){
     this.level = level;
    }
    
    public void info (LogMessage logMessage){
        logMessage.setLoggerLevel(LoggerLevel.WARNING);
        writeLog(logMessage);
    
    }
    
    public void fine (LogMessage logMessage){
        logMessage.setLoggerLevel(LoggerLevel.WARNING);
        writeLog(logMessage);
    }
        
}
