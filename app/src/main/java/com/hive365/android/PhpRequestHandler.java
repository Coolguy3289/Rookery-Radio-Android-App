package com.hive365.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.hive365.android.util.Base64;
import com.hive365.android.util.JSONParser;

public class PhpRequestHandler {
	public static String ipv4ip = null;
	private RadioService service;
	
	public PhpRequestHandler(RadioService rs){
		service = rs;
	}

	public boolean choon() {
		return sendRequest("song_rate", "choon", "3");
	}

	public boolean poon() {
		return sendRequest("song_rate", "poon", "4");
	}

	public boolean request(String request) {
		return sendRequest("request", request, "null");
	}

	public boolean shoutout(String request) {
		return sendRequest("shoutout", request, "null");
	}

	public boolean djftw(String dj) {
		return sendRequest("djrate", dj, "0");
	}

	public JSONObject getList() {
		JSONParser parser = new JSONParser();
		return parser.getJSONFromUrl("http://thegamingcorner.net:8000/status-json.xsl");
	}

	private boolean sendRequest(String type, String request,
			String votetype) {
		String serverEncode = encode("Android app");
		String encodedUser = encode(service.getUsername());
		String encodedData = encode(request);
		String url1;
		if (type.equalsIgnoreCase("song_rate"))
			url1 = "http://blha303.com.au/h365vote.php?type=" + type + "&user=" + encodedUser + "&server=" + serverEncode + "&data=" + votetype;
		else {
			url1 = "http://blha303.com.au/h365vote.php?type=" + type + "&user=" + encodedUser + "&server=" + serverEncode + "&data=" + encodedData;
		}
		try {
			URL url = new URL(url1);
			HttpURLConnection yc = (HttpURLConnection) url.openConnection();
			yc.addRequestProperty("User-Agent", "Mozilla/4.76");
			InputStreamReader isr = new InputStreamReader(yc.getInputStream());
			BufferedReader in = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				System.out.println(inputLine);
			}
			in.close();
			isr.close();
			yc.disconnect();
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public byte[] zeroPad(int length, byte[] bytes) {
		byte[] padded = new byte[length];
		System.arraycopy(bytes, 0, padded, 0, bytes.length);
		return padded;
	}

	public String encode(String string) {
		
		return Base64.encodeToString(string.getBytes(), Base64.DEFAULT).replace("\n", "").replace("\r", "");
	}

	public String decode(String string) {
		byte[] a = Base64.decode(string.getBytes(), Base64.DEFAULT);
		String b = new String(a);
		return b;
	}

	public static String splitLines(String string) {
		String lines = "";
		for (int i = 0; i < string.length(); i += 76) {
			lines = lines
					+ string.substring(i, Math.min(string.length(), i + 76));
		}

		return lines;
	}
}
