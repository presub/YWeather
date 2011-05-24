package org.bbrtm.yweather.controller;

import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;

import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.ui.screen.LocationScreen;
import org.bbrtm.yweather.util.CheckUpdateThread;
import org.bbrtm.yweather.util.Configuration;
import org.bbrtm.yweather.util.Logger;

public class LocationController extends BaseController implements FieldChangeListener
{
    private static Logger        log         = Logger.getInstance();
    private static Configuration config      = Configuration.getInstance();
    private Timer                updateTimer = null;
    
    private LocationScreen       screen      = null;
    
    public LocationController()
    {
        super();
        
        updateTimer = new Timer();
        updateTimer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                CheckUpdateThread updateThread = new CheckUpdateThread();
                updateThread.start();
            }
        }, 0, 86400000);
    }
    
    public void refreshView()
    {
        int index = screen.getSelectedIndex();
        screen.setLocations(getPlaces());
        screen.setSelectedIndex(index);
    }
    
    public void showView()
    {
        screen = new LocationScreen(this);
        UiApplication.getUiApplication().pushScreen(screen);
    }
    
    public void searchLocation(String location)
    {
        if (location != null && location.length() > 0)
        {
            SearchController controller = new SearchController(location);
            controller.showView();
            controller.startSearch();
        }
    }
    
    public Place[] getPlaces()
    {
        Vector p = config.getPlaces();
        if (p == null)
            return new Place[0];
        Place[] places = new Place[p.size()];
        p.copyInto(places);
        return places;
    }
    
    public void removeLocation(int index)
    {
        Vector p = config.getPlaces();
        if (p != null)
        {
            Place place = (Place) p.elementAt(index);
            p.removeElementAt(index);
            if (place.isHomescreen() && p.size() > 0)
            {
                setHomescreenLocation(0);
            }
            config.setPlaces(p);
            Configuration.save();
            refreshView();
        }
    }
    
    public void moveUp(int index)
    {
        Vector p = config.getPlaces();
        if (p != null && index > 0)
        {
            Place place = (Place) p.elementAt(index);
            p.removeElementAt(index);
            p.insertElementAt(place, index - 1);
            config.setPlaces(p);
            Configuration.save();
            refreshView();
            screen.setSelectedIndex(index - 1);
        }
    }
    
    public void moveDown(int index)
    {
        Vector p = config.getPlaces();
        if (p != null && index < p.size() - 1)
        {
            Place place = (Place) p.elementAt(index);
            p.removeElementAt(index);
            p.insertElementAt(place, index + 1);
            config.setPlaces(p);
            Configuration.save();
            refreshView();
            screen.setSelectedIndex(index + 1);
        }
    }
    
    public void setHomescreenLocation(int index)
    {
        Vector p = config.getPlaces();
        if (p != null)
        {
            Enumeration iter = p.elements();
            while (iter.hasMoreElements())
            {
                Place place = (Place) iter.nextElement();
                if (place.isHomescreen())
                {
                    place.setHomescreen(false);
                    break;
                }
            }
            Place place = (Place) p.elementAt(index);
            place.setHomescreen(true);
            WeatherController.updateHomeScreen(place);
            config.setPlaces(p);
            Configuration.save();
            refreshView();
            screen.setSelectedIndex(index);
        }
    }
    
    public void fieldChanged(Field field, int context)
    {
        
    }
    
    public void updatePlace(Place place)
    {
        config.updatePlace(place);
        Configuration.save();
        refreshView();
    }
    
}
