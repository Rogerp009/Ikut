	package com.ikut.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.json.JSONObject;

import android.util.Log;

import com.ikut.utils.Constant;
import com.ikut.utils.Functions;

public class JSONParser {

	private InputStream is = null;
	private JSONObject jObj = null;
	
	/** var http */
	private DefaultHttpClient httpClient = null;
	private HttpResponse httpResponse = null;
	private HttpParams httpParameters = null;
	private StatusLine statusLine = null;
	
	// constructor
	public JSONParser() {
		
		httpParameters = new BasicHttpParams();

		
	}//end constructor

	public JSONObject getJSONFromUrl(String url, List<NameValuePair> params) {
		
		String json = "";
		
		try {
			
			httpClient = new DefaultHttpClient(httpParameters); // The default value is zero, that means the timeout is not used.
			
			HttpConnectionParams.setConnectionTimeout(httpParameters, Constant.TIME_OUT_CONNECTION);
			HttpConnectionParams.setSoTimeout(httpParameters, Constant.TIME_OUT_SOCKET);
			ConnManagerParams.setTimeout(httpParameters, Constant.TIME_OUT_CONNECTION);
			
			HttpProtocolParams.setUseExpectContinue(httpClient.getParams(), false);
			
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			
			httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			
			statusLine = httpResponse.getStatusLine();
			
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				is = httpEntity.getContent();
				json = Functions.convertString(is);
				
				if(json ==  null || json.toString().equals("") || json.length() == 0){
					jObj = new JSONObject(json);
					if(jObj == null || json.length() == 0 || jObj.toString().equals("0")){
						jObj.put(Constant.KEY_ERROR, "-1");
					}					
				}else{
					jObj = new JSONObject(json);//convertir a json
				}
			}else{
		        //Closes the connection.
                Log.w("HTTP1:", statusLine.getReasonPhrase());
                httpResponse.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());				
			}
		 }catch(ConnectTimeoutException e){
				Log.w("ConnectTimeoutException ", e);
		 }catch (UnknownHostException e) {
			 	Log.w("UnknownHostException ", e);
		 }catch (UnsupportedEncodingException e) {
			 	Log.w("UnsupportedEncodingException ", e);
		 }catch(SocketTimeoutException e){
			 	Log.w("SocketTimeoutException ", e);
		 }catch (ClientProtocolException e) {
	            Log.w("HTTP2:", e );
	     } catch (IOException e) {
	            Log.w("HTTP3:", e );
	     }catch (Exception e) {
	            Log.w("HTTP4:", e );
	     }
		return jObj;
	
	}//end method
	
	

}//end class
