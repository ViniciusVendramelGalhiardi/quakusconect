package br.com.conectcar.clog.entity;

import java.io.Serializable;
 

public class LogRequest implements Serializable
{
     /**
     *
     */
    private static final long serialVersionUID = 1905122041950251207L;
    
    public String Id;

    public String AppName;

    public LogMessage[] Messages;

    public LogRequest() {
        super();        
    }
  
}
