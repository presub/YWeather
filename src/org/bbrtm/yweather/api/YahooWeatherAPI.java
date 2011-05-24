package org.bbrtm.yweather.api;

import java.io.ByteArrayInputStream;
import java.util.Vector;

import net.rim.device.api.xml.parsers.DocumentBuilder;
import net.rim.device.api.xml.parsers.DocumentBuilderFactory;

import org.bbrtm.yweather.model.YahooWeather;
import org.bbrtm.yweather.model.YahooWeatherForecast;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class YahooWeatherAPI
{
    public static YahooWeather parseYahooWeather(String xml)
    {
        try
        {
            YahooWeather weather = new YahooWeather();
            
            Document doc = createXMLDocument(xml);
            Element root = doc.getDocumentElement();
            
            NodeList nodes = root.getElementsByTagName("title");
            int nodeCount = nodes.getLength();
            for(int x = 0; x < nodeCount; ++x)
            {
                String title = getNodeValue((Element) nodes.item(x));
                if(x == 0 && ! title.equals("Yahoo! Weather - Error"))
                    break;
                if(x == 1)
                {
                    weather.setCurrentCondition(title);
                    weather.setCurrentCode("3200");
                    weather.setCurrentTemp("0");
                    return weather;
                }
                
            }
            
            Node itemNode = root.getElementsByTagName("item").item(0);
            nodes = itemNode.getChildNodes();
            nodeCount = nodes.getLength();
            for(int x = 0; x < nodeCount; ++x)
            {
                Node node = nodes.item(x);
                String name = node.getNodeName();
                if (name.equals("link")) {
                    String link = getNodeValue((Element) node); 
                    weather.setLink(link);
                    break;
                }
            }
            
            nodes = root.getElementsByTagName("yweather:condition");
            Element condition = (Element) nodes.item(0);
            weather.setCurrentCondition(condition.getAttribute("text"));
            weather.setCurrentCode(condition.getAttribute("code"));
            weather.setCurrentTemp(condition.getAttribute("temp"));
            
            nodes = root.getElementsByTagName("yweather:location");
            Element location = (Element) nodes.item(0);
            weather.setCity(location.getAttribute("city"));
            weather.setRegion(location.getAttribute("region"));
            weather.setCountry(location.getAttribute("country"));
            
            nodes = root.getElementsByTagName("yweather:units");
            Element units = (Element) nodes.item(0);
            weather.setTemperatureUnits(units.getAttribute("temperature"));
            weather.setSpeedUnits(units.getAttribute("speed"));
            weather.setDistanceUnits(units.getAttribute("distance"));
            weather.setPressureUnits(units.getAttribute("pressure"));
            
            nodes = root.getElementsByTagName("yweather:wind");
            Element wind = (Element) nodes.item(0);
            weather.setWindChill(wind.getAttribute("chill"));
            weather.setWindSpeed(wind.getAttribute("speed"));
            
            
            int direction = Integer.parseInt(wind.getAttribute("direction"));
            String windDirection = "Unknown";
            
            if( direction > 348.75 || direction < 11.25 )
            {
                windDirection = "N";
            }
            else if( direction > 11.25 && direction < 33.75 )
            {
                windDirection = "NNE";
            }
            else if( direction > 33.75 && direction < 56.25 )
            {
                windDirection = "NE";
            }
            else if( direction > 56.25 && direction < 78.75 )
            {
                windDirection = "ENE";
            }
            else if( direction > 78.75 && direction < 101.25 )
            {
                windDirection = "E";
            }
            else if( direction > 101.25 && direction < 123.75 )
            {
                windDirection = "ESE";
            }
            else if( direction > 123.75 && direction < 146.25 )
            {
                windDirection = "SE";
            }
            else if( direction > 146.25 && direction < 168.75 )
            {
                windDirection = "SSE";
            }
            else if( direction > 168.75 && direction < 191.25 )
            {
                windDirection = "S";
            }
            else if( direction > 191.25 && direction < 213.75 )
            {
                windDirection = "SSW";
            }
            else if( direction > 213.75 && direction < 236.25 )
            {
                windDirection = "SW";
            }
            else if( direction > 236.25 && direction < 258.75 )
            {
                windDirection = "WSW";
            }
            else if( direction > 258.75 && direction < 281.25 )
            {
                windDirection = "W";
            }
            else if( direction > 281.25 && direction < 303.75 )
            {
                windDirection = "WNW";
            }
            else if( direction > 303.75 && direction < 326.25 )
            {
                windDirection = "NW";
            }
            else if( direction > 326.25 && direction < 348.75 )
            {
                windDirection = "NNW";
            } 
            
            weather.setWindDirection(windDirection);
            
            nodes = root.getElementsByTagName("yweather:atmosphere");
            Element atmosphere = (Element) nodes.item(0);
            weather.setHumidity(atmosphere.getAttribute("humidity"));
            weather.setVisibility(atmosphere.getAttribute("visibility"));
            
            String rising = atmosphere.getAttribute("rising");
            
            if(rising.equals("0"))
            {
                rising = "steady";
            }
            else if(rising.equals("1"))
            {
                rising = "rising";
            }
            else // rising == "2"
            {
                rising = "falling";
            }
            
            weather.setRising(rising);
            
            weather.setPressure(atmosphere.getAttribute("pressure"));
            
            nodes = root.getElementsByTagName("yweather:astronomy");
            Element astro = (Element) nodes.item(0);
            weather.setSunrise(astro.getAttribute("sunrise"));
            weather.setSunset(astro.getAttribute("sunset"));
            
            nodes = root.getElementsByTagName("yweather:forecast");
            nodeCount = nodes.getLength();
            Vector forecasts = new Vector(nodeCount);
            for(int x = 0; x < nodeCount; ++x)
            {
                Element forecast = (Element) nodes.item(x);
                YahooWeatherForecast weatherForecast = new YahooWeatherForecast();
                weatherForecast.setCode(forecast.getAttribute("code"));
                weatherForecast.setCondition(forecast.getAttribute("text"));
                weatherForecast.setDate(forecast.getAttribute("date"));
                weatherForecast.setDay(forecast.getAttribute("day"));
                weatherForecast.setHigh(forecast.getAttribute("high"));
                weatherForecast.setLow(forecast.getAttribute("low"));
                
                forecasts.addElement(weatherForecast);
            }
            
            weather.setForecasts(forecasts);
            
            return weather;
           
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }
    
    private static Document createXMLDocument(String xml) throws Exception
    {
        
        Document document = null;
        ByteArrayInputStream bis = null;
        
        try
        {
            bis = new ByteArrayInputStream(xml.getBytes());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringElementContentWhitespace(true);
            factory.setAllowUndefinedNamespaces(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(bis);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally
        {
            if (bis != null)
                bis.close();
        }
        
        return document;
    }
    
    private static String getNodeValue(Element root)
    {
        StringBuffer buffer = new StringBuffer();
        NodeList nodes = root.getChildNodes();
        
        int nodeCount = nodes.getLength();
        for (int x = 0; x < nodeCount; ++x)
        {
            Text node = (Text) nodes.item(x);
            String value = node.getData();
            buffer.append(value);
        }
        
        return buffer.toString();
    }
}
