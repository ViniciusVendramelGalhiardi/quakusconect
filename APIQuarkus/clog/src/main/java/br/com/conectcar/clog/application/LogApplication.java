package br.com.conectcar.clog.application;

import br.com.conectcar.clog.entity.LogRequest;
import br.com.conectcar.clog.entity.LogResponse;
import br.com.conectcar.clog.entity.SendLogException;
import br.com.conectcar.clog.services.ILogServices;

public class LogApplication {
    
    String principalLogServiceName;
    ILogServices principalLogService;

    public LogApplication() {

        super();

        // obter das configurações
        principalLogServiceName = "elk";
                
        // factory criação do serviço
        principalLogService = LogServicesFactory.createLogServices(principalLogServiceName);        
       
    }

    public LogResponse gravarLog(LogRequest request) throws SendLogException
    {

        // Gera um GUID para a requisição...
        var requestId = java.util.UUID.randomUUID().toString();            
        LogResponse r = new LogResponse();
        r.Id  = requestId;
        request.Id = requestId;

        try
        {
            // garante a obtenção dos logs enviados
            r = enviarLogsPrincipais(request);
        }
        catch(SendLogException ex)
        {            
            r.Status = "500";
            r.Message = ex.getMessage();        
        }
        
        return r;
                
    }

    private LogResponse enviarLogsPrincipais(LogRequest request) throws SendLogException {
        
        var secondaryLogService = principalLogService.getSecondaryServices();

        try
        {
            var response =  principalLogService.gravarLog(request); 

            if (secondaryLogService != null)
            {
                try
                {
                    secondaryLogService.sendStoredData();
                }
                catch(Exception ex)
                {
                    // Se der erro não faz nada.... 
                    // na proxima vez que executar ele vai tentar novamente.
                }                
            }
                            
            return response;
        }
        catch(SendLogException ex1)
        {                         
        
            // não conseguiu enviar o log para o repositorio principal 
            // tenta enviar pro secundario 
            if (secondaryLogService != null)
            {
                return secondaryLogService.gravarLog(request);
            }                
            else 
            {
                throw ex1;
            }

        }
        
    } 
   
}
