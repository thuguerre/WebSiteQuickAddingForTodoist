# Build, package and deploy the project

By default, a commit on the GitHub repository (today all branches, tomorrow only on `master`) will activate an auto-deployment of the Java API to Google Cloud Platform AppEngine, using GitHub Actions.  
The deployment workflow is described in `.github/workflows/auto-deploy-api.yml`.

For documentation, the following [GitHub Secrets](https://github.com/thuguerre/WebSiteQuickAddingForTodoist/settings/secrets) have to be created first:  
- `GCLOUD_AUTH_ENV_TEST`, whose value as to be got [from here](https://console.cloud.google.com/iam-admin/serviceaccounts) and Base64-encoded (with [this site](https://www.base64encode.org/) for instance)  
- `TODOIST_CLIENT_ID_PROD` (for a PROD deployment, see [Project Set Up](PROJECT_LOCAL_SETUP.md) to get it)    
- `TODOIST_CLIENT_SECRET_PROD` (for a PROD deployment, see [Project Set Up](PROJECT_LOCAL_SETUP.md) to get it)

# Build, package and deploy the project by yourself

> Be sure you have completed the [Project Set Up](PROJECT_LOCAL_SETUP.md) on your computer before continuing this procedure.  

If you need to build and deploy the project by yourself locally or on Google Cloud Platform AppEngine, you can use the following commands. They come from [Google Cloud Endpoints Java8 Samples](https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/appengine-java8/endpoints-v2-backend).  

> Be careful, by default, the real GCP Application ID (for TEST environment) is set in the configuration files (Java and Javascript).

## On Google Cloud Platform TEST Environment

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


## On Google Cloud Platform PROD Environment

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

## On your own Google Cloud Platform Environment

If your are not allowed to deploy on TEST or PROD environments, and you have to test modifications on Java API, you can also:
1. Create your own GCP Environment
2. Set your project ID in:
  - `TodoistProxyAPI\pom.xml` (replacing property `endpoints.project.id`'s value in TEST profile for instance. You can also create your own profile if you want)
  - `BrowserExtension\background.js` (modifying value of constant `TODOIST_PROXY_API_ACCESS_TOKEN`)
3. Execute previous TEST deployment commands  

## On your local machine

***Local deployment is not working for the moment. I was not able to deploy Google Endpoints services locally.***

To deploy Java Application locally, you first have to set an environment variable on your machine  
> ENDPOINTS_SERVICE_NAME=websitequickadding4todoisttest.appspot.com

Then, you can build and deploy Java App:  
> mvn clean package  
> ${google-cloud-sdk}\bin\java_dev_appserver.cmd ./TodoistQuickWebSiteURLAddAsTask-1.0-SNAPSHOT

## Resources

- [Cloud SDK / gcloud app deploy](https://cloud.google.com/sdk/gcloud/reference/app/deploy)  
- [Cloud SDK / Use Local Development Server](https://cloud.google.com/appengine/docs/standard/java/tools/using-local-server?hl=fr)  
- [Cloud SDK / Deploy Java Application](https://cloud.google.com/appengine/docs/standard/java/tools/uploadinganapp?hl=fr)  
- [OpenAPI / Deploy Endpoints Configuration](https://cloud.google.com/endpoints/docs/openapi/deploy-endpoints-config?hl=fr)  
