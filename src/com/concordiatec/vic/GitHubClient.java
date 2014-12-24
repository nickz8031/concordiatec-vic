package com.concordiatec.vic;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.concordiatec.vic.util.FileUtil;
import com.concordiatec.vic.util.LogUtil;
import com.google.gson.annotations.SerializedName;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

public class GitHubClient {
	private static final String API_URL = "http://demo1.remyjell.com/index.php?r=article";

	  static class Contributor {
		@SerializedName("a")
	    String first;
		@SerializedName("b")
	    String second;
	  }
	  
	  static class Filer{
		  String name;
		  String type;
		  @SerializedName("tmp_name")
		  String tmpName;
		  int error;
		  int size;
		@Override
		public String toString() {
			return "Filer [name=" + name + ", type=" + type + ", tmpName=" + tmpName + ", error=" + error + ", size=" + size + "]";
		}
		  
	  }

	  interface GitHub {
		  	@GET("/article")
		  	String getArticle( @Query(value = "sign") String sign );
		  	
			@POST("/api/testAct")
			List<Contributor> contributors();
			
			@Multipart
			@POST("/api/testAct")
			void postPhoto(
			    		@PartMap Map<Integer , TypedFile> photo, 
			    		@PartMap Map<Integer , String> desc,
			    		Callback<Filer> cb
					);
	  }

	  public static void main( String filePath , String desc ) {
	    // Create a very simple REST adapter which points the GitHub API endpoint.
	    RestAdapter restAdapter = new RestAdapter.Builder()
	        .setEndpoint(API_URL)
	        .build();

	    // Create an instance of our GitHub API interface.
	    GitHub github = restAdapter.create(GitHub.class);

	    
	    // Fetch and print a list of the contributors to this library.
	    File file = new File(filePath);
	    TypedFile f1 = new TypedFile( FileUtil.getMIMEType(file) , file);
	    TypedFile f2 = f1;
	    Map<Integer , TypedFile> uFile = new HashMap<Integer, TypedFile>();
	    uFile.put(1, f1);
	    uFile.put(2, f2);
	    Map<Integer, String> descMap = new HashMap<Integer, String>();
	    descMap.put(1, desc);
	    descMap.put(2, desc);
	    github.postPhoto( uFile , descMap , new Callback<GitHubClient.Filer>() {

			@Override
			public void failure(RetrofitError arg0) {
			}

			@Override
			public void success(Filer arg0, Response arg1) {
				LogUtil.show(arg0.toString());
			}
	    	
	    } );
	    
	    
//	    for (Contributor contributor : contributors) {
//	      LogUtil.show(contributor.first + " (" + contributor.second + ")");
//	    }
	    
	    
	  }
}
