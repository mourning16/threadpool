package exception;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:42
 * @Version 1.0
 */
//选择拒绝策略为 抛错时，抛出该异常
public class RunnableDenyException extends RuntimeException {

    public RunnableDenyException(String message){
        super(message);
    }
}
