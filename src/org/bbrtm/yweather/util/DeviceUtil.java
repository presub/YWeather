package org.bbrtm.yweather.util;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.system.CodeModuleManager;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.RadioInfo;

public class DeviceUtil
{
    public static String getLanguage()
    {
        String lang = Locale.getDefault().getLanguage();
        return lang;
    }
    
    public static String getMobileNetworkNumber()
    {
        int number = RadioInfo.getMNC(RadioInfo.getCurrentNetworkIndex());
        return Integer.toString(number);
    }
    
    public static String getMobileCountryCode()
    {
        int code = RadioInfo.getMCC(RadioInfo.getCurrentNetworkIndex());
        return Integer.toString(code);
    }
    
    public static String getNetworkName()
    {
        String name = RadioInfo.getCurrentNetworkName();
        
        if (name == null || name.length() == 0)
        {
            return "Unknown";
        }
        return name;
    }
    
    public static String getDeviceName()
    {
        String deviceName = DeviceInfo.getDeviceName();
        if (deviceName == null || deviceName.length() == 0)
            return "Uknown";
        return deviceName;
    }
    
    public static String getDisplaySize()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(Display.getWidth());
        buffer.append("x");
        buffer.append(Display.getHeight());
        return buffer.toString();
    }
    
    public static String getPlatformVersion()
    {
        String os = DeviceInfo.getPlatformVersion();
        if (os == null || os.length() == 0)
        {
            if (DeviceInfo.isSimulator())
                return "Simulator";
            else
                return "Unknown";
        }
        return os;
        
    }
    
    public static String getDeviceOS()
    {
        if(DeviceInfo.isSimulator())
            return "Simulator";
        String os = DeviceInfo.getSoftwareVersion();
        if(os == null || os.length() == 0)
            return "Unknown";
        return os;
    }
    
    public static String getAppVersion()
    {
        String version = PropertyUtil.getAppVersion();
        if (version == null || version.trim().length() == 0)
        {
            version = PropertyUtil.getInstance().get("MIDlet-Version");
            if (version == null || version.trim().length() == 0)
                return "Uknown";
        }
        
        return version;
    }
    
    public static boolean isAudioCaptureSupported()
    {
        String system = System.getProperty("supports.audio.capture");
        return (system != null && system.trim().equals("true"));
    }
    
    public static String[] getSupportedAudioCaptureFormats()
    {
        String formats = System.getProperty("audio.encodings");
        return StringUtil.split(formats, " ");
    }
    
    public static boolean isPhotoCaptureSupported()
    {
        if (!DeviceInfo.hasCamera())
            return false;
        String system = System.getProperty("video.snapshot.encodings");
        return (system != null && system.trim().length() > 0);
    }
    
    public static String[] getSupportedImageCaptureFormats()
    {
        String formats = System.getProperty("video.snapshot.encodings");
        return StringUtil.split(formats, " ");
    }
    
    public static boolean isVideoCaptureSupported()
    {
        if (!DeviceInfo.hasCamera())
            return false;
        // since we launch the video recorder via the code module
        int moduleHandle = CodeModuleManager.getModuleHandle("net_rim_bb_videorecorder");
        return (moduleHandle != 0);
        
        // for embedded video camera
        // String capture = System.getProperty("supports.video.capture");
        // return (capture != null && capture.trim().equals("true"));
    }
    
    public static String[] getSupportVideoCaptureFormats()
    {
        String formats = System.getProperty("video.encodings");
        return StringUtil.split(formats, " ");
    }
    
    public static boolean isLowResolutionDevice()
    {
        return Display.getWidth() <= 320;
    }
}
