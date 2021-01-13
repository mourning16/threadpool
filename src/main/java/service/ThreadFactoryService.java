package service;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:23
 * @Version 1.0
 */

public interface ThreadFactoryService {

    public Thread createThread(Runnable runnable);

}
