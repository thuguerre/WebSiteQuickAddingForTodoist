***PROJECT UNDER DEVELOPMENT***    
***NOT EVEN IN AN ALPHA VERSION NOW :)***

![](https://github.com/thuguerre/WebSiteQuickAddingForTodoist/workflows/Auto%20Deploy%20Java%20API/badge.svg)  
![](https://github.com/thuguerre/WebSiteQuickAddingForTodoist/workflows/Auto%20Test%20Python%20API/badge.svg)

# Project Description

This ***unofficial*** Todoist browser extension allows its user to add its current web site's URL as a new Task in his Todoist Inbox, just by clicking on a single button. It completes the official Todoist browser extension, allowing the same use case but with a 4-clicks process from the browser header, or a 2-clicks process using right-click menu on page.      

For the moment, this project will only provide this single and quite simple functionnality.     

> ***Disclaimer: initially, this project used a Java API to proxy Todoist services, using Google Cloud AppEngine. However, it is currently switching to a Python version of it, using Google Cloud Functions. All documentation is migrating too, and explains for the moment both versions. Until Python version is fully fuctional and linked to the Browser Extension (not the case for now), Java API will be used and Java documentation is the right one.***

# For developers and/or contributors

## How to contribute ?

This extension project is now under developement. Contributions are still difficult for the moment. However, you will be able to contribute soon !
- By translating texts other than in English and French,
- By reporting issues, and even fixing them :)

## How both API and Browser Extension work ?

Basically, the extension has the following process, fired when the user clicks on the extension button :

1. Call the API to retrieve Todoist Client ID required in following process
2. Try to access the Todoist Access Token stored in `Browser Storage`
3. If no Access Token is found, launch [Todoist Authorization Flow](https://developer.todoist.com/sync/v8/#authorization):
    - Prompt the Todoist Authentication Form, using [Browser Identity API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/identity),
    - Get a valid OAuth Code, once user has authenticated
    - Call our `Todoist Proxy API` to transform this OAuth Code in a valid Access Token.
    - Store the Access Token in `Browser Storage`, for a future reuse
4. Launch the Task Add Flow:
    - Get active tab, to retrieve its URL
    - Call [Todoist Rest API](https://developer.todoist.com/rest/v1/#create-a-new-task) to add the task
    - If task add is a success, confirm the user the task has been added, using [Browser Notification API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/notifications)
    - If task add has failed due to Authorization reasons, revoke the Access Token and go to `Step 3` 

## How to set up the project locally ?

See [Project Local Set Up Documentation](PROJECT_LOCAL_SETUP.md).  

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

## GCP AppEngine (for Java version of the API)
- [Google Cloud Endpoints Java8 Samples](https://github.com/GoogleCloudPlatform/java-docs-samples/tree/master/appengine-java8/endpoints-v2-backend)
- [Google Cloud Endpoints Param and Return Types](https://cloud.google.com/endpoints/docs/frameworks/java/parameter-and-return-types)  
- [Google Cloud Endpoints Exceptions](https://cloud.google.com/endpoints/docs/frameworks/java/exceptions)
- [Google AppEngine Logging](https://cloud.google.com/appengine/docs/standard/java/logs/)  

## GCP Cloud Functions (for Python version of the API)
- [Official Google Cloud Functions Doc](https://cloud.google.com/functions/docs/writing/http?hl=fr#writing_http_helloworld-python)
- [Official Google Cloud Functions Testing Doc](https://cloud.google.com/functions/docs/testing/test-http?hl=fr)

## API Client
- [Create a Java RESTFul client with Jersey client](https://o7planning.org/fr/11217/creer-java-restful-client-avec-jersey-client#a4487792)