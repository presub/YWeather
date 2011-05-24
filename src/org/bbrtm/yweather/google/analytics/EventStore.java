package org.bbrtm.yweather.google.analytics;

public interface EventStore
{
    public abstract void putEvent(Event paramEvent);

    public abstract Event[] peekEvents();

    public abstract Event[] peekEvents(int numOfEvents);
    
    public abstract Event[] popEvents(int numOfEvents);

    public abstract void deleteEvent(Event event);

    public abstract int getNumStoredEvents();

    public abstract int getStoreId();

    public abstract void setReferrer(String paramString);

    public abstract String getReferrer();

    public abstract void startNewVisit();
}
