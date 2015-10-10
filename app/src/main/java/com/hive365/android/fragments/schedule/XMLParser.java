package com.hive365.android.fragments.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class XMLParser extends DefaultHandler
{


	private List<Item> list=null;

	// string builder acts as a buffer
	StringBuilder builder;

	Item scheduleItem=null;


	// Initialize the arraylist
	// @throws SAXException
	
	private List<String> parseShows(String data){
		List<String> temp = new ArrayList<String>();
		for(String show : data.split("\n")){
			String sh = show.replace("[/b]", "").replace("[b]", "").replace("\n", "").replace("\r", "").trim();
			if(!sh.equals("")){
				temp.add(sh);
			}
		}
		return temp;
	}

	@Override
	public void startDocument() throws SAXException {

		/******* Create ArrayList To Store XmlValuesModel object ******/
		list = new ArrayList<Item>();
	}


	// Initialize the temp XmlValuesModel object which will hold the parsed info
	// and the string builder that will store the read characters
	// @param uri
	// @param localName ( Parsed Node name will come in localName  )
	// @param qName
	// @param attributes
	// @throws SAXException

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		/****  When New XML Node initiating to parse this function called *****/

		// Create StringBuilder object to store xml node value
		builder=new StringBuilder();

		if(localName.equals("schedule")){

			//Log.i("parse","====login=====");
		}
		else if(localName.equals("scheditem")){

			// Log.i("parse","----Job start----");
			/********** Create Model Object  *********/
			scheduleItem = new Item();
		}
	}



	// Finished reading the login tag, add it to arraylist
	// @param uri
	// @param localName
	// @param qName
	// @throws SAXException

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {


		if(localName.equals("scheditem")){

			/** finished reading a job xml node, add it to the arraylist **/
			list.add( scheduleItem );

		}

		else  if(localName.equalsIgnoreCase("title")){  

			scheduleItem.setDay(builder.toString());
		}

		else  if(localName.equalsIgnoreCase("schedpost")){  
			
			scheduleItem.setShows(parseShows(builder.toString()));
		}


		// Log.i("parse",localName.toString()+"========="+builder.toString());
	}


	// Read the value of each xml NODE
	// @param ch
	// @param start
	// @param length
	// @throws SAXException
	
	public HashMap<String, List<String>> getList(){
		HashMap<String, List<String>> temp = new HashMap<String, List<String>>();
		for(Item i : list){
			temp.put(i.getDay(), i.getShows());
		}
		return temp;
	}
	
	public List<String> getDays(){
		List<String> temp = new ArrayList<String>();
		for(Item i : list){
			temp.add(i.getDay());
		}
		return temp;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {

		/******  Read the characters and append them to the buffer  ******/
		String tempString=new String(ch, start, length);
		builder.append(tempString);
	}
}