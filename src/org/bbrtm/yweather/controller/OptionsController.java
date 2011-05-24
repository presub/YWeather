package org.bbrtm.yweather.controller;

import net.rim.device.api.ui.UiApplication;

import org.bbrtm.yweather.ui.screen.OptionsScreen;

public class OptionsController extends BaseController
{
    private OptionsScreen screen = null;
    
    public void refreshView()
    {
    }
    
    public void showView()
    {
        screen = new OptionsScreen(this);
        UiApplication.getUiApplication().pushScreen(screen);
    }
    
}
