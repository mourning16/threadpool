package factory;

import service.ThreadFactoryService;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/12 15:31
 * @Version 1.0
 */

public class DefaultThreadFactory implements ThreadFactoryService {

    @Override
    public Thread createThread(Runnable runnable){
        Thread thread = new Thread(runnable);
        return thread;
    }

}
