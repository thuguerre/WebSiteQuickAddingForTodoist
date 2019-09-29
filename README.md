# TodoistQuickWebSiteURLAddAsTask

PROJECT UNDER DEVELOPMENT  
NOT EVEN IN AN ALPHA VERSION NOW :)

## How to contribute ?

This extension project is now under developement. Contributions are still difficult for the moment. However, you will be able to contribute soon !
- By translating texts other than in English and French,
- By creating issues, and even fixing them :)

## How it works ?

TODO

## For developers : how to set up the project ?

###Credentials
For security reasons, Todoist API credentials are not committed to the Git repository.  
For the moment, a very basic solution is used : credentials are externalized into files which are Git-ignored.  
A better solution will be found as soon as possible.

If you download this source code, to make the Browser Extension working, you have to create a `credentials.js` file in `BrowserExtension` directory. This file must look like :  
> const TEMP_TOKEN = "123456";

## Resources
- [Todoist REST API Documentation](https://developer.todoist.com/rest/v1/#create-a-new-task), for Task Add Service
- [Todoist Sync API Documentation](https://developer.todoist.com/sync/v8/#authorization), for Todoist Authorization Flow, based on OAuth
- [Browser Extension Mozilla Documentation](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/Your_first_WebExtension)
- [Browser Notifications API](https://developer.mozilla.org/en-US/docs/Mozilla/Add-ons/WebExtensions/API/notifications)