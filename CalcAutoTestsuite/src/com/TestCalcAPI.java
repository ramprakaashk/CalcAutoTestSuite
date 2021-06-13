package com;

import java.util.List;
import java.io.*;    
import java.net.*; 
import java.util.HashMap;
import java.util.Map;

public class TestCalcAPI {

	private static final String USER_AGENT = "Mozilla/5.0";
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{    
			URL url=new URL("http://localhost:8080/calc/add");    
			HttpURLConnection con =(HttpURLConnection)url.openConnection();  
			con.setRequestMethod("GET");
		//	con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("User-Agent", USER_AGENT);
			String contentType = con.getHeaderField("Content-Type");
			// Adding params
			int status = con.getResponseCode();
			System.out.println("Response code is "+con);
			
			Map<String, String> parameters = new HashMap<>();
			parameters.put("param1", "40");
			parameters.put("param2", "90");
			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			
			BufferedReader in = new BufferedReader(
					  new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
					in.close();
			out.flush();
			out.close(); 
			con.disconnect();   
			}
			catch(Exception e){
				e.getStackTrace();
				System.out.println("We got an excption"+e.getMessage());
				e.getStackTrace();
				}    
			}	
		
		
		
		
	}

