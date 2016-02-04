package com.peaceworld.wikisms.controller.rs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Base64;

import com.google.gson.Gson;

public class Utilities  {

	
	public static String SendPostRequest(String url) {

		try {
			HttpPost httppost = new HttpPost(url);
			return SendHttpRequest(httppost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String SentGetRequest(String url) {

		try {
			HttpGet httpget = new HttpGet();
			httpget.setURI(new URI(url));
			return SendHttpRequest(httpget);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return null;
		}

	}

	
	
	public static String SendHttpRequest(HttpUriRequest request) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(request);
			return GetHttpResponseContent(response);

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String GetHttpResponseContent(HttpResponse response)
			throws IllegalStateException, IOException {
		StatusLine statusLine = response.getStatusLine();
		if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			response.getEntity().writeTo(out);
			out.close();
			String responseString = out.toString();
			return responseString;
		} else {
			// Closes the connection.
			response.getEntity().getContent().close();

		}
		return null;

	}
	
	public static String toJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

	public static String gzipCompress(String data) {

		try {

			byte[] binaryData = data.getBytes("UTF-8");
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			GZIPOutputStream gzos = new GZIPOutputStream(outStream);
			gzos.write(binaryData, 0, binaryData.length);
			gzos.finish();
			gzos.close();
			binaryData = null;
			byte[]outStreamByteArray=outStream.toByteArray();
			//ApacheBase64.encodeBase64String(outStreamByteArray);
			String compressedData=Base64.encodeToString(outStreamByteArray, Base64.DEFAULT); 
			outStream.close();
			return compressedData;
		} catch (IOException ex) {
			return ex.toString();
		}

	}

	
	public static String gzipUncompress(String data){
		 
	     byte[] buffer = new byte[1024];
	 
	     try{
	    	 byte[] bytearray = Base64.decode(data, Base64.DEFAULT);//ApacheBase64.decodeBase64(data);
	    	 ByteArrayInputStream inStream = new ByteArrayInputStream(bytearray);
	    	 GZIPInputStream gzis = new GZIPInputStream(inStream);
	 
	    	 ByteArrayOutputStream outStream=new ByteArrayOutputStream();
	    	 
	        int len;
	        while ((len = gzis.read(buffer)) > 0) {
	        	outStream.write(buffer, 0, len);
	        }
	        gzis.close();
	        inStream.close();
	        bytearray=null;
	        gzis=null;
	        inStream=null;
	        String result=outStream.toString("UTF-8");
	    	outStream.close();
	    	outStream=null;
	    
	    	return result;
	 
	    }catch(IOException ex){
	       return ex.toString(); 
	    }
	   } 


}
