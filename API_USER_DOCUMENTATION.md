# Java API Documentation

This is the documentation of Java API and its different services. For the moment, all the services are callable using the same URL:
`${host}/api/todoistProxyAPI/v1/access-token/`  

The only difference is the calling method : `POST` or `DELETE`.  


## access-token POST

### request body

    {  
        "code":"7ca3a9e53560add021b9e0ccad4670ae1100d00c",  
        "state":"056537fd-3c22-4f93-881c-c3260e30109b"  
    }

### response codes

- 200 : returns a valid access token.
- 400 : code and/or state are null. Please verify your request body    
- 401 : sent code is not valid. Please verify your authentication process and get a fresh and valid code from Todoist Authorization service.    
- 409 : something bad occurred. but what ?
- 500 : API configuration loading failed. nothing to do to solve the situation. API has to be restarted.

### response body

In case of success, service returns:  

    {  
        "accessToken": "163c31b6249b092ab4a4c17d3477326acc842b8f"  
    }  

## access-token DELETE

### request body

    {  
        "accessToken": "163c31b6249b092ab4a4c17d3477326acc842b8f"  
    }  

### response codes

- 200 : token is successfully revoked.
- 400 : token is null. Please verify your request body.      
- 409 : something bad occurred. but what ?    
- 410 : token has expired or is wrong. it is no more known from Todoist and cannot be revoked.
- 500 : API configuration loading failed. nothing to do to solve the situation. API has to be restarted.

### response body

In case of success, service returns:  

    {
        "result": "OK",
        "resultMessage": "access token revoked"
    }  


## configuration GET

### request body

> no body

### response codes

- 200 : returns a valid client id configuration
- 500 : API configuration loading failed. nothing to do to solve the situation. API has to be restarted.

### response body

In case of success, service returns:  

    {
        "clientId": "51ba8ae54c9146be848bd0561003f089"
    }  


## wake-up GET

### request body

> no body

### response codes

- 200 : returns a valid client id configuration

### response body

In case of success, service returns:  

    {
        "message": "Let me sleeping..."
    }  


# Cron tasks

By default, the GCP Standard environment, used for this project, shuts down Google App Engine application if is not used. It is a problem when a user comes after the shutdown and suffers a 20-second-long process to wake up the API. To prevent this default, a Cron task has been configured to keep the API alive. It consists in a call each 5 minutes on the `Wake Up` service, doing nothing but returning a message asking to let it sleeping.  

Cron task is injected into Google App Engine using the file `cron.yaml` which can be found in `WEB-INF` folder. Please look at [API Deployment procedure](API_DEPLOYMENT.md#On-Google-Cloud-Platform-TEST-Environment) to know how to deploy tasks on environment.  

Be careful about GCP environment, as the default Google App Engine free time quota can be easily consumed with deployments added to the 24-hours alive execution.