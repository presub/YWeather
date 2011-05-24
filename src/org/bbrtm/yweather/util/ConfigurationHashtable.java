package org.bbrtm.yweather.util;

import net.rim.device.api.util.IntHashtable;
import net.rim.device.api.util.Persistable;

public final class ConfigurationHashtable extends IntHashtable implements Persistable
{
    public void putBoolean(int key, boolean b)
    {
        this.put(key, new Boolean(b));
    }
    
    public boolean getBoolean(int key, boolean defaultValue)
    {
        Boolean b = (Boolean)this.get(key);
        if(b != null)
            return b.booleanValue();
        return defaultValue;
    }
    
    public void putString(int key, String string)
    {
        if(string == null)
            this.remove(key);
        else
            this.put(key, string);
    }
    
    public String getString(int key)
    {
        return (String) this.get(key);
    }
    
    public String getString(int key, String defaultValue)
    {
        String value = (String) this.get(key);
        if(value == null)
            return defaultValue;
        return value;
    }
    
    public void putInt(int key, int value)
    {
        this.put(key, new Integer(value));
    }
    
    public int getInt(int key, int defaultValue)
    {
        Integer value = (Integer)this.get(key);
        if(value != null)
            return value.intValue();
        return defaultValue;
    }
    
    public void putObject(int key, Object value)
    {
        if(value == null)
            this.remove(key);
        else
            this.put(key, value);
    }
}
