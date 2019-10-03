const TASK_ADD_NOTIFICATION_ID = "task-add-notification";
const TODOIST_ACCESS_TOKEN_STORAGE_ID = "todoist_access_token";

const TODOIST_CLIENT_ID = "51ba8ae54b9146be839bd0561002f081";
const TODOIST_AUTHORIZE_URL = "https://todoist.com/oauth/authorize";
const TODOIST_ADD_TASK_API = "https://api.todoist.com/rest/v1/tasks";
const TODOIST_SCOPES = ["task:add", "data:read_write"];

const TODOIST_PROXY_API_ACCESS_TOKEN = "https://todoistquickwebsiteadd.appspot.com/api/todoistProxyAPI/v1/access-token/";
const REDIRECT_URL = browser.identity.getRedirectURL();

var todoist_access_token;

// all starts from this listener set on browser extension button click
browser.browserAction.onClicked.addListener(clickOnButton);

function clickOnButton() {

  // TODO delete once useless
  // setTodoistAccessTokenInBrowserStorage(TEMP_TOKEN);
  cleanAccessTokenFromEveryWhere();
    
  // trying to get access token from storage
  browser.storage.local.get(TODOIST_ACCESS_TOKEN_STORAGE_ID).then(gotAccessTokenFromStorage, onErrorToGetAccessTokenFromStorage);
}

function gotAccessTokenFromStorage(result) {

  // getting access token from result object
  todoist_access_token = result[TODOIST_ACCESS_TOKEN_STORAGE_ID];
  
  if(todoist_access_token == null || typeof todoist_access_token != 'string') {
     onErrorToGetAccessTokenFromStorage();
  } else {
    console.log("got access token from sync storage : " + todoist_access_token);
    launchAddTaskFlow();    
  }  
}

function onErrorToGetAccessTokenFromStorage() {
  console.info("no token got from browser storage");
  cleanAccessTokenFromEveryWhere(); 
  launchAuthorizationFlow();
}


/* 
 * AUTHORIZATION FLOW
 */
function launchAuthorizationFlow() {
    return startTodoistAuthorizationFlow().then(retrieveTodoistToken);
}

function startTodoistAuthorizationFlow() {

  console.log("start todoist authorization flow");
  console.log("extension's RedirectURL : " + REDIRECT_URL);  
  const state = uuidv4(); 
                   
  let authURL = TODOIST_AUTHORIZE_URL;
  authURL += `?client_id=${TODOIST_CLIENT_ID}`;
  authURL += `&scope=${encodeURIComponent(TODOIST_SCOPES.join(','))}`;
  authURL += `&state=${state}`;
  authURL += `&redirect_uri=${encodeURIComponent(REDIRECT_URL)}`;
            
  return browser.identity.launchWebAuthFlow({
    interactive: true,
    url: authURL
  });
}

