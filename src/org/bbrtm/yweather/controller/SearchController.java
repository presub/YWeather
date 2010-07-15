package org.bbrtm.yweather.controller;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.net.Network;
import org.bbrtm.yweather.net.NetworkCallback;
import org.bbrtm.yweather.ui.dialog.StatusScreen;
import org.bbrtm.yweather.ui.screen.LocationScreen;
import org.bbrtm.yweather.ui.screen.SearchScreen;
import org.bbrtm.yweather.util.Configuration;
import org.bbrtm.yweather.util.Logger;
import org.bbrtm.yweather.util.URLUTF8Encoder;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class SearchController extends BaseController implements FieldChangeListener
{
    private static Logger    log          = Logger.getInstance();
    private String           location     = null;
    private int              start        = 0;
    private static final int count        = 20;
    private int              total        = -1;
    
    private Place[]          places       = null;
    
    private SearchScreen     screen       = null;
    private StatusScreen     statusScreen = null;
    
    private GoogleAnalyticsTracker tracker = null;
    
    public SearchController(String location)
    {
        super();
        this.location = location;
        tracker = GoogleAnalyticsTracker.getInstance();
    }
    
    public void refreshView()
    {
        screen.setLocations(getPlaces());
        if(start < total)
            screen.showLoadMoreButton();
        else
            screen.hideLoadMoreButton();
    }
    
    public void showView()
    {
        screen = new SearchScreen(this);
        UiApplication.getUiApplication().pushScreen(screen);
    }
    
    public void showStatusScreen()
    {
        UiApplication.getUiApplication().invokeLater(new Runnable()
        {
            public void run()
            {
                statusScreen = new StatusScreen(resource.getString(YWeatherResource.DIALOG_SEARCHING));
                UiApplication.getUiApplication().pushScreen(statusScreen);
            }
        });        
    }
    
    public String getLocation()
    {
        return this.location;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
        start = 0;
        places = null;
    }
    
    public void setTotal(int total)
    {
        this.total = total;
    }
    
    public int getTotal()
    {
        return this.total;
    }
    
    public int getStart()
    {
        return this.start;
    }
    
    public void setStart(int start)
    {
        this.start = start;
    }
    
    public Place[] getPlaces()
    {
        return this.places;
    }
    
    public void setPlaces(Place[] places)
    {
        if(this.places == null)
        {
            this.places = places;
            return;
        }
        
        Place[] newPlaces = new Place[this.places.length + places.length];
        
        int length = this.places.length;
        for(int x = 0; x < length; ++x)
            newPlaces[x] = this.places[x];
        
        for(int x = 0; x < places.length; ++x)
            newPlaces[length + x] = places[x];
        
        this.places = newPlaces;
    }
    
    public void startSearch()
    {
        showStatusScreen();
        
        log.debug("Searching for: " + this.location);
        StringBuffer urlBuffer = new StringBuffer("http://where.yahooapis.com/v1/places.q(");
        urlBuffer.append(URLUTF8Encoder.encode(this.location));
        urlBuffer.append(");count=");
        urlBuffer.append(count);
        urlBuffer.append(";start=");
        urlBuffer.append(this.start);
        urlBuffer.append("?format=json&appid=");
        urlBuffer.append(resource.getString(YWeatherResource.APP_ID));
        
        String url = urlBuffer.toString();
        log.debug(url);
        
        Network network = new Network();
        network.doGet(url, new NetworkCallback()
        {
            public void onNetworkRequestSuccessful(Object reply)
            {
                log.debug((String) reply);
                try
                {
                    JSONObject response = new JSONObject((String) reply);
                    JSONObject places = response.getJSONObject("places");
                    if (places.has("place"))
                    {
                        JSONArray place = places.getJSONArray("place");
                        int placeCount = place.length();
                        Place[] placeArray = new Place[placeCount];
                        
                        for (int x = 0; x < placeCount; ++x)
                        {
                            placeArray[x] = new Place();
                            JSONObject aPlace = place.getJSONObject(x);
                            placeArray[x].setWoeid(aPlace.getString("woeid"));
                            placeArray[x].setName(aPlace.getString("name"));
                            placeArray[x].setCountry(aPlace.getString("country"));
                            placeArray[x].setAdmin1(aPlace.getString("admin1"));
                            placeArray[x].setAdmin2(aPlace.getString("admin2"));
                            
                        }
                        setPlaces(placeArray);
                    }
                    int total = places.getInt("total");
                    int c = places.getInt("count");
                    setTotal(total);
                    int start = places.getInt("start");
                    setStart(start + c);
                    
                }
                catch (JSONException e)
                {
                    log.exception("Exception in search response", e);
                }
                
                UiApplication.getUiApplication().invokeLater(new Runnable()
                {
                    public void run()
                    {
                        UiApplication.getUiApplication().popScreen(statusScreen);
                        refreshView();
                    }
                });
            }
            
            public void onNetworkRequestFailed(String message, Exception e)
            {
                UiApplication.getUiApplication().invokeLater(new Runnable()
                {
                    public void run()
                    {
                        UiApplication.getUiApplication().popScreen(statusScreen);
                        Dialog.alert(resource.getString(YWeatherResource.DIALOG_SEARCH_FAILED));
                    }
                });
            }
        });
    }
    
    public void fieldChanged(Field field, int context)
    {
        String cookie = (String) field.getCookie();
        if (cookie != null && cookie.equals("list") && context >= 0)
        {
            tracker.trackEvent("Search", "Add Location", null, -1);
            Place place = places[context];
            Configuration config = Configuration.getInstance();
            Vector p = config.getPlaces();
            if(p == null)
                p = new Vector(1);
            if(p.size() == 0)
                place.setHomescreen(true);
            p.addElement(place);
            config.setPlaces(p);
            Configuration.save();
            LocationScreen locationScreen = (LocationScreen) screen.getScreenBelow();
            locationScreen.getController().refreshView();
            UiApplication.getUiApplication().popScreen(screen);
            WeatherController.getInstance().updateWeather(place);
        }  
        else if (cookie != null && cookie.equals("load"))
        {
            startSearch();
        }
    }
}
