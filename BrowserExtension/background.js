const TASK_ADD_NOTIFICATION_ID = "task-add-notification";
const TODOIST_ACCESS_TOKEN_STORAGE_ID = "todoist_access_token";
const TODOIST_PROXY_API_REDIRECT_URL = "https://todoistquickwebsiteadd.appspot.com/api/todoistProxyAPI/v1/oauth-callback";
const TODOIST_PROXY_API_ACCESS_TOKEN = "https://todoistquickwebsiteadd.appspot.com/api/todoistProxyAPI/v1/access-token/";
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
     onErrorToGetAccessTokenFromStorage("sucessfully accessed the browser storage but no valid token value found");
  } else {
    console.log("got access token from sync storage : " + todoist_access_token);
    launchAddTaskFlow();    
  }  
}

function onErrorToGetAccessTokenFromStorage(error) {
  console.info("no token got from browser storage : " + error);
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
  
  const redirectURL = TODOIST_PROXY_API_REDIRECT_URL;
  const scopes = ["task:add", "data:read_write"];
  const state = uuidv4(); 
  
  console.log("Extension RedirectURL : " + redirectURL);
   
  let authURL = "https://todoist.com/oauth/authorize";
  authURL += `?client_id=${CLIENT_ID}`;
  authURL += `&scope=${encodeURIComponent(scopes.join(','))}`;
  authURL += `&state=${state}`;
  // TODO authURL += `&redirect_uri=${encodeURIComponent(redirectURL)}`;
            
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
  
  // call our Todoist Proxy API to exchange our state against a valid access token
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
  
    if (this.readyState == 4 && this.status == 200) {

      console.log("Todoist Proxy API response : " + this.responseText);
      var token = this.responseText.match(/\"message\":\"([0-9a-fA-F]{40})\"/)[1];
      console.log("token got from ws : " + token);
              
      todoist_access_token = token;
      console.log("valid access token got : " + todoist_access_token);
      
      // store the access token in browser storage to use it directly in a future call 
      setTodoistAccessTokenInBrowserStorage(todoist_access_token);   

      // now we have authenticated the user, we can launch the addtask Flow
      launchAddTaskFlow();
            
    } else if (this.readyState == 4) {
      
      console.error("token not retrieved from Proxy API ; status=" + this.status);
      console.error("error from API : " + this.responseText);
    }
  };
  xhttp.open("GET", TODOIST_PROXY_API_ACCESS_TOKEN + state, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.send(null);
  
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
  xhttp.open("POST", "https://api.todoist.com/rest/v1/tasks", true);
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
   
  todoist_access_token = null;
  browser.storage.local.remove(TODOIST_ACCESS_TOKEN_STORAGE_ID);
  console.log("access token remove from browser storage");
      
  // TODO has to call our own revoke API for this access token
  console.log("access token revoked from Todoist.");
      
  console.info("access token revoked from everywhere.");
}