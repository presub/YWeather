package org.bbrtm.yweather.ui.dialog;

import org.bbrtm.yweather.YWeatherResource;

import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ActiveAutoTextEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class AddLocationDialog extends PopupScreen implements FieldChangeListener
{
    private ActiveAutoTextEditField locationField     = null;
    private LabelField              promptField       = null;
    private ButtonField             searchButtonField = null;
    
    private static ResourceBundle   resource          = null;
    static
    {
        resource = ResourceBundle.getBundle(YWeatherResource.BUNDLE_ID, YWeatherResource.BUNDLE_NAME);
    }
    
    public AddLocationDialog() 
    {
        super(new VerticalFieldManager());
        
        promptField = new LabelField(resource.getString(YWeatherResource.DIALOG_ADD_LOCATION_PROMPT));
        add(promptField);
        
        locationField = new ActiveAutoTextEditField("", "");
        add(locationField);
        
        searchButtonField = new ButtonField(resource.getString(YWeatherResource.DIALOG_ADD_LOCATION_SEARCH_BUTTON_LABEL), ButtonField.CONSUME_CLICK | Field.FIELD_HCENTER);
        searchButtonField.setChangeListener(this);
        add(searchButtonField);
    }
    
    public String getLocation()
    {
        return locationField.getText();
    }

    public void fieldChanged(Field field, int context)
    {
        close();
    }
    
    protected boolean keyChar(char c, int status, int time)
    {
        if(c == Characters.ENTER)
        {
            close();
            return true;
        }
        else if(c == Characters.ESCAPE)
        {
            locationField.setText("");
            close();
            return true;
        }
        return super.keyChar(c, status, time);
    }
}
