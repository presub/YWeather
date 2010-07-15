package org.bbrtm.yweather.ui.field;

import net.rim.device.api.i18n.ResourceBundleFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class FocusableLabelField extends LabelField
{

    public FocusableLabelField()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public FocusableLabelField(Object text, int offset, int length, long style)
    {
        super(text, offset, length, style);
        // TODO Auto-generated constructor stub
    }

    public FocusableLabelField(Object text, long style)
    {
        super(text, style);
        // TODO Auto-generated constructor stub
    }

    public FocusableLabelField(Object text)
    {
        super(text);
        // TODO Auto-generated constructor stub
    }

    public FocusableLabelField(ResourceBundleFamily rb, int key)
    {
        super(rb, key);
        // TODO Auto-generated constructor stub
    }
    
    protected void drawFocus(Graphics graphics, boolean on)
    {
        // do nothing
    }
}
