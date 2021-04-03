package br.com.conectcar.clog.entity;

import java.io.Serializable;
 

public class LogMessage implements Serializable{
    
    private static final long serialVersionUID = 1L;

    
    public String Data;
    public String UserName;     
    public String MessageType;
    public String Message;

    public LogMessage() {
        super();
        
    }

    public LogMessage(String data, String userName,  String messageType, String message) 
    {
                
    
        this.Data = data;
        this.UserName = userName;        
        this.MessageType = messageType;
        this.Message = message;
        
    }

}
