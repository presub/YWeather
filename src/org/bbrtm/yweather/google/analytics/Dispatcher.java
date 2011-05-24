package org.bbrtm.yweather.google.analytics;

public interface Dispatcher
{
    public static final int MSG_EVENT_DISPATCHED = 6178583;
    public static final int MSG_DISPATCHED_FINISHED = 13651479;
    
    public abstract void dispatchEvents(Event[] paramArrayOfEvent);
    
    public abstract void init(String referrer);

    public abstract void stop(); 
}
