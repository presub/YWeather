package org.bbrtm.yweather.util;

import net.rim.blackberry.api.browser.Browser;
import net.rim.device.api.i18n.ResourceBundle;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

import org.bbrtm.yweather.YWeatherResource;
import org.bbrtm.yweather.net.Network;
import org.bbrtm.yweather.net.NetworkCallback;
import org.bbrtm.yweather.ui.dialog.StatusScreen;

public class CheckUpdateThread extends Thread
{
    private static final String   UPDATE_URL   = "http://bbrtm.org/yweather/yweather.jad";
    private static Logger         log          = Logger.getInstance();
    private boolean               manual       = false;
    private StatusScreen          statusScreen = null;
    
    private static ResourceBundle resources    = null;
    static
    {
        resources = ResourceBundle.getBundle(YWeatherResource.BUNDLE_ID, YWeatherResource.BUNDLE_NAME);
    }
    
    public CheckUpdateThread()
    {
        this(false);
    }
    
    public CheckUpdateThread(boolean manual)
    {
        super();
        this.manual = manual;
    }
    
    public void run()
    {
        // PostDataEncoder postData = new PostDataEncoder();
        // postData.append("device_name", DeviceUtil.getDeviceName());
        // postData.append("device_OS", DeviceUtil.getDeviceOS());
        // postData.append("software_version",
        // DeviceUtil.getDeviceSoftwareVersion());
        // postData.append("network_name", DeviceUtil.getNetworkName());
        // postData.append("display_size", DeviceUtil.getDisplaySize());
        // postData.append("TumblrVersion", DeviceUtil.getAppVersion());
        log.debug("Checking for updates.");
        
        if (manual)
        {
            statusScreen = new StatusScreen(resources.getString(YWeatherResource.STATUS_CHECKING_UPDATES));
            synchronized (Application.getEventLock())
            {
                UiApplication.getUiApplication().pushScreen(statusScreen);
            }
        }
        
        final String current = DeviceUtil.getAppVersion();
        log.debug("current version: " + current);
        
        Network network = new Network();
        network.doGet(UPDATE_URL, new NetworkCallback()
        {
            
            public void onNetworkRequestSuccessful(Object reply)
            {
                String jad = (String) reply;
                
                String version = parseJAD(jad);
                log.debug("version available: " + version);
                
                boolean update = isUpdate(current, version);
                
                if (statusScreen != null)
                {
                    synchronized (Application.getEventLock())
                    {
                        UiApplication.getUiApplication().popScreen(statusScreen);
                    }
                }
                
                if (update)
                {
                    log.debug("update availble");
                    UiApplication.getUiApplication().invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            int choice = Dialog.ask(Dialog.D_YES_NO, resources.getString(YWeatherResource.STATUS_UPDATE_AVAILABLE), Dialog.YES);
                            if (choice == Dialog.YES)
                            {
                                log.debug("opening browser");
                                Browser.getDefaultSession().displayPage(UPDATE_URL);
                            }
                        }
                    });
                }
                else if (manual)
                {
                    UiApplication.getUiApplication().invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            Dialog.inform(resources.getString(YWeatherResource.STATUS_NO_UPDATES));
                        }
                    });
                }
            }
            
            public void onNetworkRequestFailed(String message, Exception e)
            {
                if (statusScreen != null)
                {
                    synchronized (Application.getEventLock())
                    {
                        UiApplication.getUiApplication().popScreen(statusScreen);
                    }
                }
                
                if (manual)
                {
                    UiApplication.getUiApplication().invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            Dialog.inform(resources.getString(YWeatherResource.STATUS_NO_UPDATES));
                        }
                    });
                }
            }
        });
    }
    
    private String parseJAD(String jad)
    {
        try
        {
            int index = jad.indexOf("MIDlet-Version:") + 16;
            int endIndex = jad.indexOf('\r', index);
            String version = jad.substring(index, endIndex);
            return version;
        }
        catch (Exception e)
        {
            log.exception("exception parsing jad file", e);
            return "0.0.0";
        }
    }
    
    private boolean isUpdate(String current, String version)
    {
        try
        {
            String[] currentSplit = StringUtil.split(current, ".");
            String[] versionSplit = StringUtil.split(version, ".");
            int count = currentSplit.length;
            if (currentSplit.length != versionSplit.length)
            {
                Math.min(currentSplit.length, versionSplit.length);
            }
            for (int x = 0; x < count; ++x)
            {
                int c = Integer.parseInt(currentSplit[x]);
                int v = Integer.parseInt(versionSplit[x]);
                
                if (v > c)
                    return true;
            }
            if (versionSplit.length > currentSplit.length)
                return true;
        }
        catch (Exception e)
        {
            log.exception("exception checking for update", e);
        }
        return false;
    }
}
