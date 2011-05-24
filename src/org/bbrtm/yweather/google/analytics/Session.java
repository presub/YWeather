package org.bbrtm.yweather.google.analytics;

import net.rim.device.api.util.Persistable;

public class Session implements Persistable
{
    private long timestampFirst;
    private long timestampPrevious;
    private long timestampCurrent;
    private int visits;
    private int storeId;
    
    public Session(long timestampFirst, long timestampPrevious, long timestampCurrent, int visits, int storeId)
    {
        super();
        this.timestampFirst = timestampFirst;
        this.timestampPrevious = timestampPrevious;
        this.timestampCurrent = timestampCurrent;
        this.visits = visits;
        this.storeId = storeId;
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
    
    public int getStoreId()
    {
        return storeId;
    }
    
    public void setStoreId(int storeId)
    {
        this.storeId = storeId;
    }
    
}
