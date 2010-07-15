package org.bbrtm.yweather.controller;

import net.rim.device.api.ui.UiApplication;

import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.ui.screen.ForecastScreen;

public class ForecastController extends BaseController
{
    private ForecastScreen screen = null;
    private Place place = null;
    
    public ForecastController(Place place)
    {
        super();
        this.place = place;
    }
    
    public void refreshView()
    {
        // TODO Auto-generated method stub
        
    }
    
    public void showView()
    {
        screen = new ForecastScreen(this);  
        UiApplication.getUiApplication().pushScreen(screen);
    }
    
    public Place getPlace()
    {
        return this.place;
    }
    
}
