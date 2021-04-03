package br.com.conectcar.clog.services;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.conectcar.clog.entity.LogRequest;
import br.com.conectcar.clog.entity.LogResponse;
import br.com.conectcar.clog.entity.SendLogException;
 
public class ElasticSearchLogServices  extends BaseLogServices{
          
    @ConfigProperty(name="app.clog.config.elasticsearch.endpoint.url")
     String esUrl;

     @ConfigProperty(name="app.clog.config.elasticsearch.endpoint.authenticationtoken")     
     String esAuthToken;

    public ElasticSearchLogServices() {
        super();
    }

    @Override
    public LogResponse gravarLog(LogRequest logRequest) throws SendLogException {
        
        String indexName = "clog-idx-" + logRequest.AppName.toLowerCase();

        try
        {
            // Cria o indice antes de mandar o documento.
            // Se o indice ja existe, então bypass na criação
            createElasticSearchIndex(indexName);

            // Posta o documento no ES
            return postDocument(indexName, logRequest);

        }
        catch(SendLogException ex)
        {
            throw new SendLogException(ex.getMessage());
        }

    }
    
    private LogResponse postDocument(String indexName, LogRequest logRequest) throws SendLogException
    {
        
        try
        {
           
            String endpoint = esUrl + "/" + indexName + "/_doc/";
            String method = "POST";
                            
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();        
            String myRequestString = gson.toJson(logRequest);

            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, myRequestString);
            Request request = new Request.Builder()
                .url(endpoint)
                .method(method, body)
                .addHeader("Authorization", esAuthToken)
                .addHeader("Content-Type", "application/json")
            .build();

            Response response = client.newCall(request).execute();                    
            
            Integer code = response.code();                 
            
            return new LogResponse(logRequest.Id, code.toString(), response.message());
            
        }
        catch(IOException ex)
        {
            throw new SendLogException(ex.getMessage());
        }
        

    }

    private void createElasticSearchIndex(String indexName)
    {   
        try
        {
            
            String endpoint = esUrl + "/" + indexName;
            String method = "PUT";
                            
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                .url(endpoint)
                .method(method, body)
                .addHeader("Authorization", "Basic ZWxhc3RpYzpIcDkxUXR2UVd6SUg2dDVnVG5JdnNFSDg=")
                .build();

            client.newCall(request).execute();
            
        }
        catch(IOException ex)
        {
         
        }
    }
}

