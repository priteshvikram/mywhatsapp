package com.priteshvikram.mywhatsapp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Home extends Activity {
	
	ArrayList<String> list = new ArrayList<String>();
	
	public final static String EXTRA_MESSAGE = "com.priteshvikram.mywhatsapp.MESSAGE";
	
	protected  NetworkInfo checkConnectivity(){
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(Login.EXTRA_MESSAGE);
		//Log.d("data2",message);

		ListView friendlist = (ListView)findViewById(R.id.friendlist);
		
		NetworkInfo ni = checkConnectivity();
		if (ni != null && ni.isConnected())
	    {
			DownloadXMLData d=new DownloadXMLData();
			d.execute("http://priteshvikram-dawud.rhcloud.com/chat.php","ALL",message);
			final ArrayAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
			friendlist.setAdapter(adapter);
			friendlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			      public void onItemClick(AdapterView<?> parent, final View view,int position, long id)
			      {
			        final String item = (String) parent.getItemAtPosition(position);
			        Intent iteminfo = new Intent(Home.this,ListItemDetails.class);
			        iteminfo.putExtra(EXTRA_MESSAGE, item);
			        startActivity(iteminfo);
			      }
			});
	    }
	}
	
	private class DownloadXMLData extends AsyncTask<String, Void, String> {
		
		   private static final String DEBUG_TAG = "XMLData";
		   private int userid=1;
		   
		   @Override
	       protected String doInBackground(String... urls) {
	              
			   try {
	            	return downloadUrl(urls[0],urls[1],Integer.parseInt(urls[2]));
	            } catch (IOException e) {
	                return "Unable to retrieve web page. URL may be invalid.";
	            }
	       }
	       // onPostExecute displays the results of the AsyncTask.
	       @Override
	       protected void onPostExecute(String result) {
	    	XMLUsers xu=new XMLUsers(result);
	   		for(User l:xu.us) {
	   			list.add(l.name);
	   			//Log.d("data2",l.name);
	   		}
	       }
	       private String downloadUrl(String myurl,String n,int id) throws IOException {
	    	    InputStream is = null;
	    	    // Only display the first 500 characters of the retrieved
	    	    // web page content.
	    	    int len = 500;
	    	    String urlParameters="users="+n+"&userid=";
	    	    if(n=="ALL")
	    	    	urlParameters+=userid;
	    	    else if(n=="SPECIFIC")
	    	    	urlParameters+=id;
	    	    try {
	    	        URL url = new URL(myurl);
	    	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	    	        conn.setReadTimeout(10000 /* milliseconds */);
	    	        conn.setConnectTimeout(15000 /* milliseconds */);
	    	        conn.setRequestMethod("POST");
	    	        conn.setDoInput(true);
	    	        conn.setDoOutput(true);
	    	        conn.setInstanceFollowRedirects(false);
	    	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
	    	        conn.setRequestProperty("charset", "utf-8");
	    	        conn.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	    	        conn.setUseCaches (false);
	    	        // Starts the query
	    	        DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
	    	        wr.writeBytes(urlParameters);
	    	        wr.flush();
	    	        
	    	        int response = conn.getResponseCode();
	    	        //Log.d(DEBUG_TAG, "The response is: " + response);
	    	        is = conn.getInputStream();

	    	        // Convert the InputStream into a string
	    	        String contentAsString = readIt(is, len).replaceFirst("\\s+$", "");
	    	        //Log.d("data",contentAsString);
	    	        return contentAsString.toString();
	    	        
	    	    // Makes sure that the InputStream is closed after the app is
	    	    // finished using it.
	    	    } finally {
	    	        if (is != null) {
	    	            is.close();
	    	        } 
	    	    }
	       }
	       public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
	    	BufferedReader reader = null;
	   	    reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));        
	   	    String buffer;
	   	    buffer = reader.readLine();
	   	    return buffer;
	   	   }
	}
}
