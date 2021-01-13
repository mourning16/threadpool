package exception;

/**
 * @Description TODO
 * @Author Mourning 16
 * @Date 2021/1/11 19:42
 * @Version 1.0
 */

public class RunnableDenyException extends RuntimeException {

    public RunnableDenyException(String message){
        super(message);
    }
}
