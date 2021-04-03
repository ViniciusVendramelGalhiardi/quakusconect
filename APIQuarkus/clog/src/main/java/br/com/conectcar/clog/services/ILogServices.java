package br.com.conectcar.clog.services;

import br.com.conectcar.clog.entity.LogRequest;
import br.com.conectcar.clog.entity.LogResponse;
import br.com.conectcar.clog.entity.SendLogException;

public interface ILogServices 
{
    
    void setSecondaryService(ILogServices secondary);
    
    LogResponse gravarLog(LogRequest request) throws SendLogException;

    ILogServices getSecondaryServices();
 
    void sendStoredData() throws SendLogException ;
}
