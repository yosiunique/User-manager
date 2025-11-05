package enat.bank.exception;

public class RolesNotFoundException extends  RuntimeException{
    public RolesNotFoundException(String messages){
        super(messages);
    }
}
