package com.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.beans.School;
import com.model.Account;

@WebServlet("/GetSchoolInformation")
public class GetSchoolInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String jsonString = "";

		if (br != null) {
			jsonString = br.readLine();
		}

		System.out.println("Received JSON " + jsonString);
		JSONParser parser = new JSONParser();
		String action = "";
		try {
			JSONObject jsonobj = (JSONObject) parser.parse(jsonString);
			JSONObject obj = new JSONObject();
			action = (String) jsonobj.get("action");
			if (action.equals("GetSchoolInformation")) {
				HttpSession session = request.getSession(false);

				if (session == null) {
					obj.put("msg", "unauthorised");
				} else {
					obj.put("msg", "authorised");
					String name = (String) session.getAttribute("username");
					obj.put("pname", name);
					System.out.println(session.getAttribute("username"));
				}
			}
			Account account = new Account();
			String schoolName = ( String ) jsonobj.get("schoolName");
			School school = account.getSchoolInfo(schoolName);
			obj.put("schoolName", school.getSchool_Name());
			obj.put("timings", school.getTimings());
			obj.put("address", school.getAddress());
			obj.put("image", school.getImages());
			System.out.println(obj.toJSONString());
			StringWriter out = new StringWriter();
			obj.writeJSONString(out);
			String jsonText = out.toString();
			System.out.print(jsonText);
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setContentType("application/json");
			OutputStreamWriter writer = new OutputStreamWriter(response.getOutputStream());
			// writer.write(URLDecoder.decode(new String(decryptedData),
			// "UTF-8"));
			writer.write(jsonText);
			writer.flush();
			writer.close();
			System.out.println("Done Sending Data");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
