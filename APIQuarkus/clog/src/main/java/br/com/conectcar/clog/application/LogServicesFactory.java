package br.com.conectcar.clog.application;

import br.com.conectcar.clog.services.AWSS3LogServices;
import br.com.conectcar.clog.services.ElasticSearchLogServices;
import br.com.conectcar.clog.services.ILogServices;

public class LogServicesFactory {
    
    private LogServicesFactory() {
        super();
    }

    public static ILogServices createLogServices(String serviceName)
    {
        ILogServices service = null;
        
        if (serviceName.equals("elk"))
        {
            service = new ElasticSearchLogServices();
            service.setSecondaryService(new AWSS3LogServices());    
        }

        return service;
    }
    
}
