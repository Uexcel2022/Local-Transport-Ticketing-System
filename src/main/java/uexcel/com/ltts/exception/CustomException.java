package uexcel.com.ltts.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private String msg;
    private String code;
    public CustomException(String msg, String code){
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public CustomException(String msg, Throwable cause){
        super(msg, cause);
    }

    public CustomException(){

    }
}
