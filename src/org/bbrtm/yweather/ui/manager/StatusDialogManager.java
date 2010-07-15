package org.bbrtm.yweather.ui.manager;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

public class StatusDialogManager extends Manager
{    
    public StatusDialogManager()
    {
        super(0);
    }
    
    protected void sublayout(int width, int height)
    {        
        int fieldCount = getFieldCount();
        
        int xOffset = 0;
        
        for (int x = 0; x < fieldCount; ++x)
        {
            Field field = getField(x);
            
            layoutChild(field, width, height);
            
        }
        
        Field logo = getField(0);
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        
        Field label = getField(1);
        int labelWidth = label.getWidth();
        int labelHeight = label.getHeight();
        
        int maxHeight = Math.max(labelHeight, logoHeight);
        
        int yPosition = (maxHeight - logoHeight) / 2;
        
        setPositionChild(logo, xOffset, yPosition);
        
        xOffset = xOffset + logoWidth + 15;
        
        yPosition = (maxHeight - labelHeight) / 2;
        
        setPositionChild(label, xOffset, yPosition);
        
        xOffset = xOffset + labelWidth;
        
        setExtent(xOffset, maxHeight + 2);
    }
    
}
