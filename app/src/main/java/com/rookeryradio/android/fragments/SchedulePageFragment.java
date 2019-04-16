package com.rookeryradio.android.fragments;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.rookeryradio.android.MainActivity;
import com.rookeryradio.android.R;
import com.rookeryradio.android.fragments.schedule.Item;
import com.rookeryradio.android.fragments.schedule.ScheduleAdapter;
import com.rookeryradio.android.fragments.schedule.XMLParser;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

public class SchedulePageFragment extends PageFragment{
	String dataUrl = "http://stream.rookeryradio.com/rookery/Schedule.xml";
	XMLParser data;

	public SchedulePageFragment() {
		super(R.layout.schedule_page);
	}

	@Override
	public String getTitle() {
		return "Schedule";
	}

	@Override
	public void loadLayout(View v) {
		ExpandableListView list = (ExpandableListView)v.findViewById(R.id.scheduleList);


		try {
			data = new FetchXml().execute("").get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(data!=null)
		list.setAdapter(new ScheduleAdapter(this.getActivity(), data.getList(), data.getDays()));
	}

	private class FetchXml extends AsyncTask<String, Void, XMLParser> {

		@Override
		protected XMLParser doInBackground(String... params) {
			HashMap<String, List<String>> returnList = new HashMap<String, List<String>>();

			try {
				URL url = new URL(dataUrl);
				HttpURLConnection conn = (HttpURLConnection) 
						url.openConnection();
				conn.setReadTimeout(10000 /* milliseconds */);
				conn.setConnectTimeout(15000 /* milliseconds */);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				conn.connect();
				InputStream stream = conn.getInputStream();

				XMLParser parser=new XMLParser();
				SAXParserFactory factory=SAXParserFactory.newInstance();
				SAXParser sp=factory.newSAXParser();
				XMLReader reader=sp.getXMLReader();
				reader.setContentHandler(parser);
				reader.parse(new InputSource(stream));

				returnList = parser.getList();
				if(returnList != null){
					return parser;
				}

				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;

		}      
	}
}
