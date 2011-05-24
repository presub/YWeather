package org.bbrtm.yweather.ui.screen;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.util.CheckUpdateThread;

public class BaseScreen extends MainScreen
{
    protected static ResourceBundle resource         = null;
    
    protected MenuItem              exitMenuItem     = null;
    protected MenuItem              aboutMenuItem    = null;
    protected MenuItem              updatesMenuItem  = null;
    protected MenuItem              feedbackMenuItem = null;
    
    static
    {
        resource = ResourceBundle.getBundle(YWeatherResource.BUNDLE_ID, YWeatherResource.BUNDLE_NAME);
    }
    
    public BaseScreen()
    {
        this(0);
    }
    
    public BaseScreen(long style)
    {
        super(style);
        
        exitMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_EXIT, 1190000, 100000)
        {
            public void run()
            {
                HomeScreen.setName(null);
                HomeScreen.updateIcon(null);
                System.exit(0);
            }
        };
        
        addMenuItem(exitMenuItem);
        
        aboutMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_ABOUT, 1150000, 100000)
        {
            public void run()
            {
                UiApplication.getUiApplication().pushScreen(new AboutScreen());
            }
        };
        
        addMenuItem(aboutMenuItem);
        
        updatesMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_CHECK_UPDATES, 1150000, 100000)
        {
            public void run()
            {
                GoogleAnalyticsTracker.getInstance().trackEvent("Locations", "Check for Updates", null, -1);
                CheckUpdateThread updateThread = new CheckUpdateThread(true);
                updateThread.start();
            }
        };
        
        addMenuItem(updatesMenuItem);
        
        feedbackMenuItem = new MenuItem(resource, YWeatherResource.MENU_ITEM_FEEDBACK, 1150000, 100000)
        {
            public void run()
            {
                GoogleAnalyticsTracker.getInstance().trackEvent("Locations", "Feedback", null, -1);
                MessageArguments args = new MessageArguments(MessageArguments.ARG_NEW, "jemerick+yweather@gmail.com", "YWeather Feedback", "");
                Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, args);
            }
        };
        
        addMenuItem(feedbackMenuItem);
    }
    
}
