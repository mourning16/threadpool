package service;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:17
 * @Version 1.0
 */
//线程池
public interface ThreadPoolService {

    public void execute(Runnable runnable);

    public boolean isShutDown();

}
