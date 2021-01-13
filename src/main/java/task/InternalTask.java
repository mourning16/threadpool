package task;

import service.RunnableQueueService;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:45
 * @Version 1.0
 */
//该类取出runnable，并不断start
public class InternalTask  implements Runnable{

    private final RunnableQueueService runnableQueueService;

    //是否正在运行
    private volatile boolean running = true;

    public InternalTask(RunnableQueueService runnableQueueService){
        this.runnableQueueService = runnableQueueService;
    }

    @Override
    public void run(){

        while(running && !Thread.currentThread().isInterrupted()){

            try{
                Runnable task = runnableQueueService.take();
                task.run();
            }catch (Exception e){
                running = false;
                break;
            }
        }
    }

    //在线程池的shutdown方法中使用
    public void stop(){
        this.running = false;
    }

}
