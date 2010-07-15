package org.bbrtm.yweather.google.analytics;

import net.rim.device.api.util.Persistable;

public class CustomVariable implements Persistable
{
    public static final int SCOPE_VISITOR = 1;
    public static final int SCOPE_SESSION = 2;
    public static final int SCOPE_PAGE    = 3;
    
    private int             index         = 1;
    private String          name          = null;
    private String          value         = null;
    private int             scope         = 1;
    
    public CustomVariable(int index, String name, String value, int scope)
    {
        super();
        this.index = index;
        this.name = name;
        this.value = value;
        this.scope = scope;
    }
    
    public int getIndex()
    {
        return index;
    }
    
    public void setIndex(int index)
    {
        this.index = index;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
    public int getScope()
    {
        return scope;
    }
    
    public void setScope(int scope)
    {
        this.scope = scope;
    }
    
}
