package org.bbrtm.yweather.net;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANInfo;

import org.bbrtm.yweather.util.Configuration;
import org.bbrtm.yweather.util.Logger;

public final class NetworkConfig
{
    private static Logger        log             = Logger.getInstance();
    
    public static final int      MODE_AUTODETECT = 0;
    public static final int      MODE_BIS        = 1;
    public static final int      MODE_BES        = 2;
    public static final int      MODE_TCP        = 3;
    public static final int      MODE_WAP2       = 4;
    
    public static final String[] choices         = new String[] { "Auto", "BIS", "BES", "TCP", "WAP2" };
    
    private static String        connectionParameters = null;
    private static String        connectionUID = null;
    private static int           connectionMode = MODE_AUTODETECT;
    private static final String  mdsConnection   = ";deviceside=false";
    private static final String  tcpConnection   = ";deviceside=true";
    private static final String  bisConnection   = ";deviceside=false;ConnectionType=mds-public";
    private static final String  wifiConnection  = ";interface=wifi";
    private static final String  WAP2Connection  = ";ConnectionUID=";
    
    public static void init()
    {
        if (DeviceInfo.isSimulator())
        {
            log.debug("simulator, setting to TCP");
            setMode(MODE_TCP);
            return;
        }
        
        int m = Configuration.getInstance().getConnectionMode();
        
        if (m > MODE_AUTODETECT)
        {
            log.debug("found mode in config: " + m);
            setMode(m);
        }
        else
        {
            log.debug("autodetecting mode");
            setMode(autodetect());
        }
    }
    
    private static int autodetect()
    {
        log.debug("starting autodetect");
        // if you have BES service book, use it
        try
        {
            ServiceBook sb = ServiceBook.getSB();
            ServiceRecord[] records = sb.findRecordsByCid("IPPP");
            if (records != null)
            {
                for (int i = records.length - 1; i >= 0; i--)
                {
                    ServiceRecord rec = records[i];
                    if (rec.isValid() && !rec.isDisabled())
                    {
                        if (rec.getEncryptionMode() == ServiceRecord.ENCRYPT_RIM)
                        {
                            log.info("Auto Detect detected BES");
                            return MODE_BES;
                        }
                        else
                        {
                            log.info("Auto Detect detected BIS");
                            return MODE_BIS;
                        }
                    }
                }
            }
            
            String uid = getWAP2ConnectionUID();
            if (uid != null)
            {
                log.info("Auto Detect detected WAP2");
                log.info("Using WAP2 connection uid of " + uid);
                connectionUID = uid;
                return MODE_WAP2;
            }
            
            log.info("Auto Detect detected TCP");
            return MODE_TCP;
        }
        catch (Exception e)
        {
            // no permissions to explore service book. fall to TCP, I guess
            log.exception("could not auto detect network, falling back to TCP", e);
            return MODE_TCP;
        }
    }
    
    private static void setMode(int mode)
    {
        log.debug("Setting mode to " + mode);
        switch (mode)
        {
            case MODE_BES:
                connectionParameters = mdsConnection;
                break;
            case MODE_TCP:
                connectionParameters = tcpConnection;
                break;
            case MODE_BIS:
                connectionParameters = bisConnection;
                break;
            case MODE_WAP2:
                connectionParameters = WAP2Connection + connectionUID;
        }
        
        connectionMode = mode;
    }
    
    public static String getConnectionParameters()
    {
        log.debug("getting connection parameters");
        if (connectionParameters == null)
        {
            log.debug("initing the network config parameters");
            init();
        }
        
        
        boolean wifiEnabled = Configuration.getInstance().isWiFiEnabled();
        if(wifiEnabled)
            log.debug("wifi is enabled");
        
        if(wifiEnabled && isWifiActive() && isWifiAvailable())
        {
            log.debug("wifi is enabled and availabed, using WIFI");
            return wifiConnection;
        }
        
        log.debug("using " + connectionParameters);
        return connectionParameters;
    }
    
    public static String getConnectionParameters(int mode)
    {
        switch (mode)
        {
            case MODE_BES:
                return mdsConnection;
            case MODE_TCP:
                return tcpConnection;
            case MODE_BIS:
                return bisConnection;
            case MODE_WAP2:
                return WAP2Connection + connectionUID;
            case MODE_AUTODETECT:
                return getConnectionParameters(autodetect());
            default:
                return null;
        }
    }
    
    public static int getConnectionMode()
    {
        return connectionMode;
    }
    
    public static String getWAP2ConnectionUID()
    {
        log.debug("looking for WAP2 connection");
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.findRecordsByCid("WPTCP");
        String uid = null;
        
        for (int i = 0; i < records.length; i++)
        {
            // Search through all service records to find the
            // valid non-Wi-Fi and non-MMS
            // WAP 2.0 Gateway Service Record.
            if (records[i].isValid() && !records[i].isDisabled())
            {
                uid = records[i].getUid();
                if (uid != null && uid.length() != 0)
                {
                    uid = uid.toLowerCase();
                    log.debug("UID: " + uid);
                    if ((uid.indexOf("wifi") == -1) && (uid.indexOf("mms") == -1) && (uid.indexOf("wi-fi") == -1))
                    {
                        log.debug("Found WAP2 UID:" + records[i].getUid());
                        return records[i].getUid();
                    }
                }
            }
        }
        
        log.debug("No WAP2 connection");
        return null;
    }
    
    protected static boolean isWifiAvailable()
    {
        boolean wifi = (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED);
        
        log.debug(wifi ? "wifi is connected" : "wifi is not connected");
        
        return wifi;
    }
    
    protected static boolean isWifiActive()
    {
        if (RadioInfo.areWAFsSupported(RadioInfo.WAF_WLAN))
        {
            int active = RadioInfo.getActiveWAFs();
            log.debug("Active WAFs: " + active + " & " + RadioInfo.WAF_WLAN);
            
            boolean wifiActive = (active & RadioInfo.WAF_WLAN) > 0;
            
            log.debug(wifiActive ? "wifi is active" : "wifi is not active");
            
            return wifiActive;
            
        }
        
        log.debug("wifi is not supported on this device");
        return false;
    }
    
    protected static boolean isCellularDataAvailable()
    {
        return !(RadioInfo.getState() == RadioInfo.STATE_OFF || RadioInfo.getSignalLevel() == RadioInfo.LEVEL_NO_COVERAGE);
    }
    
    public static boolean isDataConnectionAvailable()
    {
        return (isCellularDataAvailable() || isWifiAvailable() && isWifiActive());
    }
}
