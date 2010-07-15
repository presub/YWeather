package org.bbrtm.yweather.ui.screen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.NumericChoiceField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.component.SeparatorField;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.controller.OptionsController;
import org.bbrtm.yweather.controller.WeatherController;
import org.bbrtm.yweather.google.analytics.GoogleAnalyticsTracker;
import org.bbrtm.yweather.net.NetworkConfig;
import org.bbrtm.yweather.util.Configuration;

public class OptionsScreen extends BaseScreen implements FieldChangeListener
{
    private static Configuration config          = Configuration.getInstance();
    private OptionsController    controller      = null;
    
    private RadioButtonField     fButtonField    = null;
    private RadioButtonField     cButtonField    = null;
    private RadioButtonGroup     unitButtonGroup = null;
    
    private CheckboxField        backgroundField = null;
    private NumericChoiceField   intervalField   = null;
    
    private ObjectChoiceField    networkField    = null;
    private CheckboxField   wifiField = null;
    
    public OptionsScreen(OptionsController controller)
    {
        super();
        setTitle(resource.getString(YWeatherResource.OPTIONS_TITLE));
        this.controller = controller;
        
        backgroundField = new CheckboxField(resource.getString(YWeatherResource.OPTIONS_LABEL_BACKGROUND_UPDATES), config.isBackgroundEnabled());
        backgroundField.setChangeListener(this);
        add(backgroundField);
        
        intervalField = new NumericChoiceField(resource.getString(YWeatherResource.OPTIONS_LABEL_UDPATE_INTERVAL), 15, 720, 15, config.getUpdateInterval() / 15 - 1);
        
        if (config.isBackgroundEnabled())
        {
            add(intervalField);
        }
        
        add(new SeparatorField());
        
        unitButtonGroup = new RadioButtonGroup();
        
        fButtonField = new RadioButtonField(resource.getString(YWeatherResource.OPTIONS_LABEL_FAHRENHEIT), unitButtonGroup, config.getUnits().equals("f"));
        add(fButtonField);
        
        cButtonField = new RadioButtonField(resource.getString(YWeatherResource.OPTIONS_LABEL_CELSIUS), unitButtonGroup, config.getUnits().equals("c"));
        add(cButtonField);
        
        add(new SeparatorField());
        
        networkField = new ObjectChoiceField(resource.getString(YWeatherResource.OPTIONS_LABEL_NETWORK), NetworkConfig.choices, config.getConnectionMode());
        add(networkField);
        
        wifiField = new CheckboxField("WiFi enabled?", config.isWiFiEnabled());
        add(wifiField);
        
        GoogleAnalyticsTracker.getInstance().trackPageView("/options");
    }
    
    public OptionsController getController()
    {
        return this.controller;
    }
    
    public void fieldChanged(Field field, int context)
    {
        if (field == backgroundField)
        {
            if (backgroundField.getChecked())
                insert(intervalField, 1);
            else
                delete(intervalField);
        }
    }
    
    protected boolean onSave()
    {
        config.setBackgroundEnabled(backgroundField.getChecked());
        config.setUpdateInterval(intervalField.getSelectedValue());
        if (cButtonField.isSelected())
            config.setUnits("c");
        else
            config.setUnits("f");
        config.setConnectionMode(networkField.getSelectedIndex());
        config.setWiFiEnabled(wifiField.getChecked());
        
        Configuration.save();
        
        if (backgroundField.isDirty() || intervalField.isDirty())
        {
            WeatherController.getInstance().resetTimer();
        }
        
        if (cButtonField.isDirty() || fButtonField.isDirty())
        {
            WeatherController.getInstance().updateAll();
        }
        
        return true;
    }
}
