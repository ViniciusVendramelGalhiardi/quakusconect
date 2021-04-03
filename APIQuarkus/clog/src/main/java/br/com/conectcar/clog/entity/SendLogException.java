package br.com.conectcar.clog.entity;

public class SendLogException extends Exception {

    private static final long serialVersionUID = 1L;
    
    public SendLogException(String message) {
        super(message);        
    }
}
