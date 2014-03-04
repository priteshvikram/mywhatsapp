package com.priteshvikram.mywhatsapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListItemDetails extends Activity {
	
	ArrayList<String> list = new ArrayList<String>();
	public final static String EXTRA_MESSAGE = "com.priteshvikram.mywhatsapp.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	    
		setContentView(R.layout.activity_list_item_details);
	    
		ListView frienddetails = (ListView)findViewById(R.id.friendDetails);
		
		String[] values = new String[] { "Chat", "Profile", "Send Message" };
		for(int i=0;i<values.length;++i) 
			list.add(values[i]);
		final StableArrayAdapter adapter = new StableArrayAdapter(this,android.R.layout.simple_list_item_1, list);
		frienddetails.setAdapter(adapter);
		frienddetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      public void onItemClick(AdapterView<?> parent, final View view,int position, long id)
		      {
		        //final String item = (String) parent.getItemAtPosition(position);
		      }
		});
	}
	private class StableArrayAdapter extends ArrayAdapter<String>
	{
		public StableArrayAdapter(Context context, int textViewResourceId,List<String> objects)
		{
			super(context, textViewResourceId, objects);
		}

	 }
}
