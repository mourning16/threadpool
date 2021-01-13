package service;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:21
 * @Version 1.0
 */
//队列，用于存放缓存的队列
public interface RunnableQueueService {

    //当有新的任务进来时会offer到队列中
    public void offer(Runnable runnable);

    //工作线程通过take方法获取runnable
    public Runnable take() throws InterruptedException;

    int size();

}
