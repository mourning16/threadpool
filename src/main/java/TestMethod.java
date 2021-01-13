import factory.DefaultThreadFactory;
import service.DenyService;
import service.ThreadFactoryService;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/12 16:04
 * @Version 1.0
 */

public class TestMethod implements Runnable{

    private static int i = 0;

    @Override
    public void run(){
        System.out.println(i++);
    }

    public static void main(String[] args) {
        DenyService denyService = new DenyService.DiscardDenyPolicy();
        BasicThreadPool basicThreadPool = new BasicThreadPool(5,10,8,new DefaultThreadFactory(),10,
               denyService,1000, TimeUnit.MILLISECONDS);
        System.out.println("线程池已经创建~~");
        for(int i=0;i<20;i++){
            basicThreadPool.execute(new TestMethod());
        }
    }
}
