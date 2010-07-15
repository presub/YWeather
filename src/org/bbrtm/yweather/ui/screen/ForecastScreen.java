package org.bbrtm.yweather.ui.screen;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.controller.ForecastController;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.model.Place;
import org.bbrtm.yweather.model.YahooWeather;
import org.bbrtm.yweather.model.YahooWeatherForecast;
import org.bbrtm.yweather.ui.field.FocusableLabelField;
import org.bbrtm.yweather.ui.manager.HalfHorizontalFieldManager;
import org.bbrtm.yweather.util.Configuration;

public class ForecastScreen extends BaseScreen
{
    private static Configuration       config             = Configuration.getInstance();
    private ForecastController         controller         = null;
    
    private LabelField                 updated            = null;
    
    // current conditions
    private HalfHorizontalFieldManager tempIconManger     = null;
    private LabelField                 currentTemp        = null;
    private BitmapField                currentIcon        = null;
    
    private LabelField                 currentCondition   = null;
    
    private VerticalFieldManager       detailManager      = null;
    
    private LabelField                 barometerField     = null;
    private LabelField                 humidityField      = null;
    private LabelField                 visibilityField    = null;
    private LabelField                 windField          = null;
    
    private LabelField                 sunriseField       = null;
    private LabelField                 sunsetField        = null;
    
    // forecasts
    private HalfHorizontalFieldManager forecastsManager   = null;
    private VerticalFieldManager       forecast1Manager   = null;
    private VerticalFieldManager       forecast2Manager   = null;
    
    // forecast 1
    private LabelField                 forecast1Date      = null;
    private BitmapField                forecast1Image     = null;
    private LabelField                 forecast1Condition = null;
    private LabelField                 forecast1Temp      = null;
    
    // forecast 2
    private LabelField                 forecast2Date      = null;
    private BitmapField                forecast2Image     = null;
    private LabelField                 forecast2Condition = null;
    private LabelField                 forecast2Temp      = null;
    
    private MenuItem                   toggleMenuItem     = null;
    private MenuItem                   viewMenuItem       = null;
    private boolean                    showDetails        = true;
    
