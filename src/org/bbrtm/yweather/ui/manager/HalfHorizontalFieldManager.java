package org.bbrtm.yweather.ui.manager;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;

public class HalfHorizontalFieldManager extends Manager
{
    private int gutter = 10;

    public HalfHorizontalFieldManager(long style)
    {
        super(style);
    }
    
    public HalfHorizontalFieldManager()
    {
        super(Field.USE_ALL_WIDTH);
    }
    
    protected void sublayout(int width, int height)
    {
        // get the left and right fields
        Field leftField = getField(0);
        Field rightField = getField(1);
        
        // calculate the width of the column
        int halfWidth = (width - gutter) / 2;
        
        // layout the left field
        layoutChild(leftField, halfWidth, height);
        setPositionChild(leftField, 0, 0);
        
        // layout the right field
        layoutChild(rightField, halfWidth, height);
        setPositionChild(rightField, halfWidth + gutter, 0);
        
        
        // set the extent of this field
        setExtent(width, Math.max(leftField.getHeight(), rightField.getHeight()));
    }
    
    public int getPreferredWidth()
    {
        return Display.getWidth();
    }
    
    public int getPreferredHeight()
    {
        Field leftField = getField(0);
        Field rightField = getField(1);
        
        return Math.max(leftField.getPreferredHeight(), rightField.getPreferredHeight());
    }
    
    public int getGutter()
    {
        return this.gutter;
    }
    
    public void setGutter(int gutter)
    {
        this.gutter = gutter;
    }
}
