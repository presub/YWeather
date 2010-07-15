package org.bbrtm.yweather.model;

import net.rim.device.api.util.Persistable;

public class YahooWeatherForecast implements Persistable
{
    private String day;
    private String date;
    private String low;
    private String high;
    private String condition;
    private String code;
    
    public String getDay()
    {
        return day;
    }
    
    public void setDay(String day)
    {
        this.day = day;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }
    
    public String getLow()
    {
        return low;
    }
    
    public void setLow(String low)
    {
        this.low = low;
    }
    
    public String getHigh()
    {
        return high;
    }
    
    public void setHigh(String high)
    {
        this.high = high;
    }
    
    public String getCondition()
    {
        return condition;
    }
    
    public void setCondition(String condition)
    {
        this.condition = condition;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
}
