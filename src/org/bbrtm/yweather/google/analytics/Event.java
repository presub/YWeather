package org.bbrtm.yweather.google.analytics;

import java.util.Random;
import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class Event implements Persistable
{
    
    public int    userId;
    public String accountId;
    public int    randomVal;
    public long   timestampFirst;
    public long   timestampPrevious;
    public long   timestampCurrent;
    public int    visits;
    public String category;
    public String action;
    public String label;
    public int    value;
    public int    screenWidth;
    public int    screenHeight;
    
    private Vector customVariables;
    
    public Event(int userId, String accountId, int randomVal, long timestampFirst, long timestampPrevious, long timestampCurrent, int visits, String category, String action, String label, int value,
            int screenWidth, int screenHeight)
    {
        this.userId = userId;
        this.accountId = accountId;
        this.randomVal = randomVal;
        this.timestampFirst = timestampFirst;
        this.timestampPrevious = timestampPrevious;
        this.timestampCurrent = timestampCurrent;
        this.visits = visits;
        this.category = category;
        this.action = action;
        this.label = label;
        this.value = value;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
        
        customVariables = new Vector(5);
    }
    
    public Event(int userId, String accountId, String category, String action, String label, int value, int screenWidth, int screenHeight)
    {
        this(userId, accountId, (new Random(System.currentTimeMillis()).nextInt()), -1, -1, -1, -1, category, action, label, value, screenWidth, screenHeight);
    }
    
    public String toString()
    {
        return "user id:" + this.userId + " " + "random:" + this.randomVal + " " + "timestampCurrent:" + this.timestampCurrent + " " + "timestampPrevious:" + this.timestampPrevious + " "
                + "timestampFirst:" + this.timestampFirst + " " + "visits:" + this.visits + " " + "value:" + this.value + " " + "category:" + this.category + " " + "action:" + this.action + " "
                + "label:" + this.label + " " + "width:" + this.screenWidth + " " + "height:" + this.screenHeight;
    }
    
    public long getTimestampFirst()
    {
        return timestampFirst;
    }
    
    public void setTimestampFirst(long timestampFirst)
    {
        this.timestampFirst = timestampFirst;
    }
    
    public long getTimestampPrevious()
    {
        return timestampPrevious;
    }
    
    public void setTimestampPrevious(long timestampPrevious)
    {
        this.timestampPrevious = timestampPrevious;
    }
    
    public long getTimestampCurrent()
    {
        return timestampCurrent;
    }
    
    public void setTimestampCurrent(long timestampCurrent)
    {
        this.timestampCurrent = timestampCurrent;
    }
    
    public int getVisits()
    {
        return visits;
    }
    
    public void setVisits(int visits)
    {
        this.visits = visits;
    }
    
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        
        if (!(obj instanceof Event))
            return false;
        
        if (this == obj)
            return true;
        
        Event e = (Event) obj;
        
        if (!this.accountId.equals(e.accountId))
            return false;
        if (!this.action.equals(e.action))
            return false;
        if (!this.category.equals(e.category))
            return false;
        if (!this.label.equals(e.label))
            return false;
        
        if (this.randomVal != e.randomVal)
            return false;
        if (this.screenHeight != e.screenHeight)
            return false;
        if (this.screenWidth != e.screenWidth)
            return false;
        
        if (this.timestampCurrent != e.timestampCurrent)
            return false;
        if (this.timestampFirst != e.timestampFirst)
            return false;
        if (this.timestampPrevious != e.timestampPrevious)
            return false;
        
        if (this.visits != e.visits)
            return false;
        if (this.userId != e.userId)
            return false;
        if (this.value != e.value)
            return false;
        
        return true;
    }
    
    public boolean hasCustomVariables()
    {
        return customVariables.size() > 0;
    }
    
    public CustomVariable[] getCustomVariables()
    {
        CustomVariable[] array = null;        
        array = new CustomVariable[customVariables.size()];
        customVariables.copyInto(array);
        customVariables.removeAllElements();        
        return array;
    }
    
    public void setCustomVariable(CustomVariable cv)
    {
        int size = customVariables.size();
        if (size < 5)
            customVariables.addElement(cv);
    }
}
