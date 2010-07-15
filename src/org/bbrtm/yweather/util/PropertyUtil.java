package org.bbrtm.yweather.util;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeModuleGroup;
import net.rim.device.api.system.CodeModuleGroupManager;

public class PropertyUtil
{
    private static PropertyUtil instance        = null;
    private boolean             firstRun        = true;
    private CodeModuleGroup     codeModuleGroup = null;
    
    private PropertyUtil()
    {
        CodeModuleGroup[] allGroups = CodeModuleGroupManager.loadAll();
        String moduleName = ApplicationDescriptor.currentApplicationDescriptor().getModuleName();
        
        // We shall check for null because when the app is copied into the
        // emulator (or installed via JavaLoader) it does not allow jad
        // properties to be read
        if (moduleName == null || allGroups == null)
        {
            return;
        }
        
        for (int i = 0; i < allGroups.length; i++)
        {
            if (allGroups[i] != null && allGroups[i].containsModule(moduleName))
            {
                codeModuleGroup = allGroups[i];
                break;
            }
        }
    }
    
    public static PropertyUtil getInstance()
    {
        if (instance == null)
            instance = new PropertyUtil();
        return instance;
    }
    
    public String get(String property)
    {
        if (firstRun)
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException ie)
            {
            }
            firstRun = false;
        }
        if (codeModuleGroup != null)
        {
            return codeModuleGroup.getProperty(property);
        }
        else
        {
            return "Unknown";
        }
    }
    
    public static synchronized String getAppName()
    {
        ApplicationDescriptor descriptor = ApplicationDescriptor.currentApplicationDescriptor();
        return descriptor.getName();
    }
    
    public static synchronized String getAppVersion()
    {
        ApplicationDescriptor descriptor = ApplicationDescriptor.currentApplicationDescriptor();
        return descriptor.getVersion();
    }
}
