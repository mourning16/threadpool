package service;

import exception.RunnableDenyException;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 18:51
 * @Version 1.0
 */
//用户执行拒绝策略
@FunctionalInterface
public interface DenyService {

    public void reject(Runnable runnable,ThreadPoolService threadPoolService);

    //该策略将任务直接丢弃
    public class DiscardDenyPolicy implements DenyService{
        @Override
        public void reject(Runnable runnable,ThreadPoolService threadPoolService){
            System.out.println("您的请求已经被忽略~~");
        }
    }

    //该拒绝策略向任务提交者抛出异常
    public class AbortDenyPolicy implements DenyService{
        @Override
        public void reject(Runnable runnable,ThreadPoolService threadPoolService){
            throw new RunnableDenyException("您的请求已经被拒绝~~");
        }
    }

    //该拒绝策略会使任务在提交者所在线程执行任务
    public class RunnerDenyPolicy implements DenyService{
        @Override
        public void reject(Runnable runnable,ThreadPoolService threadPoolService){
            if(!threadPoolService.isShutDown()){
                runnable.run();
            }
        }
    }

}
