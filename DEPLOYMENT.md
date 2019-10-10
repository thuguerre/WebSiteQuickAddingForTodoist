# Build, package and deploy the project

## Java API

These are the command lines to execute to build and deploy API on Google Cloud Platform AppEngine. They come from [Google Cloud Endpoints Java8 Samples](https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/appengine-java8/endpoints-v2-backend).

> Be careful, by default, the real GCP Application ID (for TEST environment) is set in the configuration files (Java and Javascript).

### On Google Cloud Platform TEST Environment

First be sure your Cloud SDK aims to the TEST Environment:  
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

First be sure your Cloud SDK aims to the ***PROD*** Environment:  
> gcloud config get-value project

If the result is not `websitequickadding4todoistprod`, you can set it with:
> gcloud config set project websitequickadding4todoistprod

You can then build and deploy the Java API:  

> 1. mvn clean package ***-P PROD***  
> 2. mvn endpoints-framework:openApiDocs ***-P PROD***  
> 3. gcloud endpoints services deploy target/openapi-docs/openapi.json   
> 4. mvn appengine:deploy ***-P PROD***

> Note that we use the Maven profiles to modify the inner configuration. Do not forget to active the PROD Profile on each of your Maven command.

If you have only modified the Java code itself, without modifying the API contract, you can only execute:
> mvn appengine:deploy ***-P PROD***

Always test the deployment to be sure all is right.

### On your own Google Cloud Platform Environment

If your are not allowed to deploy on TEST or PROD environemen, and you have to test modifications on Java API, you can also:
1. Create your own GCP Environment
2. Set your project ID in:
  - `TodoistProxyAPI\pom.xml` (replacing property `endpoints.project.id`'s value in TEST profile for instance. You can also create your own profile if you want)
  - `BrowserExtension\background.js` (modifying value of constant `TODOIST_PROXY_API_ACCESS_TOKEN`)
3. Execute previous TEST deployment commands  

### On your local machine

***Local deployment is not working for the moment. I was not able to deploy Google Endpoints services locally.***

To deploy Java Application locally, you first have to set an environment variable on your machine  
> ENDPOINTS_SERVICE_NAME=websitequickadding4todoisttest.appspot.com

Then, you can build and deploy Java App:  
> mvn clean package  
> ${google-cloud-sdk}\bin\java_dev_appserver.cmd ./TodoistQuickWebSiteURLAddAsTask-1.0-SNAPSHOT

### Resources

- [Cloud SDK / gcloud app deploy](https://cloud.google.com/sdk/gcloud/reference/app/deploy)  
- [Cloud SDK / Use Local Development Server](https://cloud.google.com/appengine/docs/standard/java/tools/using-local-server?hl=fr)  
- [Cloud SDK / Deploy Java Application](https://cloud.google.com/appengine/docs/standard/java/tools/uploadinganapp?hl=fr)  
- [OpenAPI / Deploy Endpoints Configuration](https://cloud.google.com/endpoints/docs/openapi/deploy-endpoints-config?hl=fr)  
