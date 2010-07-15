package org.bbrtm.yweather;

import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.SystemListener;
import net.rim.device.api.ui.UiApplication;

import org.bbrtm.yweather.controller.LocationController;
import org.bbrtm.yweather.controller.WeatherController;
import org.bbrtm.yweather.google.analytics.CustomVariable;
import org.bbrtm.yweather.google.analytics.Event;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.util.DeviceUtil;

public class YWeather extends UiApplication implements SystemListener
{
    public YWeather()
    {
        LocationController controller = new LocationController();
        controller.showView();
        WeatherController weather = WeatherController.getInstance();
        weather.setLocationController(controller);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        YWeather y = new YWeather();
        
        if (ApplicationManager.getApplicationManager().inStartup())
        {
            y.addSystemListener(y);
        }
        else
        {
            y.startUpLater();
        }
        
        y.enterEventDispatcher();
    }
    
    public void batteryGood()
    {
        // do nothing
    }
    
    public void batteryLow()
    {
        // do nothing
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
        removeSystemListener(this);
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
        }
        startUp();
    }
    
    private void startUp()
    {
        WeatherController.getInstance().updateAll();
        GoogleAnalyticsTracker tracker = GoogleAnalyticsTracker.getInstance();
        Event event = tracker.createPageView("/app_loaded");
        event.setCustomVariable(new CustomVariable(1, "OS", DeviceUtil.getDeviceOS(), CustomVariable.SCOPE_SESSION));
        event.setCustomVariable(new CustomVariable(2, "Application Version", DeviceUtil.getAppVersion(), CustomVariable.SCOPE_SESSION));
        tracker.addEvent(event);
    }
    
    private void startUpLater()
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                startUp();
            }
        });
    }
    
}
