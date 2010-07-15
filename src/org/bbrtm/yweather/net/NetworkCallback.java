package org.bbrtm.yweather.net;

public interface NetworkCallback
{
    public void onNetworkRequestSuccessful(final Object reply);
    
    public void onNetworkRequestFailed(final String message, Exception e);
}
