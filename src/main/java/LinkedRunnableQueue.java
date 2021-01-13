import service.DenyService;
import service.RunnableQueueService;
import service.ThreadPoolService;

import java.util.LinkedList;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:56
 * @Version 1.0
 */
public class LinkedRunnableQueue implements RunnableQueueService {

    //任务队列最大容量，在构建时传入
    private final int limit;

    private final DenyService denyService;

    private final LinkedList<Runnable> runnableList = new LinkedList<>();

    private final ThreadPoolService threadPoolService;

    public LinkedRunnableQueue(int limit,DenyService denyService,ThreadPoolService threadPoolService){
        this.limit = limit;
        this.denyService = denyService;
        this.threadPoolService = threadPoolService;
    }

    //如果超过队列数则执行拒绝策略
    @Override
    public void offer(Runnable runnable){

        synchronized (runnableList){
            if(runnableList.size()>=limit){
                denyService.reject(runnable,threadPoolService);
            }else{
                runnableList.add(runnable);
                runnableList.notifyAll();
            }
        }
    }

    @Override
    public Runnable take()throws InterruptedException{

        synchronized (runnableList){
            while(runnableList.isEmpty()){
                try{
                    runnableList.wait();
                }catch (InterruptedException e){
                    throw  e;
                }
            }
        }
        return  runnableList.removeFirst();
    }

    @Override
    public int size(){
        return runnableList.size();
    }

}
