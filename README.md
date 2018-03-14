# OneNote Content Push
## Java SDK Sample for Microsoft Graph
Sample using the Java Graph SDK to push OneDrive content to OneNote using multipart POST

## How to Run

### Register an application
1. Register an app at [apps.dev.microsoft.com](apps.dev.microsoft.com/portal/register-app)

    a) Copy-paste the application ID into the [`Constants`](https://github.com/cbales/onenote-content-push-java-graph-sample/blob/master/src/com/graph/contentPush/Constants.java) file as `clientId`
    
    b) Click "Generate New Password" to get an application secret
    
    c) Copy-paste the application secret into the [`Constants`](https://github.com/cbales/onenote-content-push-java-graph-sample/blob/master/src/com/graph/contentPush/Constants.java) file as `clientSecret`
    
    d) Click "Add a Platform" > "Web"
        *This console app will not have a UI, but this platform will be used during the admin consent in step 2*
        
    e) Set the redirect url to "http://localhost"
    
    f) Under Application Permissions, select:
    
        - Directory.ReadWrite.All
        
        - Group.ReadWrite.All
        
        - Notes.ReadWrite.All
        
    g) Hit "Save"
    
2. Authorize your app to access the permissions you requested in your registration

   a) Navigate to the URL:
  
  ```HTML
  https://login.microsoftonline.com/{tenant}/adminconsent
  ?client_id={client_id}
  &state=12345
  &redirect_uri=http://localhost
```

   b) Log in as an administrator to approve the scopes you requested
  
3. Update the code to match your tenant

    a) In [`OnenoteSync`](https://github.com/cbales/onenote-content-push-java-graph-sample/blob/master/src/com/graph/contentPush/OneNoteSync.java), update the group to search for to match a group in your tenant

4. Build and run the console app

## Learn More
This sample uses the Java SDK for Microsoft Graph. You can learn more on the project's [wiki](https://github.com/microsoftgraph/msgraph-sdk-java/wiki).
