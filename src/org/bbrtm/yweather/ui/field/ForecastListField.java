package org.bbrtm.yweather.ui.field;

import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ObjectListField;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.model.Place;

public class ForecastListField extends ObjectListField
{
    private boolean               hasFocus    = false;
    Bitmap                        homeFocus   = null;
    Bitmap                        homeUnfocus = null;
    
    private static ResourceBundle resources   = null;
    static
    {
        resources = ResourceBundle.getBundle(YWeatherResource.BUNDLE_ID, YWeatherResource.BUNDLE_NAME);
    }
    
    public ForecastListField()
    {
        super();
        
        homeUnfocus = Bitmap.getBitmapResource("house_black.png");
        homeFocus = Bitmap.getBitmapResource("house_white.png");
        
        int height = getFont().getHeight();
        
        setRowHeight(height * 2 + 15);
    }
    
    public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width)
    {
        Place place = (Place) this.get(this, index);
        boolean updating = place.getWeather() == null;
        boolean selected = (index == getSelectedIndex() && hasFocus);
        
        int rowHeight = this.getRowHeight();
        int fontHeight = getFont().getHeight();
        int yOffset = y + 5;
        int xOffset = width;
        
        if (!updating)
        {
            Bitmap icon = Bitmap.getBitmapResource(place.getWeather().getCurrentCode() + ".png");
            yOffset = (rowHeight - icon.getHeight()) / 2 + y;
            xOffset = width - 5 - icon.getWidth();
            graphics.drawBitmap(xOffset, yOffset, icon.getWidth(), icon.getHeight(), icon, 0, 0);
            
            Font tempFont = getFont().derive(Font.PLAIN, rowHeight - 10, Ui.UNITS_px);
            String temp = place.getWeather().getCurrentTemp() + "°";
            int advance = tempFont.getAdvance(temp);
            
            xOffset = xOffset - advance - 2;
            yOffset = y + 5;
            
            graphics.setFont(tempFont);
            graphics.setColor(selected ? Color.WHITE : Color.BLACK);
            graphics.drawText(temp, xOffset, yOffset);
            graphics.setFont(getFont());
        }
        
        yOffset = y + 5;
        graphics.drawText(place.getName(), 5, yOffset, DrawStyle.ELLIPSIS, xOffset);
        
        yOffset += fontHeight + 5;
        graphics.setColor(selected ? Color.WHITE : Color.GRAY);
        if (!selected && place.getWeather() != null && place.getWeather().getCurrentCode() != null && place.getWeather().getCurrentCode().equals("3200"))
            graphics.setColor(Color.RED);
        graphics.drawText(updating ? resources.getString(YWeatherResource.WEATHER_UPDATING) : place.getWeather().getCurrentCondition(), 5, yOffset, DrawStyle.ELLIPSIS, width - 165);
        
        if (place.isHomescreen())
        {
            graphics.drawBitmap(width - 12, y + 2, homeFocus.getWidth(), homeFocus.getHeight(), selected ? homeFocus : homeUnfocus, 0, 0);
        }
        
        // draw the separator line
        graphics.setColor(Color.BLACK);
        graphics.drawLine(0, y + rowHeight - 1, width, y + rowHeight - 1);
    }
    
    protected void onFocus(int direction)
    {
        hasFocus = true;
        super.onFocus(direction);
        invalidate();
    }
    
    protected void onUnfocus()
    {
        hasFocus = false;
        super.onUnfocus();
        invalidate();
    }
    
    protected int moveFocus(int amount, int status, int time)
    {
        invalidate();
        return super.moveFocus(amount, status, time);
    }
    
    protected void moveFocus(int x, int y, int status, int time)
    {
        invalidate();
        super.moveFocus(x, y, status, time);
    }
}
