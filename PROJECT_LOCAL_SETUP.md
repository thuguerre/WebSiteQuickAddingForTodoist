# How to set up the project ?

## Todoist Account

In order to make this project working and testable, you will need to own a [Todoist Account](https://todoist.com), first to authenticate the extension as any user will do, but also to get application credentials.  

You will also need to declare an application in [Todoist Application Console](https://developer.todoist.com/appconsole.html), in order to get Client Id and Secret values which will have to be set in the following credentials files.  

*Author's note : I own a premium Todoist account, and currently do not know if a free account is enough.*  

## Credentials

> If you need to modify the API part of the project, you will have to execute the following process, and generate your own `TODOIST_CLIENT_ID` and `TODOIST_CLIENT_SECRET`.  
> If you do not plan to modify the API but only the browser extension, you will be able to call the default test API which will provide you the official `TODOIST_CLIENT_ID`.    

For security reasons, Todoist API credentials are not committed (and must not) to the Git repository. The API is designed to use Environment Variables, not to be based on war-inner-files containing these values.    
To make the API working on any environment (your local one or a distant server), you have first to set following both environment variables:
1. `TODOIST_CLIENT_ID`
2. `TODOIST_CLIENT_SECRET`     

where:
- `TODOIST_CLIENT_ID` value is the Client Id got from your [Todoist Application Console](https://developer.todoist.com/appconsole.html). It has to have the same value as in `background.js` file, in `BrowserExtension` directory.
- `TODOIST_CLIENT_SECRET` value is the Secret Key got from your [Todoist Application Console](https://developer.todoist.com/appconsole.html).

MacOS Users can use [this article](https://medium.com/@mamk2118/setting-up-environment-variables-in-macos-mojave-and-mac-os-catalina-27ea1bb032f3) to set their environment variables.

By default, Browser Extension will automatically retrieve the `TODOIST_CLIENT_ID` from API when launched.

Both environment variables are required to launch Unit Tests AND to package the Java API, as they are injected in `appengine-web.xml` to make it working on Google Cloud Platform environment. *Yes, finally, the .war file contains a file with credentials values, but I did not find yet how to set Environment Variables on AppEngine Java 8 in another way.*          

## How to build and deploy the API ?

The project is currently switching from Java:

* See [Java API Deployment instructions](JAVA_API_DEPLOYMENT.md), to deploy it on Google Cloud Platform AppEngine.  
* See [Java API User Documentation](API_USER_DOCUMENTATION.md), to learn how to use it.  

to Python:

* Python Version: `3.8`
* See [Python Functions Deployment instructions](PYTHON_FUNCTIONS_DEPLOYMENT.md), to deploy it on Google Cloud Platform Functions.
* See TODO, to use Python API.   

## How to test the extension ?

**By default, the extension aims the API on TEST environment. Please look at URL at the very top of the file `background.js`.**

Under your Firefox browser (**not yet tested under Chrome**) 
- Open `about:debugging` tab 
- Click on `This Firefox`
- Click on `Load Temporary Add-on`
- Choose any file in the `BrowserExtension` directory

*You can have more information about testing an extension on [this page](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/Your_first_WebExtension).*  

Do not hesitate to click on Extension `Debug` button to have a look at the extension's logs.    
