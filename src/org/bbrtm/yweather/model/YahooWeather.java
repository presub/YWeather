package org.bbrtm.yweather.model;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class YahooWeather implements Persistable
{
    public YahooWeather()
    {
        super();
        date = System.currentTimeMillis();
    }
    
    private long date;
    private String city;
    private String region;
    private String country;
    
    private String currentCondition;
    private String currentTemp;
    private String currentCode;
    
    private String temperatureUnits;
    private String distanceUnits;
    private String pressureUnits;
    private String speedUnits;
    
    private String windChill;
    private String windDirection;
    private String windSpeed;
    
    private String humidity;
    private String visibility;
    private String pressure;
    private String rising;
    
    private String sunrise;
    private String sunset;
    
    private Vector forecasts;
    
    public long getDate()
    {
        return date;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public String getRegion()
    {
        return region;
    }
    
    public void setRegion(String region)
    {
        this.region = region;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public String getCurrentCondition()
    {
        return currentCondition;
    }
    
    public void setCurrentCondition(String currentCondition)
    {
        this.currentCondition = currentCondition;
    }
    
    public String getCurrentTemp()
    {
        return currentTemp;
    }
    
    public void setCurrentTemp(String currentTemp)
    {
        this.currentTemp = currentTemp;
    }
    
    public String getCurrentCode()
    {
        return currentCode;
    }
    
    public void setCurrentCode(String currentCode)
    {
        this.currentCode = currentCode;
    }
    
    public String getTemperatureUnits()
    {
        return temperatureUnits;
    }
    
    public void setTemperatureUnits(String temperatureUnits)
    {
        this.temperatureUnits = temperatureUnits;
    }
    
    public String getDistanceUnits()
    {
        return distanceUnits;
    }
    
    public void setDistanceUnits(String distanceUnits)
    {
        this.distanceUnits = distanceUnits;
    }
    
    public String getPressureUnits()
    {
        return pressureUnits;
    }
    
    public void setPressureUnits(String pressureUnits)
    {
        this.pressureUnits = pressureUnits;
    }
    
    public String getSpeedUnits()
    {
        return speedUnits;
    }
    
    public void setSpeedUnits(String speedUnits)
    {
        this.speedUnits = speedUnits;
    }
    
    public String getWindChill()
    {
        return windChill;
    }
    
    public void setWindChill(String windChill)
    {
        this.windChill = windChill;
    }
    
    public String getWindDirection()
    {
        return windDirection;
    }
    
    public void setWindDirection(String windDirection)
    {
        this.windDirection = windDirection;
    }
    
    public String getWindSpeed()
    {
        return windSpeed;
    }
    
    public void setWindSpeed(String windSpeed)
    {
        this.windSpeed = windSpeed;
    }
    
    public String getHumidity()
    {
        return humidity;
    }
    
    public void setHumidity(String humidity)
    {
        this.humidity = humidity;
    }
    
    public String getVisibility()
    {
        return visibility;
    }
    
    public void setVisibility(String visibility)
    {
        this.visibility = visibility;
    }
    
    public String getPressure()
    {
        return pressure;
    }
    
    public void setPressure(String pressure)
    {
        this.pressure = pressure;
    }
    
    public String getRising()
    {
        return rising;
    }
    
    public void setRising(String rising)
    {
        this.rising = rising;
    }

    public String getSunrise()
    {
        return sunrise;
    }

    public void setSunrise(String sunrise)
    {
        this.sunrise = sunrise;
    }

    public String getSunset()
    {
        return sunset;
    }

    public void setSunset(String sunset)
    {
        this.sunset = sunset;
    }

    public Vector getForecasts()
    {
        return forecasts;
    }

    public void setForecasts(Vector forecasts)
    {
        this.forecasts = forecasts;
    }
    
}
