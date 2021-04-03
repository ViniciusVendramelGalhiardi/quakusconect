package br.com.conectcar.clog.controller;
 
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import br.com.conectcar.clog.application.LogApplication;
import br.com.conectcar.clog.entity.LogMessage;
import br.com.conectcar.clog.entity.LogRequest;
import br.com.conectcar.clog.entity.LogResponse;
import br.com.conectcar.clog.entity.SendLogException;


@Path("/c-log")
public class LogController {
    
    @ConfigProperty(name="app.clog.version")
    String versao;

    @ConfigProperty(name="app.clog.config.elasticsearch.endpoint.url")
    String esUrl;

    @ConfigProperty(name="app.clog.config.elasticsearch.endpoint.authenticationtoken")     
    String esAuthToken;
    
    LogApplication app;

    public LogController() 
    {
        super();
        this.app = new LogApplication();
    }

    @GET    
    @Path("/info")   
    public String info() {
        return "ConectCar c-Log " + versao + ", " + esUrl;
    }

    @GET
    @Path("/test")    
    public String test()
    {
        return "OK";
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/log")    
    public Response log(LogRequest request) {
        
        // Variaveis
        int statusCode;
        LogResponse r = new LogResponse();
        
        try
        {
            // solicita a gravação do log pra camada de aplicação
            r = app.gravarLog(request); 
            statusCode = 200;            
        }
        catch(SendLogException ex1)
        {
           r.Message = ex1.getMessage();
           r.Status = "500";
           statusCode = 500;
        }
        catch(Exception ex2)
        {
            r.Message = ex2.getMessage();
            r.Status = "500";
            statusCode = 500;
        }

        return Response.ok(r).status(statusCode).build();            

    }
    
   
/*

    public static void main(String[] args)
    {

        var x = new LogController();
        
        List<LogMessage> itens = new ArrayList<>();        
        itens.add(new LogMessage("2021-03-16 12:30:45", "joao@conectcar.com",  "Error", "Erro ao estabelecer conexão com o provedor 1 XPTO"));
        itens.add(new LogMessage("2021-03-16 11:30:45", "joao@conectcar.com",  "Error", "Erro Teste"));
        itens.add(new LogMessage("2021-03-16 13:32:45", "jose@conectcar.com",  "Warning", "Alerta de instabilidade no serviço A"));
        itens.add(new LogMessage("2021-03-16 11:34:45", "jose@conectcar.com",  "Info", "Marco de 1000 conexões simultâneas"));
        itens.add(new LogMessage("2021-03-16 15:38:45", "maria@conectcar.com", "Error", "Erro ao estabelecer conexão com o provedor 2 XPTO"));

        LogMessage[] msgs = new LogMessage[itens.size()];
        msgs = itens.toArray(msgs);
        LogRequest req = new LogRequest();
        req.AppName = "App1";
        req.Messages = msgs;
        new LogController().log(req);
        
    }
*/

}