function retrieveTodoistToken(redirectURL) {

  console.log("start retrieveTodoistToken");
   
  const state = redirectURL.match(/state=([0-9a-fA-F]{8}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{4}\-[0-9a-fA-F]{12})/)[1];
  const code = redirectURL.match(/code=([0-9a-fA-F]{40})/)[1];

  console.log("OAUTH State got from Todoist : " + state);
  console.log("OAUTH Code got from Todoist : " + code);
  
  var xhttpContent = "{\"state\":\"" + state + "\", \"code\":\"" + code + "\"}";
  
  // call our Todoist Proxy API to exchange our state against a valid access token
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
  
    if (this.readyState == 4 && this.status == 200) {

      todoist_access_token = this.responseText.match(/\"accessToken\": \"([0-9a-fA-F]{40})\"/)[1];
      console.log("access token : " + todoist_access_token);
      
      // store the access token in browser storage to use it directly in a future call 
      setTodoistAccessTokenInBrowserStorage(todoist_access_token);   

      // now we have authenticated the user, we can launch the addtask Flow
      launchAddTaskFlow();
            
    } else if (this.readyState == 4) {
      
      console.error("token not retrieved from Proxy API ; status=" + this.status);
      console.error("error from API : " + this.responseText);
    }
  };
  xhttp.open("POST", TODOIST_PROXY_API_ACCESS_TOKEN, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.send(xhttpContent);
  
  console.log("end of retrieveTodoistToken");
}


/* 
 * ADD TASK FLOW
 */
function launchAddTaskFlow() {

  // protecting the flow from a recurent call
  if(todoist_access_token != null) {
    // we retrieve the current and active tab to add its URL as a task
    browser.tabs.query({currentWindow: true, active: true}).then(onTabGot, onErrorToGetTab);
  } else {
    console.error("launchAddTaskFlow has been called without a valid access token. Stop the flow.")
  }
}

function onTabGot(tabInfo) {

  console.log("active tab obtained.");
  console.log("getting active tab URL to add it as task in Todoist Inbox");

  // create the JSON data request, from current tab's URL      
  var currentTabUrl = tabInfo[0].url;
  var xhttpContent = '{"content": "' + currentTabUrl + '"}';
  
  // call Todoist API to add task
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
  
    if (this.readyState == 4 && this.status == 200) {    
      console.info("URL added as task in Inbox !");
      confirmTaskCreationToUser();
      
    } else if (this.readyState == 4 && this.status == 403) {
      
      console.warn("token not authorized to add task. revoking it, and ask for a new one.");
      
      cleanAccessTokenFromEveryWhere();    
      return launchAuthorizationFlow();
    }
  };
  xhttp.open("POST", TODOIST_ADD_TASK_API, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.setRequestHeader("X-Request-Id", "$(" + uuidv4() + ")");
  xhttp.setRequestHeader("Authorization", "Bearer " + todoist_access_token);
  xhttp.send(xhttpContent);
}

function onErrorToGetTab(error) {
  console.error(error);
}

function confirmTaskCreationToUser() {
  browser.notifications.create(TASK_ADD_NOTIFICATION_ID, {
    "type": "basic",
    "iconUrl": browser.runtime.getURL("icons/border-48.png"),
    "title": browser.i18n.getMessage("taskAddConfirmationTitle"),
    "message": browser.i18n.getMessage("taskAddConfirmationMessage")
  });
}


/* 
 * BROWSER STORAGE MANAGEMENT
 */
function setTodoistAccessTokenInBrowserStorage(token) {
  var obj = {};
  obj[TODOIST_ACCESS_TOKEN_STORAGE_ID] = token;
  browser.storage.local.set(obj).then(onTokenStoredToBrowserStorage, onErrorToStoreTokenInBrowserStorage);
}

function onTokenStoredToBrowserStorage() {
  console.info("token has been stored in browser storage for a future reuse.");
}

function onErrorToStoreTokenInBrowserStorage(error) {
  console.error("fail to store acess token in browser storage : " + error);
}


/* 
 * TOOLS
 */
function uuidv4() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
    return v.toString(16);
  });
}


function cleanAccessTokenFromEveryWhere() {

  // clearing the access token the browser storage   
  browser.storage.local.remove(TODOIST_ACCESS_TOKEN_STORAGE_ID);
  console.log("access token remove from browser storage");
  
  // call our Todoist Proxy API to revoke the access token
  var xhttpContent = "{\"accessToken\":\"" + todoist_access_token + "\"}";
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
  
    if (this.readyState == 4 && this.status == 200) {

      console.log("access token revoked from Todoist.");
      console.log(this.responseText);
                  
    } else if (this.readyState == 4) {
      
      console.warn("unable to revoke the access token from Todoist. status=" + this.status);
      console.warn("error from API : " + this.responseText);
    }
  };
  xhttp.open("DELETE", TODOIST_PROXY_API_ACCESS_TOKEN, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.send(xhttpContent);

  // clearing 'local' variable
  todoist_access_token = null;      
  console.info("access token revoked from everywhere.");
}