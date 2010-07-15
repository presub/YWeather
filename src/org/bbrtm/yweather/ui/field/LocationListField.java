package org.bbrtm.yweather.ui.field;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.component.ObjectListField;

public class LocationListField extends ObjectListField
{
    protected boolean navigationClick(int status, int time)
    {
        fieldChangeNotify(getSelectedIndex());
        return true;
    }
    
    protected boolean trackwheelClick(int status, int time)
    {
        fieldChangeNotify(getSelectedIndex());
        return true;
    }
    
    protected boolean keyChar(char key, int status, int time)
    {
        if(key == Characters.ENTER)
        {
            fieldChangeNotify(getSelectedIndex());
            return true;
        }
        return super.keyChar(key, status, time);
    }
    
    public boolean isDirty()
    {
        return false;
    }    
}
