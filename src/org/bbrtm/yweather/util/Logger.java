package org.bbrtm.yweather.util;

import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventLogger;

public final class Logger
{
    
    public static final int   DEBUG       = EventLogger.DEBUG_INFO;
    public static final int   INFO        = EventLogger.INFORMATION;
    public static final int   WARN        = EventLogger.WARNING;
    public static final int   ERROR       = EventLogger.ERROR;
    public static final int   CRITICAL    = EventLogger.SEVERE_ERROR;
    
    private static Logger     _INSTANCE;
    private static String     LOG_NAME    = "YWeather";
    private static final long TEXT_LOG_ID = 0x41d5bd06b9fc833fL;     // org.bbrtm.yweather.util.Logger
                                                                      
    public static Logger getInstance()
    {
        if (_INSTANCE == null)
        {
            _INSTANCE = new Logger();
        }
        
        return _INSTANCE;
    }
    
    private Logger()
    {
        EventLogger.register(TEXT_LOG_ID, LOG_NAME, EventLogger.VIEWER_STRING);
    }
    
    public void debug(String s)
    {
        log(s, DEBUG);
    }
    
    public void info(String s)
    {
        log(s, INFO);
    }
    
    public void warn(String s)
    {
        log(s, WARN);
    }
    
    public void error(String s)
    {
        log(s, ERROR);
    }
    
    public void critical(String s)
    {
        log(s, CRITICAL);
    }
    
    public void exception(String s, Exception e)
    {
        log(s, e, ERROR);
    }
    
    public void exception(Exception e)
    {
        log("", e, ERROR);
    }
    
    /**
     * Just log a simple string
     * 
     * @param s
     *            The message to log
     * @param level
     *            the level at which to log that message
     */
    public void log(String s, int level)
    {
        log(s, null, level);
    }
    
    /**
     * Log an event
     * 
     * @param s
     *            String to log
     * @param e
     *            Exception to log, if any. may be null
     * @param level
     *            one of {@link EventLogger#WARNING} etc
     */
    public void log(String s, Exception e, int level)
    {
        if (e != null)
        {
            StringBuffer sb = new StringBuffer(s);
            sb.append(' ');
            sb.append(e.getClass().getName());
            sb.append(':');
            sb.append(e.getMessage());
            s = sb.toString();
        }
        
        //if (DeviceInfo.isSimulator())
        //    System.out.println("**** " + LOG_NAME + "  " + s);
        
        // simply log s.getBytes()
        EventLogger.logEvent(TEXT_LOG_ID, s.getBytes(), level);
        
    }
    
}
