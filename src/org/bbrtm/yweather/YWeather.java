/*
 * yWeatherLauncher.java
 *
 * © <your company here>, <year>
 * Confidential and proprietary.
 */

package org.bbrtm.yweather;

import org.bbrtm.yweather.controller.WeatherController;
import org.bbrtm.yweather.util.Configuration;
import org.bbrtm.yweather.util.Logger;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.SystemListener;

/**
 * 
 */
public class YWeather extends Application implements SystemListener
{
    
    private static Configuration config = Configuration.getInstance();
    private static Logger        log    = Logger.getInstance();
    
    public YWeather()
    {
        
        // WeatherController weather = WeatherController.getInstance();
    }
    
    public static void main(String[] args)
    {
        
        if ((args != null) && (args.length > 0) && args[0].equals("gui"))
        {
            YWeather y = new YWeather();
            config.setIsRunning(true);
            y.addSystemListener(y);
            y.enterEventDispatcher();
        }
        else
        {
            YWeather ywl = new YWeather();
            WeatherController weather = WeatherController.getInstance();
            weather.updateAll();
            ywl.addSystemListener(ywl);
            ywl.enterEventDispatcher();
        }
    }
    
    public void batteryGood()
    {
        WeatherController weather = WeatherController.getInstance();
        if ((config.isBackgroundEnabled()) && (!weather.isTimerRunning()))
        {
            weather.startTimer();
            config.setIsRunning(true);
            log.info("Battery status normal; restarted timer");
        }
    }
    
    public void batteryLow()
    {
        WeatherController weather = WeatherController.getInstance();
        if (config.isBackgroundEnabled())
        {
            weather.stopTimer();
            config.setIsRunning(false);
            log.info("Battery status critical; stopped timer");
        }
    }
    
    public void batteryStatusChange(int status)
    {
        // do nothing
    }
    
    public void powerOff()
    {
        // do nothing
    }
    
    public void powerUp()
    {
        
    }
    
}
