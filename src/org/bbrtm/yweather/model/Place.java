package org.bbrtm.yweather.model;

import net.rim.device.api.util.Persistable;

public class Place implements Persistable
{
    private String woeid;
    private String name;
    private String country;
    private String admin1;
    private String admin2;
    
    private YahooWeather weather = null;
    
    private boolean homescreen = false;
    
    public String getWoeid()
    {
        return woeid;
    }
    
    public void setWoeid(String woeid)
    {
        this.woeid = woeid;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getAdmin1()
    {
        return admin1;
    }
    
    public void setAdmin1(String admin1)
    {
        this.admin1 = admin1;
    }
    
    public String getAdmin2()
    {
        return admin2;
    }
    
    public void setAdmin2(String admin2)
    {
        this.admin2 = admin2;
    }
    
    public boolean isHomescreen()
    {
        return this.homescreen;
    }
    
    public void setHomescreen(boolean homescreen)
    {
        this.homescreen = homescreen;
    }
    
    public String toString()
    {
        return this.name + ", " + this.admin1 + ", " + this.country;
    }

    public YahooWeather getWeather()
    {
        return weather;
    }

    public void setWeather(YahooWeather weather)
    {
        this.weather = weather;
    }
    
    public boolean equals(Object obj)
    {
        if(!(obj instanceof Place))
            return false;
        
        Place place = (Place) obj;        
        return this.getWoeid().equals(place.getWoeid());
        
    }
    
}
