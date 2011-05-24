package org.bbrtm.yweather.ui.dialog;

import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ActiveAutoTextEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.bbrtm.yweather.YWeatherResource;

public class RenameDialog extends PopupScreen implements FieldChangeListener
{
    private ActiveAutoTextEditField nameField     = null;
    private LabelField              promptField       = null;
    private ButtonField             saveButtonField = null;
    private String name = null;
    
    private static ResourceBundle   resource          = null;
    static
    {
        resource = ResourceBundle.getBundle(YWeatherResource.BUNDLE_ID, YWeatherResource.BUNDLE_NAME);
    }
    
    public RenameDialog(String name) 
    {
        super(new VerticalFieldManager());
        this.name = name;
        
        promptField = new LabelField(resource.getString(YWeatherResource.DIALOG_RENAME_PROMPT));
        add(promptField);
        
        nameField = new ActiveAutoTextEditField("", name);
        nameField.setCursorPosition(name.length());
        add(nameField);
        
        saveButtonField = new ButtonField(resource.getString(YWeatherResource.DIALOG_RENAME_SAVE_BUTTON_LABEL), ButtonField.CONSUME_CLICK | Field.FIELD_HCENTER);
        saveButtonField.setChangeListener(this);
        add(saveButtonField);
    }
    
    public String getName()
    {
        return nameField.getText();
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
            nameField.setText(name);
            close();
            return true;
        }
        return super.keyChar(c, status, time);
    }
}