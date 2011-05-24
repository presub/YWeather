package org.bbrtm.yweather.google.analytics;

import java.util.Timer;
import java.util.TimerTask;

import net.rim.device.api.system.Display;

import org.bbrtm.yweather.util.Logger;

public class GoogleAnalyticsTracker
{
    private static Logger       log         = Logger.getInstance();
    
    public static final String                  VERSION     = "1.0";
    private static final GoogleAnalyticsTracker INSTANCE    = new GoogleAnalyticsTracker();
    public static final String                  TRACKER_TAG = "googleanalytics";
    private String                              accountId;
    private int                                 dispatchPeriod;
    private EventStore                          eventStore;
    private Dispatcher                          dispatcher;
    
    private Timer                               timer;
    
    private TimerTask                           dispatcherTask;
    
    public static final String PAGEVIEW_EVENT_CATEGORY = "__##GOOGLEPAGEVIEW##__";
    public static final String INSTALL_EVENT_CATEGORY  = "__##GOOGLEINSTALL##__";
    
    
    private GoogleAnalyticsTracker()
    {
        accountId = "UA-139752-11";
        start(accountId, 60 * 60);
    }
    
    public static GoogleAnalyticsTracker getInstance()
    {
        return INSTANCE;
    }
    
    private void start(String accountId, int dispatchPeriod)
    {
        if (this.eventStore == null)
            this.eventStore = PersistentEventStore.getInstance();
        
        if (this.dispatcher == null)
            this.dispatcher = new NetworkDispatcher();
        
        start(accountId, dispatchPeriod, this.eventStore, this.dispatcher);
    }
    
    private void start(String accountId)
    {
        start(accountId, -1);
    }
    
    private void start(String accountId, int dispatchPeriod, EventStore eventStore, Dispatcher dispatcher)
    {
        log.debug("starting for account: " + accountId + "\n\ndispatch period: " + dispatchPeriod);
        this.accountId = accountId;
        this.eventStore = eventStore;
        this.eventStore.startNewVisit();
        this.dispatcher = dispatcher;
        this.dispatcher.init(this.eventStore.getReferrer());
        timer = new Timer();
        dispatcherTask = new TimerTask()
        {
            public void run()
            {
                dispatch();
            }
        };
        
        setDispatchPeriod(dispatchPeriod);
    }
    
    public void trackEvent(String category, String action, String label, int value)
    {
        try
        {
            log.debug("track event: " + category + " action: " + action + " label: " + label + " value: " + value);
            addEvent(createEvent(this.accountId, category, action, label, value));
        }
        catch (Exception e)
        {
            log.exception("Exception tracking event", e);
        }
    }
    
    public void trackPageView(String page)
    {
        try
        {
            log.debug("creating page view: " + page);
            addEvent(createEvent(this.accountId, PAGEVIEW_EVENT_CATEGORY, page, null, -1));
        }
        catch (Exception e)
        {
            log.exception("exception tracking page view", e);
        }
    }
    
    public Event createPageView(String page)
    {
        return createEvent(this.accountId, PAGEVIEW_EVENT_CATEGORY, page, null, -1);
    }
    
    public Event createEvent(String category, String action, String label, int value)
    {
        return createEvent(this.accountId, category, action, label, value);
    }
    
    private Event createEvent(String accountId, String category, String action, String label, int value)
    {
        Event localEvent = new Event(this.eventStore.getStoreId(), accountId, category, action, label, value, Display.getWidth(), Display.getHeight());
        return localEvent;
    }
    
    public void addEvent(Event event)
    {
        this.eventStore.putEvent(event);
    }
    
    public void setDispatchPeriod(int dispatchPeriod)
    {
        this.dispatchPeriod = dispatchPeriod;
        if (dispatchPeriod <= 0)
        {
            timer.cancel();
        }
        else
        {
            cancelPendingDispatches();
            timer.scheduleAtFixedRate(dispatcherTask, dispatchPeriod * 1000, dispatchPeriod * 1000);
        }
    }
    
    private void cancelPendingDispatches()
    {
        log.debug("cancel pending dispatches");
        if (timer != null)
            timer.cancel();
        timer = new Timer();
    }
    
    public void dispatch()
    {
        log.debug("dispatch");
        int eventsBeingDispatched = this.eventStore.getNumStoredEvents();
        log.debug("events to be dispatched: " + eventsBeingDispatched);
        if (eventsBeingDispatched != 0)
        {
            Event[] arrayOfEvent = this.eventStore.popEvents(eventsBeingDispatched);
            this.dispatcher.dispatchEvents(arrayOfEvent);
        }
    }
    
    public void stop()
    {
        log.debug("stopping");
        this.dispatcher.stop();
        cancelPendingDispatches();
    }
}
