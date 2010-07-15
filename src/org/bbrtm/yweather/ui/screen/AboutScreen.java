package org.bbrtm.yweather.ui.screen;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.util.DeviceUtil;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;

public class AboutScreen extends BaseScreen
{
    private BitmapField logo    = null;
    private LabelField  about   = null;
    private LabelField  version = null;
    
    public AboutScreen()
    {
        super();
        
        setTitle(new LabelField(resource.getString(YWeatherResource.ABOUT_TITLE), USE_ALL_WIDTH | DrawStyle.HCENTER));
        
        version = new LabelField(resource.getString(YWeatherResource.ABOUT_CURRENT_VERSION) + " " + DeviceUtil.getAppVersion() + "\n", USE_ALL_WIDTH | DrawStyle.HCENTER);
        add(version);
        
        about = new LabelField(resource.getString(YWeatherResource.ABOUT_TEXT), USE_ALL_WIDTH | DrawStyle.HCENTER);
        add(about);
        
        logo = new BitmapField(Bitmap.getBitmapResource("yahoo_logo.png"), FIELD_HCENTER);
        add(logo);
        
        GoogleAnalyticsTracker.getInstance().trackPageView("/about");
    }
}
