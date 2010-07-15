package org.bbrtm.yweather.util;

import java.util.Vector;

import org.bbrtm.yweather.model.Place;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public final class Configuration
{
    private static Configuration    _INSTANCE;
    private ConfigurationHashtable  configurationHashtable;
    private static final long       STOREHASH      = 0x204adff197de7708L; // org.bbrtm.yweather.util.Configuration
                                                                          
    private static final int        KEY_NETWORK    = 1;
    private static final int        KEY_PLACES     = 2;
    private static final int        KEY_UNITS      = 3;
    private static final int        KEY_BACKGROUND = 4;
    private static final int        KEY_INTERVAL   = 5;
    private static final int        KEY_DETAILS    = 6;
    private static final int        KEY_WIFI       = 7;
    
    private static Object           placeLock      = new Object();
    
    private static PersistentObject persist;
    
    private Configuration()
    {
        persist = PersistentStore.getPersistentObject(STOREHASH);
        Object contents = persist.getContents();
        if (contents instanceof ConfigurationHashtable)
        {
            try
            {
                configurationHashtable = (ConfigurationHashtable) contents;
            }
            catch (Exception ex)
            {
                configurationHashtable = new ConfigurationHashtable();
                persist.setContents(configurationHashtable);
                persist.commit();
            }
        }
        else
        {
            configurationHashtable = new ConfigurationHashtable();
            persist.setContents(configurationHashtable);
            persist.commit();
        }
    }
    
    public static Configuration getInstance()
    {
        if (_INSTANCE == null)
            _INSTANCE = new Configuration();
        return _INSTANCE;
    }
    
    public static void save()
    {
        persist.commit();
    }
    
    public int getConnectionMode()
    {
        return configurationHashtable.getInt(KEY_NETWORK, 0);
    }
    
    public void setConnectionMode(int mode)
    {
        configurationHashtable.putInt(KEY_NETWORK, mode);
    }
    
    public void setPlaces(Vector places)
    {
        synchronized (placeLock)
        {
            configurationHashtable.put(KEY_PLACES, places);
        }
    }
    
    public synchronized void updatePlace(Place place)
    {
        synchronized (placeLock)
        {
            Vector places = (Vector) configurationHashtable.get(KEY_PLACES);
            int index = places.indexOf(place);
            places.removeElementAt(index);
            places.insertElementAt(place, index);
            configurationHashtable.put(KEY_PLACES, places);
        }
    }
    
    public Vector getPlaces()
    {
        Vector places = null;
        synchronized (placeLock)
        {
            places = (Vector) configurationHashtable.get(KEY_PLACES);
        }
        return places;
    }
    
    public void setUnits(String units)
    {
        configurationHashtable.putString(KEY_UNITS, units);
    }
    
    public String getUnits()
    {
        return configurationHashtable.getString(KEY_UNITS, "f");
    }
    
    public boolean isWiFiEnabled()
    {
        return configurationHashtable.getBoolean(KEY_WIFI, true);
    }
    
    public void setWiFiEnabled(boolean wifi)
    {
        configurationHashtable.putBoolean(KEY_WIFI, wifi);
    }
    
    public void setBackgroundEnabled(boolean enabled)
    {
        configurationHashtable.putBoolean(KEY_BACKGROUND, enabled);
    }
    
    public boolean isBackgroundEnabled()
    {
        return configurationHashtable.getBoolean(KEY_BACKGROUND, true);
    }
    
    public void setUpdateInterval(int interval)
    {
        configurationHashtable.putInt(KEY_INTERVAL, interval);
    }
    
    public int getUpdateInterval()
    {
        return configurationHashtable.getInt(KEY_INTERVAL, 60);
    }
    
    public boolean showDetails()
    {
        return configurationHashtable.getBoolean(KEY_DETAILS, true);
    }
    
    public void setDetails(boolean details)
    {
        configurationHashtable.putBoolean(KEY_DETAILS, details);
    }
}
