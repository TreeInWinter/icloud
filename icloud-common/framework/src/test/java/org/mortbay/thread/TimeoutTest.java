package org.mortbay.thread;


import junit.framework.TestCase;

import org.eclipse.jetty.util.thread.Timeout;
public class TimeoutTest extends TestCase
{
    Timeout timeout = new Timeout();
    Timeout.Task[] tasks;

    /* ------------------------------------------------------------ */
    /* 
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        timeout=new Timeout();
        timeout.setDuration(1000000);
        tasks= new Timeout.Task[10]; 
        
        for (int i=0;i<tasks.length;i++)
        {
            tasks[i]=new Timeout.Task(){};
            timeout.setNow(1000+i*100);
            timeout.schedule(tasks[i]);
        }
        timeout.setNow(100);
    }

    /* ------------------------------------------------------------ */
    /* 
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
    

    /* ------------------------------------------------------------ */
    public void testExpiry()
    {
        timeout.setDuration(200);
        timeout.setNow(1500);
        timeout.tick();
        
        for (int i=0;i<tasks.length;i++)
        {
            assertEquals("isExpired "+i,i<4, tasks[i].isExpired());
        }
    }

    /* ------------------------------------------------------------ */
    public void testCancel()
    {
        timeout.setDuration(200);
        timeout.setNow(1700);

        for (int i=0;i<tasks.length;i++)
            if (i%2==1)
                tasks[i].cancel();

        timeout.tick();
        
        for (int i=0;i<tasks.length;i++)
        {
            assertEquals("isExpired "+i,i%2==0 && i<6, tasks[i].isExpired());
        }
    }

    /* ------------------------------------------------------------ */
    public void testTouch()
    {
        timeout.setDuration(200);
        timeout.setNow(1350);
        tasks[2].reschedule();
        
        
        timeout.setNow(1500);
        timeout.tick();
        for (int i=0;i<tasks.length;i++)
        {
            assertEquals("isExpired "+i,i!=2 && i<4, tasks[i].isExpired());
        }
        
        timeout.setNow(1550);
        timeout.tick();
        for (int i=0;i<tasks.length;i++)
        {
            assertEquals("isExpired "+i, i<4, tasks[i].isExpired());
        }  
    }


    /* ------------------------------------------------------------ */
    public void testDelay()
    {
        Timeout.Task task = new Timeout.Task(){};
        timeout.cancelAll();
        timeout.setDuration(200);

        timeout.setNow(100);
        timeout.schedule(task);
        assertEquals("delay", false, task.isExpired());
        timeout.setNow(200);
        timeout.tick();
        assertEquals("delay", false, task.isExpired());
        timeout.setNow(400);
        timeout.tick();
        assertEquals("delay", true, task.isExpired());
        


        timeout.setNow(500);
        timeout.schedule(task, 100);
        
        timeout.setNow(550);
        timeout.tick();
        assertEquals("delay", false, task.isExpired());
        
//        timeout.setNow(650);
        timeout.setNow(800); // 500(now) + 100(delay) + 200(duration) = 800
        timeout.tick();
        assertEquals("delay", true, task.isExpired());
        
        

        timeout.setNow(1100);
        timeout.schedule(task, 300);
        
        timeout.setNow(1350);
        timeout.tick();
        assertEquals("delay", false, task.isExpired());
        
//        timeout.setNow(1450);
        timeout.setNow(1600); // 1100(now) + 300(delay) + 200(duration) = 1600
        timeout.tick();
        assertEquals("delay", true, task.isExpired());
    }

}
