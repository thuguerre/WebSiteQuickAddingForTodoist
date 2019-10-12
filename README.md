***PROJECT UNDER DEVELOPMENT***    
***NOT EVEN IN AN ALPHA VERSION NOW :)***


# Project Description

This ***unofficial*** Todoist browser extension allows its user to add its current web site's URL as a new Task in his Todoist Inbox, just by clicking on a single button. It completes the official Todoist browser extension, allowing the same use case but with a 4-clicks process from the browser header, or a 2-clicks process using right-click menu on page.      

For the moment, this project will only provide this single and quite simple functionnality.     

# For developers and/or contributors

## How to contribute ?

This extension project is now under developement. Contributions are still difficult for the moment. However, you will be able to contribute soon !
- By translating texts other than in English and French,
- By reporting issues, and even fixing them :)

## How it works ?

Basically, the extension has the following process, fired when the user clicks on the extension button :

1. Try to access the Todoist Access Token stored in `Browser Storage`
2. If no Access Token is found, launch [Todoist Authorization Flow](https://developer.todoist.com/sync/v8/#authorization):
    - Prompt the Todoist Authentication Form, using [Browser Identity API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/identity),
    - Get a valid OAuth Code, once user has authenticated
    - Call our `Todoist Proxy API` to transform this OAuth Code in a valid Access Token.
    - Store the Access Token in `Browser Storage`, for a future reuse
3. Launch the Task Add Flow:
    - Get active tab, to retrieve its URL
    - Call [Todoist Rest API](https://developer.todoist.com/rest/v1/#create-a-new-task) to add the task
    - If task add is a success, confirm the user the task has been added, using [Browser Notification API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/notifications)
    - If task add has failed due to Authorization reasons, revoke the Access Token and go to `Step 2` 

## How to set up the project ?

### Todoist Account

In order to make this project working and testable, you will need to own a [Todoist Account](https://todoist.com), first to authenticate the extension as any user will do, but also to get application credentials.  

You will also need to declare an application in [Todoist Application Console](https://developer.todoist.com/appconsole.html), in order to get Client Id and Secret values which will have to be set in the following credentials files.  

*I own a premium Todoist account, and currently do not know if a free account is enough.*  

### Credentials

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

### How to build and deploy the API ?

See [Java API Deployment instructions](API_DEPLOYMENT.md).  
See [Java API Documentation](API_USER_DOCUMENTATION.md).      
 

### How to test the extension ?

Under your Firefox browser (**not yet tested under Chrome**) 
- Open `about:debugging` tab 
- Click on `This Firefox`
- Click on `Load Temporary Add-on`
- Choose any file in the `BrowserExtension` directory

*You can have more information about testing an extension on [this page](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/Your_first_WebExtension).*  

Do not hesitate to click on Extension `Debug` button to have a look at the extension's logs.    

# Resources

## Todoist
- [Todoist REST API Documentation](https://developer.todoist.com/rest/v1/#create-a-new-task), for Task Add Service
- [Todoist Sync API Documentation](https://developer.todoist.com/sync/v8/#authorization), for Todoist Authorization Flow, based on OAuth protocol
- [Todoist Brand Usage Limits](https://developer.todoist.com/sync/v8/#brand-usage)

## Browser Extension 
- [Browser Extension Mozilla Documentation](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/Your_first_WebExtension)
- [Browser Notifications API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/notifications)
- [Browser Storage API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/storage)
- [Browser Identity API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/identity)
- [Browser Identity API Samples](https://github.com/mdn/webextensions-examples/blob/master/google-userinfo/)

## GCP AppEngine
- [Google Cloud Endpoints Java8 Samples](https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/appengine-java8/endpoints-v2-backend)
- [Google Cloud Endpoints Param and Return Types](https://cloud.google.com/endpoints/docs/frameworks/java/parameter-and-return-types)  
- [Google Cloud Endpoints Exceptions](https://cloud.google.com/endpoints/docs/frameworks/java/exceptions)
- [Google AppEngine Logging](https://cloud.google.com/appengine/docs/standard/java/logs/)  

## API Client
- [Create a Java RESTFul client with Jersey client](https://o7planning.org/fr/11217/creer-java-restful-client-avec-jersey-client#a4487792)