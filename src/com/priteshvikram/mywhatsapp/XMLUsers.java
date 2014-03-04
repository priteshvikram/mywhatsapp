package com.priteshvikram.mywhatsapp;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XMLUsers {
	
	public List<User> us=new ArrayList<User>();
	
	XMLUsers(String d)
	{
		XmlPullParserFactory pullParserFactory;
		User u=null;
        String text=null;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			    InputStream in_s = new ByteArrayInputStream(d.getBytes("UTF-8"));
		        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	            parser.setInput(in_s, null);

	            int eventType = parser.getEventType();
	            while (eventType != XmlPullParser.END_DOCUMENT) {
	                String tagname = parser.getName();
	                switch (eventType) {
	                case XmlPullParser.START_TAG:
	                    if (tagname.equalsIgnoreCase("user")) {
	                        // create a new instance of employee
	                    	u = new User();
	                    }
	                    break;
	 
	                case XmlPullParser.TEXT:
	                    text = parser.getText();
	                    break;
	 
	                case XmlPullParser.END_TAG:
	                    if (tagname.equalsIgnoreCase("user")) {
	                        // add employee object to list
	                        us.add(u);
	                    } else if (tagname.equalsIgnoreCase("name")) {
	                        u.name=text;
	                    } else if (tagname.equalsIgnoreCase("icon")) {
	                        u.icon=text;
	                    }
	                    break;
	 
	                default:
	                    break;
	                }
	                eventType = parser.next();
	            }

		} catch (XmlPullParserException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
