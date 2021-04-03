package br.com.conectcar.clog.services;
 
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;


import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.conectcar.clog.entity.LogRequest;
import br.com.conectcar.clog.entity.LogResponse;
import br.com.conectcar.clog.entity.SendLogException;

 
public class AWSS3LogServices extends BaseLogServices {
   
    @ConfigProperty(name="app.clog.config.aws.s3.accesskey")
    String accessKey;

    @ConfigProperty(name="app.clog.config.aws.s3.secretkey")
    String secretKey;

    @ConfigProperty(name="app.clog.config.aws.s3.bucketname")
    String bucketName;
 
    @ConfigProperty(name="app.clog.config.aws.s3.signalbucketname")
    String signalBucketName;

 

    public AWSS3LogServices(){

        super();    
               
    }

    @Override
    public LogResponse gravarLog(LogRequest request) throws SendLogException {
       
        
        BasicAWSCredentials awsCreds;
        AmazonS3 s3Client;
        // Cria as credenciais do Aws S3
        awsCreds= new BasicAWSCredentials(accessKey, secretKey);
              
        // cria o client
        s3Client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withRegion(Regions.US_EAST_1)
            .build();  

        try{

            // gera o nome do arquivo baseado no ID da requisição
            var fileName = request.Id + ".txt";

            // Garante a existencia do bucket antes de subir o arquivo
            if (!s3Client.doesBucketExistV2(bucketName))
                s3Client.createBucket(bucketName);
                        
            // Monta a mensagem a ser enviada no formato JSON
            String myData = new Gson().toJson(request);

            // Envia o objeto pro bucket no S3 da AWS
            s3Client.putObject(bucketName, fileName, myData);        

            // Implementar o retorno
            return new LogResponse(request.Id ,"201", "Documento criado no armazenamento Secundario"); 

        }
        catch(AmazonServiceException ex)
        {            
            throw new SendLogException(ex.getErrorMessage());
        }
             
    }
        
    @Override
    public void sendStoredData() throws SendLogException {
        
        BasicAWSCredentials awsCreds;

        AmazonS3 s3Client;
        // Cria as credenciais do Aws S3
        awsCreds= new BasicAWSCredentials(accessKey, secretKey);
              
        // cria o client
        s3Client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
            .withRegion(Regions.US_EAST_1)
            .build();  

            try{

                // gera o nome do arquivo baseado no ID da requisição
                var fileName = "signal.elk-on.txt";
     
                 // Garante a existencia do bucket antes de subir o arquivo
                if (!s3Client.doesBucketExistV2(signalBucketName))
                    s3Client.createBucket(signalBucketName);

                // Envia o objeto pro bucket no S3 da AWS
                s3Client.putObject(bucketName, fileName, "");        
    
            }
            catch(AmazonServiceException ex)
            {            
                throw new SendLogException(ex.getErrorMessage());
            }

        

    }

}