    public ForecastScreen(ForecastController controller)
    {
        super(VERTICAL_SCROLL);
        this.controller = controller;
        GoogleAnalyticsTracker.getInstance().trackPageView("/forecast");
        showDetails = config.showDetails();
        Place place = controller.getPlace();
        YahooWeather weather = place.getWeather();
        YahooWeatherForecast forecast1 = (YahooWeatherForecast) weather.getForecasts().elementAt(0);
        YahooWeatherForecast forecast2 = (YahooWeatherForecast) weather.getForecasts().elementAt(1);
        
        setTitle(new LabelField(place.getName(), DrawStyle.HCENTER | USE_ALL_WIDTH));
        
        DateFormat format = DateFormat.getInstance(DateFormat.DATE_MEDIUM | DateFormat.TIME_MEDIUM);
        
        updated = new FocusableLabelField(format.formatLocal(weather.getDate()), USE_ALL_WIDTH | FIELD_HCENTER | DrawStyle.HCENTER | FOCUSABLE);
        add(updated);
        
        tempIconManger = new HalfHorizontalFieldManager(USE_ALL_WIDTH);
        
        Bitmap current = Bitmap.getBitmapResource(weather.getCurrentCode() + ".png");
        currentIcon = new BitmapField(current, USE_ALL_WIDTH | FIELD_HCENTER | DrawStyle.HCENTER);
        
        Font tempFont = getFont().derive(Font.PLAIN, current.getHeight(), Ui.UNITS_px);
        Font boldFont = getFont().derive(Font.BOLD);
        
        String temp = weather.getCurrentTemp();
        String feels = weather.getWindChill();
        if (temp.equals(feels))
        {
            temp = temp + "°";
        }
        else
        {
            temp = temp + "° (" + feels + "°)";
        }
        
        currentTemp = new FocusableLabelField(temp, USE_ALL_WIDTH | FIELD_HCENTER | DrawStyle.HCENTER);
        currentTemp.setFont(tempFont);
        
        tempIconManger.add(currentTemp);
        tempIconManger.add(currentIcon);
        
        add(tempIconManger);
        
        currentCondition = new FocusableLabelField(weather.getCurrentCondition(), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        add(currentCondition);
        
        detailManager = new VerticalFieldManager();
        
        detailManager.add(new SeparatorField());
        
        barometerField = new FocusableLabelField("Barometer: " + weather.getPressure() + weather.getPressureUnits() + " and " + weather.getRising(), FIELD_HCENTER | USE_ALL_WIDTH);
        detailManager.add(barometerField);
        
        humidityField = new FocusableLabelField("Humidity: " + weather.getHumidity() + "%", FIELD_HCENTER | USE_ALL_WIDTH);
        detailManager.add(humidityField);
        
        visibilityField = new FocusableLabelField("Visibility: " + weather.getVisibility() + weather.getDistanceUnits(), FIELD_HCENTER | USE_ALL_WIDTH);
        detailManager.add(visibilityField);
        
        windField = new FocusableLabelField("Wind: " + weather.getWindDirection() + " " + weather.getWindSpeed() + weather.getSpeedUnits(), FIELD_HCENTER | USE_ALL_WIDTH);
        detailManager.add(windField);
        
        sunriseField = new FocusableLabelField("Sunrise @ " + weather.getSunrise(), FIELD_HCENTER | USE_ALL_WIDTH);
        sunsetField = new FocusableLabelField("Sunset @ " + weather.getSunset(), FIELD_HCENTER | USE_ALL_WIDTH);
        detailManager.add(sunriseField);
        detailManager.add(sunsetField);
        
        if (showDetails)
            add(detailManager);
        
        add(new SeparatorField());
        
        forecastsManager = new HalfHorizontalFieldManager(USE_ALL_WIDTH);
        forecast1Manager = new VerticalFieldManager(USE_ALL_WIDTH);
        forecast2Manager = new VerticalFieldManager(USE_ALL_WIDTH);
        
        forecast1Date = new FocusableLabelField(forecast1.getDay(), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast1Date.setFont(boldFont);
        forecast1Manager.add(forecast1Date);
        
        forecast1Image = new BitmapField(Bitmap.getBitmapResource(forecast1.getCode() + ".png"), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast1Manager.add(forecast1Image);
        
        forecast1Condition = new FocusableLabelField(forecast1.getCondition(), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast1Manager.add(forecast1Condition);
        
        forecast1Temp = new FocusableLabelField(forecast1.getHigh() + "° / " + forecast1.getLow() + "°", FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast1Manager.add(forecast1Temp);
        
        forecast2Date = new FocusableLabelField(forecast2.getDay(), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast2Date.setFont(boldFont);
        forecast2Manager.add(forecast2Date);
        
        forecast2Image = new BitmapField(Bitmap.getBitmapResource(forecast2.getCode() + ".png"), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast2Manager.add(forecast2Image);
        
        forecast2Condition = new FocusableLabelField(forecast2.getCondition(), FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast2Manager.add(forecast2Condition);
        
        forecast2Temp = new FocusableLabelField(forecast2.getHigh() + "° / " + forecast2.getLow() + "°", FIELD_HCENTER | USE_ALL_WIDTH | DrawStyle.HCENTER);
        forecast2Manager.add(forecast2Temp);
        
        forecastsManager.add(forecast1Manager);
        forecastsManager.add(forecast2Manager);
        
        add(forecastsManager);
        
        add(new NullField(FOCUSABLE));
        
        toggleMenuItem = new MenuItem(resource.getString(YWeatherResource.MENU_ITEM_TOGGLE_DETAILS), 490000, 100)
        {
            public void run()
            {
                toggleDetails();
            }
        };
        addMenuItem(toggleMenuItem);
        
        viewMenuItem = new MenuItem(resource.getString(YWeatherResource.MENU_ITEM_VIEW_YAHOO_FORECAST), 450000, 1000)
        {
            public void run()
            {
                openYahooWeather();
            }
        };
        addMenuItem(viewMenuItem);
    }
    
    public ForecastController getController()
    {
        return controller;
    }
    
    protected void openYahooWeather()
    {       
        try {
            String link = getController().getPlace().getWeather().getLink();
            Browser.getDefaultSession().displayPage(link);
        }catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected boolean keyChar(char c, int status, int time)
    {
        if (c == 't')
        {
            this.scroll(Manager.TOPMOST);
            return true;
        }
        else if (c == 'b')
        {
            this.scroll(Manager.BOTTOMMOST);
            return true;
        }
        else if (c == 'd')
        {
            toggleDetails();
            return true;
        }
        return super.keyChar(c, status, time);
    }
    
    private void toggleDetails()
    {
        showDetails = !showDetails;
        config.setDetails(showDetails);
        if (showDetails)
            insert(detailManager, 3);
        else
            delete(detailManager);
    }
}
