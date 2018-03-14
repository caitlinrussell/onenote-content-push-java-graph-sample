package com.graph.contentPush;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.core.DefaultClientConfig;
import com.microsoft.graph.core.IClientConfig;
import com.microsoft.graph.models.extensions.DriveItem;
import com.microsoft.graph.models.extensions.Group;
import com.microsoft.graph.models.extensions.IGraphServiceClient;
import com.microsoft.graph.models.extensions.Multipart;
import com.microsoft.graph.models.extensions.Notebook;
import com.microsoft.graph.models.extensions.OnenotePage;
import com.microsoft.graph.models.extensions.OnenoteSection;
import com.microsoft.graph.options.HeaderOption;
import com.microsoft.graph.options.Option;
import com.microsoft.graph.options.QueryOption;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IDriveItemCollectionPage;
import com.microsoft.graph.requests.extensions.IGroupCollectionPage;

public class OneNoteSync {
	
	public static IGraphServiceClient graphClient;
	
	public static void main ( String [] arguments )
    {
		//Get an access token for the client
        Authenticator authenticator = new Authenticator();
        IAuthenticationProvider authenticationProvider = authenticator
        		.getAuthenticationProvider();
        
        //Create a new Graph client using the default client config and our auth provider
        IClientConfig clientConfig = DefaultClientConfig
        		.createWithAuthenticationProvider(authenticationProvider);
        graphClient = GraphServiceClient
        		.builder()
        		.fromConfig(clientConfig)
        		.buildClient();
        
       
        
        //Get our IT group
        Option filterOption = new QueryOption("$filter", "displayName eq 'IT'");
        IGroupCollectionPage groups = graphClient
        		.groups()
        		.buildRequest(Arrays.asList(filterOption))
        		.get();
        Group itGroup = groups.getCurrentPage().get(0);
        
        HashMap<String, String> spLinks = getRelevantOneDriveLinks("mobile", itGroup.id);
        String content = createHtmlList(spLinks);
        
        //Get our IT group's default OneNote notebook
        Notebook defaultBook = graphClient
        		.groups(itGroup.id)
        		.onenote()
        		.notebooks()
        		.buildRequest()
        		.get()
        		.getCurrentPage()
        		.get(0);
        
        //Get our notebook's default section
        OnenoteSection defaultSection = graphClient
        		.groups(itGroup.id)
        		.onenote()
        		.notebooks(defaultBook.id)
        		.sections()
        		.buildRequest()
        		.get()
        		.getCurrentPage()
        		.get(0);

        //Post our links to the OneNote section
        postContent(content, itGroup.id, defaultSection.id);
        
        System.out.println("Posted content!");
    }
	
	/**
	 * Get a list of OneDrive links that match a given search string for a group
	 * 
	 * @param searchString the string to search for
	 * @param groupId the group to search the drive of
	 * @return a dictionary of names and URLs that match the search query
	 */
	public static HashMap<String, String> getRelevantOneDriveLinks(String searchString, String groupId) {
		Option searchOption = new QueryOption("$search", searchString);
		IDriveItemCollectionPage oneDriveItems = graphClient
				.groups(groupId)
				.drive()
				.root()
				.children()
				.buildRequest(Arrays.asList(searchOption))
				.get();
		HashMap<String, String> oneDriveLinks = new HashMap<String, String>();
		
		List<DriveItem> itemList = oneDriveItems.getCurrentPage();
		
		while (itemList != null) {
			for (DriveItem item : itemList) {
				oneDriveLinks.put(item.name, item.webUrl);
			}
			
			if (oneDriveItems.getNextPage() == null)
				break;
			itemList = oneDriveItems
					.getNextPage()
					.buildRequest()
					.get()
					.getCurrentPage();
		}
		return oneDriveLinks;
	}
	
	/**
	 * Post content to a given OneNote notebook section
	 * @param content the HTML content to post
	 * @param groupId the group that owns the notebook
	 * @param sectionId the section to post to
	 */
	public static void postContent(String content, String groupId, String sectionId) {
		//This sets up the multipart request
		Multipart multipart = new Multipart();
    	
		//Replace our HTML template placeholder with our links
    	String htmlBody= Constants.htmlContent.replace("[replace-content]", content);
    	
    	//You can also add file parts
    	File imgFile = new File("src/assets/bike.jpg");
    	File pdfFile = new File("src/assets/hardware-request-process.pdf");
    	
    	try {
    		//Add all the files to the multipart request
	    	multipart.addHtmlPart("Presentation", htmlBody);
	    	multipart.addImagePart("bike", imgFile);
	    	multipart.addFilePart("hardware-request-process", "application/pdf", pdfFile);
	    	
	        // Add multipart request header
	        List<Option> options = new ArrayList<Option>();
	        options.add(new HeaderOption(
	        		"Content-Type", "multipart/form-data; boundary=\"" + multipart.boundary() + "\""
	        		));
	        
	        // Post the multipart content
			OnenotePage page = graphClient
					.groups(groupId)
					.onenote()
					.sections(sectionId)
					.pages()
					.buildRequest(options)
					.post(multipart.content());
    	} catch (Exception e) {
			e.printStackTrace();
    	}
	}
	
	/**
	 * HTML helper to transform our dictionary into a list
	 * @param spLinks the titles and URLs of links
	 * @return an HTML string of the links
	 */
	public static String createHtmlList(HashMap<String, String> spLinks) {
		String content = "<p>";
        Iterator it = spLinks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            content += "<a href=\"" + pair.getValue() + "\">" + pair.getKey() + "</a>";
            content += "<br/>";
            it.remove();
        }
        content += "</p>";
        return content;
	}
}
