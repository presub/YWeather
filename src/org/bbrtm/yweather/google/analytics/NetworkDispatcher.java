package org.bbrtm.yweather.google.analytics;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Stack;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import org.bbrtm.yweather.net.NetworkConfig;
import org.bbrtm.yweather.util.DeviceUtil;
import org.bbrtm.yweather.util.Logger;

public class NetworkDispatcher implements Dispatcher
{
    private static Logger       log                     = Logger.getInstance();
    
    private static final String GOOGLE_ANALYTICS_HOST   = "www.google-analytics.com";
    private static final String GOOGLE_ANALYTICS_URL    = "http://www.google-analytics.com";
    private final String        userAgent;
    
    private static final int    MAX_EVENTS_PER_PIPELINE = 30;
    private static final int    MAX_SEQUENTIAL_REQUESTS = 5;
    private static final long   MIN_RETRY_INTERVAL      = 2L;
    
    private DispatcherThread    dispatcherThread;
    
    public NetworkDispatcher()
    {        
        StringBuffer buffer = new StringBuffer();
        
        buffer.append("BlackBerry");
        buffer.append(DeviceUtil.getDeviceName());
        buffer.append("/");
        buffer.append(DeviceUtil.getDeviceOS());
        
        this.userAgent = buffer.toString();
        log.info("user-agent: " + this.userAgent);
    }
    
    public void init(String referrer)
    {
        stop();
        this.dispatcherThread = new DispatcherThread(referrer, this.userAgent);
        this.dispatcherThread.start();
    }
    
    public void dispatchEvents(Event[] events)
    {
        this.dispatcherThread.dispatchEvents(events);
    }
    
    public void stop()
    {
        if ((this.dispatcherThread == null))
            return;
        this.dispatcherThread.stop();
    }
    
    private static class DispatcherThread extends Thread
    {
        private boolean      running;
        private final String referrer;
        private final String userAgent;
        private int          lastStatusCode;
        private int          maxEventsPerRequest;
        private long         retryInterval;
        private final Stack  eventQueue = new Stack();
        private Hashtable    headers;
        private int          eventsPerRequest;
        
        private DispatcherThread(String referrer, String userAgent)
        {
            super("DispatcherThread");
            this.maxEventsPerRequest = MAX_EVENTS_PER_PIPELINE;
            this.referrer = referrer;
            this.userAgent = userAgent;
            this.running = false;
            this.eventsPerRequest = 0;
            this.headers = new Hashtable(2);
            headers.put("User-Agent", this.userAgent);
            headers.put("Host", GOOGLE_ANALYTICS_HOST);
        }
        
        public void dispatchEvents(Event[] events)
        {
            log.debug("dispatching events");
            for (int i = 0; i < events.length; ++i)
            {
                eventQueue.push(events[i]);
            }
        }
        
        public synchronized void start()
        {
            log.debug("start dispatch thread");
            this.running = true;
            super.start();
        }
        
        public void stop()
        {
            log.debug("stopping dispatch thread");
            running = false;
        }
        
        public boolean isRunning()
        {
            return this.running;
        }
        
        public void run()
        {
            HttpConnection con = null;
            OutputStream os = null;
            InputStream is = null;
            
            while (running)
            {
                if (! eventQueue.empty())
                {
                    final Event event = (Event) eventQueue.pop();
                    log.debug("dispatching event: \n\n" + event.toString());
                    String url = GOOGLE_ANALYTICS_URL;
                    if (event.category.equals(GoogleAnalyticsTracker.PAGEVIEW_EVENT_CATEGORY))
                        url += NetworkRequestUtil.constructPageviewRequestPath(event, this.referrer);
                    else
                        url += NetworkRequestUtil.constructEventRequestPath(event, this.referrer);
                    url += NetworkConfig.getConnectionParameters();
                    log.debug(url);
                    try 
                    {
                        if(!NetworkConfig.isDataConnectionAvailable())
                            throw new Exception("no data connection");
                        log.debug("creating http connection");
                        con = (HttpConnection) Connector.open(url);
                        con.setRequestMethod(HttpConnection.GET);
                        con.setRequestProperty("User-Agent", this.userAgent);
                        con.setRequestProperty("Host", GOOGLE_ANALYTICS_HOST);
                        int status = con.getResponseCode();   
                        log.debug("http return status code: " + status);
                    }
                    catch (Exception e) {
                        log.exception("exception dispatching event", e);
                        try
                        {
                            Thread.sleep(60 * 1000);
                        }
                        catch (InterruptedException e1)
                        {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        eventQueue.push(event);
                    }
                }
                else
                {
                    try
                    {
                        Thread.sleep(60 * 1000);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}
