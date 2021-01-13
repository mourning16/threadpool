import factory.DefaultThreadFactory;
import service.DenyService;
import service.RunnableQueueService;
import service.ThreadFactoryService;
import service.ThreadPoolService;
import task.InternalTask;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 20:10
 * @Version 1.0
 */

public class BasicThreadPool extends Thread implements ThreadPoolService {

    //初始化线程
    private final int initSize;

    //最大线程数
    private final int maxSize;

    //核心线程数
    private final int coreSize;

    //当前活跃线程数
    private int activeCount;

    //线程工厂
    private final ThreadFactoryService threadFactoryService;

    //任务队列
    private final RunnableQueueService runnableQueueService;

    private volatile boolean isShutdown = false;

    private final Queue<ThreadTask> threadTaskQueue = new ArrayDeque();

    //工作线程队列
    private final static DenyService DEFAULT_DENY_POLICY = new DenyService.DiscardDenyPolicy();

    private final static ThreadFactoryService DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    private final long keepAliveTime;

    private final TimeUnit timeUnit;

    public BasicThreadPool(int initSize, int intMaxSize, int coreSize, ThreadFactoryService threadFactoryService,
    int queueSize, DenyService denyPolicy,long keepAliveTime,TimeUnit timeUnit){
        this.initSize = initSize;
        this.maxSize = intMaxSize;
        this.coreSize = coreSize;
        this.threadFactoryService = threadFactoryService;
        this.runnableQueueService = new LinkedRunnableQueue(queueSize,denyPolicy,this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    private void init(){
        start();
        for(int i=0; i < initSize; i ++){
            newThread();
        }
    }

    public void execute(Runnable runnable){

        if(this.isShutdown){
            throw new IllegalStateException("该线程池已经被销毁~~");
        }
        this.runnableQueueService.offer(runnable);
    }

    private void newThread(){

        InternalTask internalTask = new InternalTask(runnableQueueService);
        Thread thread = this.threadFactoryService.createThread(internalTask);
        ThreadTask threadTask= new ThreadTask(thread,internalTask);
        threadTaskQueue.offer(threadTask);
        this.activeCount++;
        thread.start();
    }

    private void removeThread(){
        ThreadTask threadTask = threadTaskQueue.remove();
        threadTask.internalTask.stop();
        this.activeCount--;
    }

    @Override
    public void run(){

        while(!isShutdown && isInterrupted()) {
            try {
                timeUnit.sleep(keepAliveTime);
            } catch (InterruptedException e) {
                isShutdown = true;
                break;
            }
            synchronized (this) {
                if (isShutdown) break;

                //当前队列有任务未处理  activeCount<coreSize继续扩容
                if (runnableQueueService.size() > 0 && activeCount < coreSize) {

                    for (int i = initSize; i < coreSize; i++) {
                        newThread();
                    }
                    continue;
                }

                //当前队列有任务未处理  activeCount<maxSize继续扩容
                if (runnableQueueService.size() > 0 && activeCount < maxSize) {

                    for (int i = initSize; i < maxSize; i++) {
                        newThread();
                    }
                }

                if (runnableQueueService.size() == 0 && activeCount < maxSize) {
                    for (int i = coreSize; i < maxSize; i++) {
                        removeThread();
                    }
                }
            }
        }
    }

    @Override
    public boolean isShutDown(){
        return isShutdown;
    }

    private static class ThreadTask{

        Thread thread;
        InternalTask internalTask;

        public ThreadTask(Thread thread,InternalTask internalTask){
            this.thread = thread;
            this.internalTask = internalTask;
        }
    }


}
