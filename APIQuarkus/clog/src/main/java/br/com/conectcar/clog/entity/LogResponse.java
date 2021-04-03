package br.com.conectcar.clog.entity;

public class LogResponse {
    
    public String Id;
    public String Status;
    public String Message;

    public LogResponse() {
        super();
    }

    public LogResponse(String id, String status, String message) {
        super();
        this.Id = id;
        this.Status = status;
        this.Message = message;
    }
}
