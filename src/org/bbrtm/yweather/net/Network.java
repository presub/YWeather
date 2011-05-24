package org.bbrtm.yweather.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.io.http.HttpProtocolConstants;

import org.bbrtm.yweather.util.Logger;

public final class Network
{
    public void doGet(String url, NetworkCallback cb)
    {
        HttpPost p = new HttpPost(url, HttpProtocolConstants.HTTP_METHOD_GET, null, cb);
        p.start();
    }
    
    public void doGet(String url, Hashtable headers, NetworkCallback cb)
    {
        HttpPost p = new HttpPost(url, HttpProtocolConstants.HTTP_METHOD_GET, null, headers, cb);
        p.start();
    }
    
    public void doPost(String url, Hashtable data, NetworkCallback nc)
    {
        StringBuffer sb = new StringBuffer();
        Enumeration e = data.keys();
        while (e.hasMoreElements())
        {
            String key = (String) e.nextElement();
            if (sb.length() > 0)
            {
                sb.append('&');
            }
            sb.append(key).append('=').append(data.get(key));
        }
        doPost(url, sb.toString().getBytes(), nc);
    }
    
    public void doPost(String url, byte[] postBody, NetworkCallback cb)
    {
        HttpPost p = new HttpPost(url, HttpProtocolConstants.HTTP_METHOD_POST, postBody, cb);
        p.start();
    }
    
    public void doPost(String url, String contentType, byte[] postBody, NetworkCallback cb)
    {
        HttpPost p = new HttpPost(url, HttpProtocolConstants.HTTP_METHOD_POST, contentType, postBody, cb);
        p.start();
    }
    
    public void doPost(String url, Hashtable headers, byte[] postBody, NetworkCallback cb)
    {
        HttpPost p = new HttpPost(url, HttpProtocolConstants.HTTP_METHOD_POST, postBody, headers, cb);
        p.start();
    }
    
    class HttpPost extends Thread
    {
        String          url         = null;
        byte[]          postBody    = null;
        String          method      = null;
        String          contentType = null;
        NetworkCallback callback    = null;
        Hashtable       headers     = null;
        
        public HttpPost(String url, String method, byte[] post, NetworkCallback cb)
        {
            this.url = url.concat(NetworkConfig.getConnectionParameters());
            this.method = method;
            this.postBody = post;
            this.callback = cb;
            this.contentType = "application/x-www-form-urlencoded";
        }
        
        public HttpPost(String url, String method, String contentType, byte[] post, NetworkCallback cb)
        {
            this(url, method, post, cb);
            this.contentType = contentType;
        }
        
        public HttpPost(String url, String method, byte[] post, Hashtable headers, NetworkCallback cb)
        {
            this(url, method, post, cb);
            this.headers = headers;
        }
        
        public void run()
        {
            HttpConnection con = null;
            OutputStream os = null;
            InputStream is = null;
            
            try
            {
                con = (HttpConnection) Connector.open(url);
                
                if (postBody != null)
                {
                    con.setRequestProperty("Content-Type", contentType);
                    con.setRequestProperty("Content-Length", Integer.toString(postBody.length));
                }
                
                if (headers != null)
                {
                    Enumeration iter = headers.keys();
                    while (iter.hasMoreElements())
                    {
                        String key = (String) iter.nextElement();
                        String value = (String) headers.get(key);
                        con.setRequestProperty(key, value);
                    }
                }
                
                if (HttpProtocolConstants.HTTP_METHOD_POST.equals(method))
                {
                    con.setRequestMethod(HttpConnection.POST);
                }
                else
                {
                    con.setRequestMethod(HttpConnection.GET);
                }
                
                if (postBody != null)
                {
                    os = con.openOutputStream();
                    os.write(postBody, 0, postBody.length);
                    os.close();
                }
                
                int status = con.getResponseCode();
                
                if (status == HttpConnection.HTTP_OK || status == 201)
                {
                    
                    if (callback != null)
                    {
                        String response = slurpResponse(con);
                        
                        callback.onNetworkRequestSuccessful(response);
                    }
                }
                else if (status == HttpConnection.HTTP_FORBIDDEN)
                {
                    String response = slurpResponse(con);
                    
                    if (callback != null)
                        callback.onNetworkRequestFailed(response, null);
                }
                else if (status == HttpConnection.HTTP_BAD_REQUEST)
                {
                    String response = slurpResponse(con);
                    
                    if (callback != null)
                        callback.onNetworkRequestFailed(response, null);
                }
                else if (status == HttpConnection.HTTP_MOVED_TEMP)
                {
                    String response = slurpResponse(con);
                    String location = con.getHeaderField("Location");
                    Exception e = new RuntimeException(Integer.toString(status) + response + location);
                    if (callback != null)
                        callback.onNetworkRequestFailed("HTTP 302", e);
                }
                else
                {
                    String response = slurpResponse(con);
                    Exception e = new RuntimeException(Integer.toString(status) + response);
                    if (callback != null)
                        callback.onNetworkRequestFailed("Unknown Error", e);
                }
            }
            catch (Exception e)
            {
                Logger.getInstance().log("HTTP POST", e, Logger.ERROR);
                callback.onNetworkRequestFailed(e.getMessage(), e);
            }
        }
    }
    
    public static String buildAuthChallenge(String username, String password)
    {
        // build the auth challenge:
        OutputStream memStream = new ByteArrayOutputStream();
        OutputStream challengeStream = new Base64OutputStream(memStream);
        String basicAuth = null;
        try
        {
            challengeStream.write(username.getBytes());
            challengeStream.write(':');
            challengeStream.write(password.getBytes());
            challengeStream.flush();
            challengeStream.close();
            basicAuth = "Basic " + memStream.toString();
            memStream.close();
        }
        catch (Exception e)
        {
            
        }
        return basicAuth;
    }
    
    private static String slurpResponse(HttpConnection con)
    {
        StringBuffer buffer = new StringBuffer();
        try
        {
            InputStream is = con.openInputStream();
            byte[] data = new byte[1024];
            int len = 0;
            
            while (-1 != (len = is.read(data)))
            {
                buffer.append(new String(data, 0, len));
            }
            
            is.close();
            con.close();
            
        }
        catch (Exception e)
        {
            Logger.getInstance().exception("slurping response", e);
        }
        String response = buffer.toString();
        return response;
    }
}
