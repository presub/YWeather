package org.bbrtm.yweather.ui.dialog;

import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.PopupScreen;

import org.bbrtm.yweather.ui.field.AnimatedGIFField;
import org.bbrtm.yweather.ui.manager.StatusDialogManager;

public class StatusScreen extends PopupScreen
{
    private AnimatedGIFField indicatorBitmapField = null;
    private LabelField       messageLabelField    = null;
    
    public StatusScreen(String message)
    {
        super(new StatusDialogManager());
        
        indicatorBitmapField = new AnimatedGIFField((GIFEncodedImage) EncodedImage.getEncodedImageResource("ajax-loader.gif"));
        add(indicatorBitmapField);
        
        messageLabelField = new LabelField(message)
        {
            protected void paint(Graphics graphics)
            {
                graphics.setColor(Color.WHITE);
                super.paint(graphics);
            }
        };
        add(messageLabelField);
        
    }
    
    protected void sublayout(int width, int height)
    {
        layoutDelegate(width, height);
        setPositionDelegate(10, 10);
        
        int delWidth = getDelegate().getWidth();
        int delHeight = getDelegate().getHeight();
        
        setExtent(delWidth + 20, delHeight + 20);
        
        setPosition((width - getWidth()) / 2, (height - getHeight()) / 2);
        
    }
    
    protected void paint(Graphics graphics)
    {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        super.paint(graphics);
    }
    
    protected boolean navigationClick(int status, int time)
    {
        return true;
    }
    
    protected boolean keyChar(char c, int status, int time)
    {
        return true;
    }
}
