package org.bbrtm.yweather.google.analytics;

import java.util.Random;
import java.util.Vector;

import net.rim.device.api.system.NonPersistableObjectException;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import org.bbrtm.yweather.util.ConfigurationHashtable;
import org.bbrtm.yweather.util.Logger;
import org.bbrtm.yweather.util.Queue;

public class PersistentEventStore implements EventStore
{
    private static Logger           log          = Logger.getInstance();
    private static EventStore       INSTANCE     = null;
    private ConfigurationHashtable  hashtable    = null;
    private static final long       STOREHASH    = 0x6b94b254b7d12dacL; // org.bbrtm.yweather.google.analytics.PersistentEventStore
    private static PersistentObject persist;
    
    private static final int        KEY_REFERRER = 1;
    private static final int        KEY_SESSION  = 2;
    private static final int        KEY_EVENTS   = 3;
    
    private static final int        MAX_EVENTS   = 1000;
    private int                     storeId;
    private long                    timestampFirst;
    private long                    timestampPrevious;
    private long                    timestampCurrent;
    private int                     visits;
    private boolean                 sessionUpdated;
    
    private PersistentEventStore()
    {
        persist = PersistentStore.getPersistentObject(STOREHASH);
        Object contents = persist.getContents();
        if (contents instanceof ConfigurationHashtable)
        {
            try
            {
                hashtable = (ConfigurationHashtable) contents;
            }
            catch (Exception ex)
            {
                hashtable = new ConfigurationHashtable();
                persist.setContents(hashtable);
                persist.commit();
            }
        }
        else
        {
            hashtable = new ConfigurationHashtable();
            persist.setContents(hashtable);
            persist.commit();
        }
    }
    
    public static PersistentEventStore getInstance()
    {
        if (INSTANCE == null)
            INSTANCE = new PersistentEventStore();
        return (PersistentEventStore) INSTANCE;
    }
    
    public static void save()
    {
        try
        {
            persist.commit();
        }
        catch (NonPersistableObjectException e)
        {
            log.exception("error persisting google analytics", e);
        }
    }
    
    public void deleteEvent(Event event)
    {
        Queue events = getEvents();
        events.removeElement(event);
        PersistentEventStore.save();
    }
    
    private Event[] vectorToArray(Vector events)
    {
        Event[] eventArray = new Event[events.size()];
        events.copyInto(eventArray);
        return eventArray;
    }
    
    public Event[] popEvents(int numOfEvents)
    {
        Queue queue = getEvents();
        int max = (numOfEvents > getNumStoredEvents()) ? getNumStoredEvents() : numOfEvents;
        Event[] events = new Event[max];
        
        for (int x = 0; x < max; ++x)
            events[x] = (Event) queue.dequeue();
        setEvents(queue);
        PersistentEventStore.save();
        return events;
    }
    
    public Event[] peekEvents()
    {
        return peekEvents(MAX_EVENTS);
    }
    
    public Event[] peekEvents(int numOfEvents)
    {
        
        Queue events = getEvents();
        if (events.size() <= numOfEvents)
            return vectorToArray(events);
        
        Event[] eArray = new Event[numOfEvents];
        for (int x = 0; x < numOfEvents; ++x)
        {
            eArray[x] = (Event) events.elementAt(x);
        }
        return eArray;
    }
    
    public void putEvent(Event event)
    {
        Queue events = getEvents();
        if (events.size() >= MAX_EVENTS)
        {
            log.warn("Store full. Not storing last event.");
            return;
        }
        if (!(this.sessionUpdated))
            storeUpdatedSession();
        
        event.setTimestampCurrent(this.timestampCurrent);
        event.setTimestampFirst(this.timestampFirst);
        event.setTimestampPrevious(this.timestampPrevious);
        event.setVisits(this.visits);
        
        log.debug("pushing event: " + event);
        events.queue(event);
        setEvents(events);
        PersistentEventStore.save();
    }
    
    public int getNumStoredEvents()
    {
        return getEvents().size();
    }
    
    public int getStoreId()
    {
        return this.storeId;
    }
    
    public void startNewVisit()
    {
        log.debug("start new visit");
        this.sessionUpdated = false;
        
        Session session = getSession();
        
        if (session == null)
        {
            long l = System.currentTimeMillis() / 1000L;
            this.timestampFirst = l;
            this.timestampCurrent = l;
            this.timestampPrevious = l;
            this.visits = 1;
            this.storeId = (new Random(System.currentTimeMillis()).nextInt() & 0x7FFFFFFF);
            session = new Session(l, l, l, 1, this.storeId);
            setSession(session);
            PersistentEventStore.save();
        }
        else
        {
            this.timestampFirst = session.getTimestampFirst();
            this.timestampCurrent = (System.currentTimeMillis() / 1000L);
            this.timestampPrevious = session.getTimestampCurrent();
            this.visits = session.getVisits() + 1;
            this.storeId = session.getStoreId();
        }
        
    }
    
    private void storeUpdatedSession()
    {
        log.debug("update session");
        Session session = getSession();
        if (session != null)
        {
            session.setTimestampCurrent(this.timestampCurrent);
            session.setTimestampPrevious(this.timestampPrevious);
            session.setVisits(this.visits);
            this.sessionUpdated = true;
        }
        setSession(session);
        PersistentEventStore.save();
    }
    
    public void setReferrer(String paramString)
    {
        hashtable.putString(KEY_REFERRER, paramString);
    }
    
    public String getReferrer()
    {
        return hashtable.getString(KEY_REFERRER);
    }
    
    public void setSession(Session session)
    {
        hashtable.putObject(KEY_SESSION, session);
    }
    
    public Session getSession()
    {
        return (Session) hashtable.get(KEY_SESSION);
    }
    
    public void setEvents(Queue events)
    {
        hashtable.putObject(KEY_EVENTS, events);
    }
    
    public Queue getEvents()
    {
        Object e = hashtable.get(KEY_EVENTS);
        if (!(e instanceof Queue) || e == null)
            return new Queue();
        Queue events = (Queue) e;
        return events;
    }
}
