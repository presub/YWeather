package org.bbrtm.yweather.util;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class Queue extends Vector implements Persistable
{

    public Queue()
    {
        super();
        // TODO Auto-generated constructor stub
    }

    public Queue(int initialCapacity, int capacityIncrement)
    {
        super(initialCapacity, capacityIncrement);
        // TODO Auto-generated constructor stub
    }

    public Queue(int initialCapacity)
    {
        super(initialCapacity);
        // TODO Auto-generated constructor stub
    }
    
    public void queue(Object obj)
    {
        this.addElement(obj);
    }
    
    public Object dequeue()
    {
        Object obj = this.firstElement();
        this.removeElementAt(0);
        return obj;
    }
}
