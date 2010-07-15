package org.bbrtm.yweather.google.analytics;

import net.rim.device.api.i18n.Locale;

import org.bbrtm.yweather.util.URLUTF8Encoder;

public class NetworkRequestUtil
{
    private static final String GOOGLE_ANALYTICS_GIF_PATH = "/__utm.gif";
    private static final String FAKE_DOMAIN_HASH          = "999";
    
    public static String constructPageviewRequestPath(Event event, String referrer)
    {
        String str = "";
        if (event.action != null)
            str = event.action;
        if (!(str.startsWith("/")))
            str = "/" + str;
        str = encode(str);
        Locale localLocale = Locale.getDefault();
        StringBuffer localStringBuilder = new StringBuffer(GOOGLE_ANALYTICS_GIF_PATH);
        localStringBuilder.append("?utmwv=4.3");
        localStringBuilder.append("&utmn=").append(event.randomVal);
        localStringBuilder.append("&utmcs=UTF-8");
        
        if(event.hasCustomVariables())
        {
            localStringBuilder.append("&utme=");
            localStringBuilder.append(constructCustomVariablesPath(event.getCustomVariables()));
        }
        
        localStringBuilder.append("&utmsr=");
        localStringBuilder.append(event.screenWidth);
        localStringBuilder.append("x");
        localStringBuilder.append(event.screenHeight);
        localStringBuilder.append("&utmul=");
        localStringBuilder.append(localLocale.getLanguage());
        localStringBuilder.append("-");
        localStringBuilder.append(localLocale.getCountry());
        localStringBuilder.append("&utmp=").append(str);
        localStringBuilder.append("&utmac=").append(event.accountId);
        localStringBuilder.append("&utmcc=").append(getEscapedCookieString(event, referrer));
        return localStringBuilder.toString();
    }
    
    public static String constructEventRequestPath(Event event, String referrer)
    {
        Locale localLocale = Locale.getDefault();
        StringBuffer localStringBuilder1 = new StringBuffer();
        StringBuffer localStringBuilder2 = new StringBuffer();
        localStringBuilder2.append("5(");
        localStringBuilder2.append(encode(event.category));
        localStringBuilder2.append("*");
        localStringBuilder2.append(encode(event.action));
        if (event.label != null)
        {
            localStringBuilder2.append("*").append(encode(event.label));
        }
        localStringBuilder2.append(")");
        if (event.value > -1)
        {
            localStringBuilder2.append("(");
            localStringBuilder2.append(event.value);
            localStringBuilder2.append(")");
        }
        localStringBuilder1.append(GOOGLE_ANALYTICS_GIF_PATH);
        localStringBuilder1.append("?utmwv=4.3");
        localStringBuilder1.append("&utmn=").append(event.randomVal);
        localStringBuilder1.append("&utmt=event");
        localStringBuilder1.append("&utme=").append(localStringBuilder2.toString());
        localStringBuilder1.append("&utmcs=UTF-8");
        localStringBuilder1.append("&utmsr=");
        localStringBuilder1.append(event.screenWidth);
        localStringBuilder1.append("x");
        localStringBuilder1.append(event.screenHeight);
        localStringBuilder1.append("&utmul=");
        localStringBuilder1.append(localLocale.getLanguage());
        localStringBuilder1.append("-");
        localStringBuilder1.append(localLocale.getCountry());
        localStringBuilder1.append("&utmac=").append(event.accountId);
        localStringBuilder1.append("&utmcc=").append(getEscapedCookieString(event, referrer));
        return localStringBuilder1.toString();
    }
    
    public static String getEscapedCookieString(Event event, String referrer)
    {
        StringBuffer localStringBuilder = new StringBuffer();
        localStringBuilder.append("__utma=");
        localStringBuilder.append(FAKE_DOMAIN_HASH).append(".");
        localStringBuilder.append(event.userId).append(".");
        localStringBuilder.append(event.timestampFirst).append(".");
        localStringBuilder.append(event.timestampPrevious).append(".");
        localStringBuilder.append(event.timestampCurrent).append(".");
        localStringBuilder.append(event.visits);
        if (referrer != null)
        {
            localStringBuilder.append("+__utmz=");
            localStringBuilder.append(FAKE_DOMAIN_HASH).append(".");
            localStringBuilder.append(event.timestampFirst).append(".");
            localStringBuilder.append("1.1.");
            localStringBuilder.append(referrer);
        }
        return encode(localStringBuilder.toString());
    }
    
    private static String encode(String paramString)
    {
        return URLUTF8Encoder.encode(paramString);
    }
    
    private static String constructCustomVariablesPath(CustomVariable[] vars)
    {
        StringBuffer utme = new StringBuffer("8(");
        for(int x = 0; x <  vars.length; ++x)
        {
            if(x > 0)
                utme.append("*");
            utme.append(encode(vars[x].getName()));
        }
        utme.append(")9(");
        for(int x = 0; x <  vars.length; ++x)
        {
            if(x > 0)
                utme.append("*");
            utme.append(encode(vars[x].getValue()));
        }
        utme.append(")11(");
        for(int x = 0; x <  vars.length; ++x)
        {
            if(x > 0)
                utme.append("*");
            utme.append(vars[x].getScope());
        }
        
        utme.append(")");
        
        return utme.toString();
    }
}
