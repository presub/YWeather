package org.bbrtm.yweather.ui.field;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.GIFEncodedImage;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BitmapField;

public class AnimatedGIFField extends BitmapField
{
    private GIFEncodedImage image = null;
    
    private int currentFrame;
    private int width;
    private int height;
    private AnimatorThread animatorThread = null;
    
    public AnimatedGIFField(GIFEncodedImage image)
    {
        this(image, 0);
    }
    
    public AnimatedGIFField(GIFEncodedImage image, long style)
    {
        //Call super to setup the field with the specified style.
        //The image is passed in as well for the field to
        //configure its required size.
        super(image.getBitmap(), style);

        //Store the image and it's dimensions.
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();

        //Start the animation thread.
        animatorThread = new AnimatorThread(this);
        animatorThread.start();
    }
    
    protected void paint(Graphics graphics)
    {
        //Call super.paint. This will draw the first background
        //frame and handle any required focus drawing.
        super.paint(graphics);

        //Don't redraw the background if this is the first frame.
        if (currentFrame != 0)
        {
            //Draw the animation frame.
            graphics.drawImage(image.getFrameLeft(currentFrame), image.getFrameTop(currentFrame),
                image.getFrameWidth(currentFrame), image.getFrameHeight(currentFrame), image, currentFrame, 0, 0);
        }
    }

    //Stop the animation thread when the screen the field is on is
    //popped off of the display stack.
    protected void onUndisplay()
    {
        animatorThread.stop();
        super.onUndisplay();
    }
    
  //A thread to handle the animation.
    private class AnimatorThread extends Thread
    {
        private AnimatedGIFField imageField;
        private boolean isRunning = true;
        private int totalFrames;     //The total number of frames in the image.
        private int loopCount;       //The number of times the animation has looped (completed).
        private int totalLoops;      //The number of times the animation should loop (set in the image).

        public AnimatorThread(AnimatedGIFField imageField)
        {
            this.imageField = imageField;
            totalFrames = image.getFrameCount();
            totalLoops = image.getIterations();

        }

        public synchronized void stop()
        {
            isRunning = false;
        }

        public void run()
        {
            while(isRunning)
            {
                //Invalidate the field so that it is redrawn.
                /*UiApplication.getUiApplication().invokeAndWait(new Runnable()
                {
                    public void run()
                    {
                        imageField.invalidate();
                    }
                });*/
                
                synchronized (Application.getEventLock())
                {
                    imageField.invalidate();
                }

                try
                {
                    //Sleep for the current frame delay before
                    //the next frame is drawn.
                    sleep(image.getFrameDelay(currentFrame) * 10);
                }
                catch (InterruptedException iex)
                {} //Couldn't sleep.

                //Increment the frame.
                ++currentFrame;

                if (currentFrame == totalFrames)
                {
                    //Reset back to frame 0 if we have reached the end.
                    currentFrame = 0;

                    ++loopCount;

                    //Check if the animation should continue.
                    if (loopCount == totalLoops)
                    {
                        isRunning = false;
                    }
                }
            }
        }
    }
}
