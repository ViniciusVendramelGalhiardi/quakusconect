package br.com.conectcar.clog.services;

import br.com.conectcar.clog.entity.LogRequest;
import br.com.conectcar.clog.entity.LogResponse;
import br.com.conectcar.clog.entity.SendLogException;

public abstract class BaseLogServices implements ILogServices {

    ILogServices secondaryServices;

    public ILogServices getSecondaryServices()
    {
        return secondaryServices;
    }

    protected BaseLogServices() {
        super();
    }

    @Override
    public void setSecondaryService(ILogServices secondary) {
        secondaryServices = secondary;        
    }

    @Override
    public LogResponse gravarLog(LogRequest request) throws SendLogException {        
        return null;
    }

    @Override
    public void sendStoredData() throws SendLogException {
    
    }        
    
}
