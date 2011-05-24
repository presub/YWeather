package org.bbrtm.yweather.util;

import java.util.Vector;

public class StringUtil
{
    /**
     * 
     * @param src
     *            The source string
     * @param find
     *            The string to find
     * @param replace
     *            The string to replace find with
     * @return The updated string
     */
    public static String replaceString(String src, String find, String replace)
    {
        StringBuffer buffer = new StringBuffer();
        
        int index = 0;
        int currIndex = 0;
        
        index = src.indexOf(find, currIndex);
        
        while (index >= 0)
        {
            String add = src.substring(currIndex, index);
            buffer.append(add);
            buffer.append(replace);
            currIndex = index + find.length();
            index = src.indexOf(find, currIndex);
        }
        
        String add = src.substring(currIndex);
        buffer.append(add);
        
        return buffer.toString();
    }
    
    public static String[] split(String string, String token)
    {
        final Vector strings = new Vector();
        final int tokenLength = token.length();
        
        int index = 0;
        int currIndex  = string.indexOf(token, index);
        
        while (currIndex != -1)
        {
            strings.addElement(string.substring(index, currIndex));
            index = currIndex + tokenLength;
            currIndex  = string.indexOf(token, index);
        }
        
        strings.addElement(string.substring(index));
        
        final String[] result = new String[strings.size()];
        strings.copyInto(result);
        return result;
    }
    
    public static String[] stringVectorToArray(Vector vector)
    {
        String[] strings = new String[vector.size()];
        vector.copyInto(strings);
        return strings;
    }
    
    public static String getFileName(String path)
    {
        int index = path.lastIndexOf('/');
        return path.substring(++index);
    }
}


