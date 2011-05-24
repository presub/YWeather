package org.bbrtm.yweather.controller;

import net.rim.device.api.i18n.ResourceBundle;

import org.bbrtm.yweather.YWeatherResource;

public abstract class BaseController
{
    protected static ResourceBundle resource = null;
    
    static
    {
        resource = ResourceBundle.getBundle(YWeatherResource.BUNDLE_ID, YWeatherResource.BUNDLE_NAME);
    }
    
    public abstract void showView();
    
    public abstract void refreshView();
}
