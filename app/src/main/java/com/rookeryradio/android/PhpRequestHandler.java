package com.rookeryradio.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.rookeryradio.android.util.Base64;
import com.rookeryradio.android.util.JSONParser;

public class PhpRequestHandler {
	public static String ipv4ip = null;
	private RadioService service;
	
	PhpRequestHandler(RadioService rs){
		service = rs;
	}

	boolean choon() {
		return sendRequestChoon("song_rate", "choon", "3");
	}

	boolean poon() {
		return sendRequestPoon("song_rate", "poon", "4");
	}

	boolean request(String request) {
		return sendRequestRequest("request", request, "null");
	}

	boolean shoutout(String request) {
		return sendRequestShoutout("shoutout", request, "null");
	}

	boolean djftw(String dj) {
		return sendRequestDJFTW("djrate", dj, "0");
	}

	JSONObject getList() {
		JSONParser parser = new JSONParser();
		return parser.getJSONFromUrl("http://stream.rookeryradio.com:8088/status-json.xsl");
	}

	private boolean sendRequestPoon(String type, String request, String votetype) {
		String serverEncode = encode("Android app");
		String encodedUser = encode(service.getUsername());
		String encodedData = encode(request);
		String url1;
		if (type.equalsIgnoreCase("song_rate"))
			//url1 = "http://blha303.com.au/h365vote.php?type=" + type + "&user=" + encodedUser + "&server=" + serverEncode + "&data=" + votetype;
			url1 = "http://hive365.co.uk/android/app-rating.php?t=4&n="+ encodedUser;
		else {
			url1 = "http://hive365.co.uk/android/app-rating.php?t=4&n="+ encodedUser;
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
	private boolean sendRequestChoon(String type, String request,
								String votetype) {
		String serverEncode = encode("Android app");
		String encodedUser = encode(service.getUsername());
		String encodedData = encode(request);
		String url1;
		if (type.equalsIgnoreCase("song_rate"))
			//url1 = "http://blha303.com.au/h365vote.php?type=" + type + "&user=" + encodedUser + "&server=" + serverEncode + "&data=" + votetype;
			url1 = "http://hive365.co.uk/android/app-rating.php?t=3&n="+ encodedUser;
		else {
			url1 = "http://hive365.co.uk/android/app-rating.php?t=3&n="+ encodedUser;
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
	private boolean sendRequestShoutout(String type, String request,
								String votetype) {
		String serverEncode = encode("Android app");
		String encodedUser = encode(service.getUsername());
		String encodedData = encode(request);
		String url1;
		if (type.equalsIgnoreCase("song_rate"))
			url1 = "http://hive365.co.uk/android/app-shoutout.php?s="+encodedData+"&n="+encodedUser;;
		//else {
			url1 = "http://hive365.co.uk/android/app-shoutout.php?s="+encodedData+"&n="+encodedUser;

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
	private boolean sendRequestRequest(String type, String request,
								String votetype) {
		String serverEncode = encode("Android app");
		String encodedUser = encode(service.getUsername());
		String encodedData = encode(request);
		String url1;
		if (type.equalsIgnoreCase("song_rate"))
			//url1 = "http://blha303.com.au/h365vote.php?type=" + type + "&user=" + encodedUser + "&server=" + serverEncode + "&data=" + votetype;
			url1 = "http://hive365.co.uk/android/app-request.php?s="+encodedData+"&n="+encodedUser;
		else {
			url1 = "http://hive365.co.uk/android/app-request.php?s="+encodedData+"&n="+encodedUser;;
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

	private boolean sendRequestDJFTW(String type, String request,
								String votetype) {
		String serverEncode = encode("Android app");
		String encodedUser = encode(service.getUsername());
		String encodedData = encode(request);
		String url1;
		if (type.equalsIgnoreCase("song_rate"))
			//url1 = "http://blha303.com.au/h365vote.php?type=" + type + "&user=" + encodedUser + "&server=" + serverEncode + "&data=" + votetype;
			url1 = "http://hive365.co.uk/android/app-rating.php?t=5&n="+ encodedUser;
		else {
			url1 = "http://hive365.co.uk/android/app-rating.php?t=5&n="+ encodedUser;
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
