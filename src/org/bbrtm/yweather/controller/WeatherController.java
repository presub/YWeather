package org.bbrtm.yweather.controller;

import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.rim.blackberry.api.homescreen.HomeScreen;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.api.YahooWeatherAPI;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.model.YahooWeather;
import org.bbrtm.yweather.net.Network;
import org.bbrtm.yweather.net.NetworkCallback;
import org.bbrtm.yweather.net.NetworkConfig;
import org.bbrtm.yweather.util.Configuration;
import org.bbrtm.yweather.util.Logger;

public class WeatherController extends BaseController
{
    private static Logger            log                = Logger.getInstance();
    private static Configuration     config             = Configuration.getInstance();
    
    private static WeatherController INSTANCE           = null;
    
    private LocationController       locationController = null;
    
    private Timer                    timer              = null;
    private TimerTask                updateTimerTask    = null;
    
    private GoogleAnalyticsTracker   tracker            = null;
    
    private WeatherController()
    {
        super();
        tracker = GoogleAnalyticsTracker.getInstance();
        timer = new Timer();
        updateTimerTask = new TimerTask()
        {
            public void run()
            {
                log.info("Timer updating");
                WeatherController.getInstance().updateAll();
            }
        };
        
        if (config.isBackgroundEnabled())
        {
            startTimer();
        }
    }
    
    public boolean isTimerRunning()
    {
        
        if (timer == null) return false; else return true;
    }
    
    public void startTimer()
    {
        timer = new Timer();
        updateTimerTask = new TimerTask()
        {
            public void run()
            {
                log.info("Timer updating");
                WeatherController.getInstance().updateAll();
            }
        };
        System.out.println(config.getUpdateInterval());
        int interval = config.getUpdateInterval() * 60000;
        log.debug("starting timer with interval: " + interval);
        System.out.println("starting timer with interval: " + interval);
        timer.scheduleAtFixedRate(updateTimerTask, interval, interval);
    }
    
    public void stopTimer()
    {
        log.debug("stopping timer");
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
    }
    
    public void resetTimer()
    {
        log.debug("reseting timers");
        stopTimer();
        if (config.isBackgroundEnabled())
            startTimer();
    }
    
    public static WeatherController getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new WeatherController();
        return INSTANCE;
    }
    
    public void setLocationController(LocationController controller)
    {
        this.locationController = controller;
    }
    
    public void updateAll()
    {
        Vector places = config.getPlaces();
        if (places != null)
        {
            Enumeration iter = places.elements();
            
            while (iter.hasMoreElements())
            {
                Place place = (Place) iter.nextElement();
                updateWeather(place);
            }
        }
    }
    
    public void updateWeather(final Place place)
    {
        log.debug("updating weather for " + place.getName());
        System.out.println("updating weather for " + place.getName());
        if (place.getWeather() != null)
        {
            place.getWeather().setCurrentCondition(resource.getString(YWeatherResource.WEATHER_UPDATING));
            config.updatePlace(place);
        }
        tracker.trackEvent("Locations", "Update Weather", null, -1);
        String url = "http://weather.yahooapis.com/forecastrss?w=" + place.getWoeid() + "&u=" + config.getUnits();
        Network network = new Network();
        
        while (! NetworkConfig.isDataConnectionAvailable()) {
            try
            {
                log.debug("no data connection, sleeping for 5 seconds");
                Thread.sleep(5000);
            }
            catch (InterruptedException e){}
        }
        
        if (NetworkConfig.isDataConnectionAvailable())
        {
            log.debug("getting weather data from yahoo");
            network.doGet(url, new NetworkCallback()
            {
                public void onNetworkRequestSuccessful(Object reply)
                {
                    log.debug("successful response: " + reply);
                    YahooWeather weather = YahooWeatherAPI.parseYahooWeather((String) reply);
                    place.setWeather(weather);
                    config.updatePlace(place);
                    Configuration.save();
                    synchronized (Application.getEventLock())
                    {
                        if (config.getIsRunning()) locationController.refreshView();
                    }
                    if (place.isHomescreen())
                    {
                        updateHomeScreen(place);
                    }
                }
                
                public void onNetworkRequestFailed(String message, Exception e)
                {
                    log.error("error updating from yahoo " + message);
                    try
                    {
                        Thread.sleep(5000);
                    }
                    catch (InterruptedException e1){}
                    updateWeather(place);
                }
            });
        }
    }
    
    public static void updateHomeScreen(Place place)
    {
        if (place != null)
        {
            YahooWeather weather = place.getWeather();
            if (weather != null)
            {
                HomeScreen.setName(weather.getCurrentTemp() + "° " + weather.getCurrentCondition());
                
                Font badgeFont = Font.getDefault().derive(Font.PLAIN, 6, Ui.UNITS_pt);
                String temp = weather.getCurrentTemp() + "°";
                int width = badgeFont.getAdvance(temp);
                int height = badgeFont.getHeight();
                
                Bitmap home = Bitmap.getBitmapResource(weather.getCurrentCode() + ".png");
                
                int x = home.getWidth() - width - 2;
                int y = home.getHeight() - height - 2;
                
                Graphics graphics = new Graphics(home);
                graphics.setColor(Color.BLACK);
                graphics.setGlobalAlpha(200);
                graphics.fillRoundRect(x, y, width + 8, height + 2, 5, 5);
                graphics.setColor(Color.WHITE);
                graphics.setGlobalAlpha(255);
                graphics.setFont(badgeFont);
                graphics.drawText(temp, x + 2, y + 1);
                
                HomeScreen.updateIcon(home);
            }
        }
    }
    
    public void refreshView()
    {
        // no view
    }
    
    public void showView()
    {
        // no view
    }
    
}
