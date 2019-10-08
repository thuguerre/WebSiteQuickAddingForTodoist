# Build, package and deploy the project

## Java API

### On Google Cloud Platform TEST Environment

First be sure your `gcloud` SDK aims to the TEST Environment:  
> gcloud config get-value project

If the result is not `websitequickadding4todoisttest`, you can set it with:
> gcloud config set project websitequickadding4todoisttest

You can then build and deploy the Java API:  
 
> 1. mvn clean package  
> 2. mvn endpoints-framework:openApiDocs  
> 3. gcloud endpoints services deploy target/openapi-docs/openapi.json   
> 4. mvn appengine:deploy

If you have only modified the Java code itself, without modifying the API contract, you can only execute:
> mvn appengine:deploy


### On Google Cloud Platform PROD Environment

> **BE CAREFUL: you are going to deploy on PROD**  

First be sure your `gcloud` SDK aims to the TEST Environment:  
> gcloud config get-value project

If the result is not `websitequickadding4todoistprod`, you can set it with:
> gcloud config set project websitequickadding4todoistprod

You can then build and deploy the Java API:  

> 1. mvn clean package **-P PROD**  
> 2. mvn endpoints-framework:openApiDocs **-P PROD**  
> 3. gcloud endpoints services deploy target/openapi-docs/openapi.json   
> 4. mvn appengine:deploy **-P PROD**

> Note that we use the Maven profiles to modify the inner configuration. Do not forget to active the PROD Profile on each of your Maven command.

If you have only modified the Java code itself, without modifying the API contract, you can only execute:
> mvn appengine:deploy **-P PROD**  


  