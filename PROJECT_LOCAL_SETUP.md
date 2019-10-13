# How to set up the project ?

## Todoist Account

In order to make this project working and testable, you will need to own a [Todoist Account](https://todoist.com), first to authenticate the extension as any user will do, but also to get application credentials.  

You will also need to declare an application in [Todoist Application Console](https://developer.todoist.com/appconsole.html), in order to get Client Id and Secret values which will have to be set in the following credentials files.  

*I own a premium Todoist account, and currently do not know if a free account is enough.*  

## Credentials

> If you do not plan to modify the Java API, you can use the official `TODOIST_CLIENT_ID` already set in `background.js`.  
> However, if you need to modify the Java part of the project, you will have to execute the following process, and generate your own `TODOIST_CLIENT_ID` and `TODOIST_CLIENT_SECRET`.  

For security reasons, Todoist API credentials are not committed to the Git repository.  
For the moment, a very basic solution is used : credentials are externalized into files which are Git-ignored.  
A better solution will be found as soon as possible.

You have to create a `credentials.properties` file in `TodoistProxyAPI/src/main/resources` directory. This file must look like:
> TODOIST_CLIENT_ID=123456  
> TODOIST_CLIENT_SECRET=123456    

where:
- `TODOIST_CLIENT_ID` value is the Client Id got from your [Todoist Application Console](https://developer.todoist.com/appconsole.html). It has the same value as in `background.js` file, in `BrowserExtension` directory.
- `TODOIST_CLIENT_SECRET` value is the Secret Key got from your [Todoist Application Console](https://developer.todoist.com/appconsole.html).

Regarding the extension part, the `TODOIST_CLIENT_ID` (and only it) is set in the header of `background.js`. Of course, `TODOIST_CLIENT_ID` in both Java and Javascript parts have to be equal.       

## How to build and deploy the API ?

See [Java API Deployment instructions](API_DEPLOYMENT.md), to deploy it on Google Cloud AppEngine.  
See [Java API User Documentation](API_USER_DOCUMENTATION.md), to learn how to use it.      
 

## How to test the extension ?

Under your Firefox browser (**not yet tested under Chrome**) 
- Open `about:debugging` tab 
- Click on `This Firefox`
- Click on `Load Temporary Add-on`
- Choose any file in the `BrowserExtension` directory

*You can have more information about testing an extension on [this page](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/Your_first_WebExtension).*  

Do not hesitate to click on Extension `Debug` button to have a look at the extension's logs.    
