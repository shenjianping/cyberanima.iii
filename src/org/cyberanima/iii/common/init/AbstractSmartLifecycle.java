package org.cyberanima.iii.common.init;

import org.apache.log4j.Logger;
import org.springframework.context.SmartLifecycle;

public abstract class AbstractSmartLifecycle implements SmartLifecycle
{

    protected Logger logger = Logger.getLogger(this.getClass());
    private boolean isRunning = false;

    @Override
    public boolean isAutoStartup()
    {
        return true;
    }

    @Override
    public void stop(Runnable runnable)
    {
        runnable.run();
        isRunning = false;
        logger.info("stop Runnable");
    }

    @Override
    public boolean isRunning()
    {
        return isRunning;
    }
    @Override
    public void start()
    {
        isRunning = true;
        load();
        logger.info("start");
    }
    @Override
    public void stop()
    {
        isRunning = false;
        logger.info("stop");
    }

    protected abstract void load();
}
