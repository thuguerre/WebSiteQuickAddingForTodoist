# Build, package and deploy the Python project

By default, a commit on the GitHub repository, in directory `TodoistProxyAPIPython`, will activate an auto-deployment of the Python API to Google Cloud Platform Functions, using GitHub Actions, on an TEST Environment. The workflow is run only if a file in the API directory `TodoistProxyAPIPython` is changed (or if the Python GitHub Actions workflow is modified).    

The deployment workflow is described in `.github/workflows/auto-deploy-python-api.yml`.

For documentation in case of required GitHub re-configuration, the following [GitHub Secrets](https://github.com/thuguerre/WebSiteQuickAddingForTodoist/settings/secrets) have to be created first:  
- `GCLOUD_AUTH_ENV_TEST`, whose value as to be got [from here](https://console.cloud.google.com/iam-admin/serviceaccounts) and then base64-encoded (with [this site](https://www.base64encode.org/) for instance).  
- `GCLOUD_AUTH_ENV_PROD`, whose is the same the previous but for PROD environment     
- `TODOIST_CLIENT_ID_PROD` (for a PROD deployment, see [Project Set Up](PROJECT_LOCAL_SETUP.md) to get it)    
- `TODOIST_CLIENT_SECRET_PROD` (for a PROD deployment, see [Project Set Up](PROJECT_LOCAL_SETUP.md) to get it)

# Build, package and deploy the project by yourself

> Be sure you have completed the [Project Set Up](PROJECT_LOCAL_SETUP.md) on your computer before continuing this procedure.  

If you need to build and deploy the project by yourself locally or on Google Cloud Platform Functions, you can use the following commands. They come from [a unofficial nice article](https://medium.com/google-cloud/deploying-a-python-serverless-function-in-minutes-with-gcp-19dd07e19824) to start with Python Google Cloud Functions.  

> Be careful, by default, the real GCP Application ID (for TEST environment) is set in the configuration files (Python and Javascript).

## On Google Cloud Platform TEST Environment

First be sure your Cloud SDK aims to the TEST Environment:  
    
    gcloud config get-value project

If the result is not `websitequickadding4todoisttest`, you can set it with:
    
    gcloud config set project websitequickadding4todoisttest

If you are in `TodoistProxyAPIPython` directory, you can then deploy the Python API:  
 
    gcloud functions deploy access-token --entry-point access_token --region europe-west1 --runtime python38 --trigger-http --allow-unauthenticated

Once successfully deployed, you can execute Deployment Tests, from `TodoistProxyAPIPython` directory:

    pytest -m 'not unittest and deploymenttest'

## On Google Cloud Platform PROD Environment

> ***Python PROD Deployment has not been done yet. See [issue #80](https://github.com/thuguerre/WebSiteQuickAddingForTodoist/issues/80). Following steps have just been copied from TEST Deployment for the moment.***  

> **BE CAREFUL: you are going to deploy on PROD**  

First be sure your Cloud SDK aims to the ***PROD*** Environment:  

    gcloud config get-value project

If the result is not `websitequickadding4todoistprod`, you can set it with:

    gcloud config set project websitequickadding4todoistprod

If you are in `TodoistProxyAPIPython` directory, you can then deploy the Python API:  
 
    gcloud functions deploy access-token --entry-point access_token --region europe-west1 --runtime python38 --trigger-http --allow-unauthenticated

Always test the deployment to be sure all is right.  
TODO: when PROD deployment is done (see [issue #80](https://github.com/thuguerre/WebSiteQuickAddingForTodoist/issues/80)), add here the command to execute Deployment Tests on PROD.

## On your own Google Cloud Platform Environment

If your are not allowed to deploy on TEST or PROD environments, and you have to test modifications on Java API, you can also:
1. Create your own GCP Environment
2. For now, there is hardcoded URL in `TodoistProxyAPIPython/main_test.py`
3. Execute previous TEST deployment commands  

## On your local machine

***Local deployment is not working for the moment. I did not look for it yet.***

## Resources

- [Nice article to start with Python Google Cloud Functions](https://medium.com/google-cloud/deploying-a-python-serverless-function-in-minutes-with-gcp-19dd07e19824)  
- [Official Google Cloud Functions Doc](https://cloud.google.com/functions/docs/writing/http?hl=fr#writing_http_helloworld-python)
- [Official Google Cloud Functions Testing Doc](https://cloud.google.com/functions/docs/testing/test-http?hl=fr)