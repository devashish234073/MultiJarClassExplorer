package com;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/**
 * Servlet implementation class ListFiles
 */
@WebServlet("/ListFiles")
@MultipartConfig
public class ListFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListFiles() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    private static HashMap<String,UserSession> userSessions = new HashMap<String,UserSession>();  

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("actionBtn").equals("Add")) {
			String description = request.getParameter("description"); // Retrieves <input type="text" name="description">
		    Part filePart = request.getPart("files"); // Retrieves <input type="file" name="file">
		    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
		    InputStream inputStream = filePart.getInputStream();
		    
		    String fileList = appendFileToList(fileName, request, response);
		    
		    String sessionToken = getSessionToken(request,response);
		    request.setAttribute("session-token", sessionToken);
		    
		    if(fileList.indexOf("changed:")!=-1) {
		    	HashMap<String,InputStream> isMap;
		    	UserSession userSession = userSessions.get(sessionToken);
		    	if(userSession==null) {
		    		userSession = new UserSession(sessionToken);
		    		userSessions.put(sessionToken, userSession);
		    	} 
		    	userSession.addStream(fileName, inputStream);
		    	fileList = fileList.replace("changed:", "");
		    }
		    
		    request.setAttribute("fNames", fileList);
		    
		    RequestDispatcher rd = request.getRequestDispatcher("./home.jsp");
		    rd.forward(request, response);
		} else if(request.getParameter("actionBtn").equals("Analyze")) {
			String sessionToken = getSessionToken(request,response);
			UserSession userSession = userSessions.get(sessionToken);
			if(userSession.getListSize() == 0) {
				PrintWriter pw = response.getWriter();
				pw.write("<font color=\"red\">No files added!</font>");
			} else {
				PrintWriter pw = response.getWriter();
				Iterator<String> itr = userSession.getFileNameIterator();
				while(itr.hasNext()) {
					String fName = itr.next();
					InputStream is = userSession.getInputStream(fName);
					pw.write("---------------------------------\r\n");
					for(String f : JarClassLocator.getAllFileInsideStream(is)) {
						pw.write(fName+"~"+f+"\r\n");	
					}
					is.close();
					pw.write("-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-x-\r\n");
				}
			}
		} else {
			PrintWriter pw = response.getWriter();
			pw.write("<font color=\"red\">Invalid Action</font>");
		}
		
	}
	
	private String appendFileToList(String fileName, HttpServletRequest request, HttpServletResponse response) {
		String existing = request.getParameter("fNames");
		if(existing.equals("")) {
			return "changed:"+fileName;
		} else {
		    String[] existingSplit = existing.split(";");
		    for(int i=0;i<existingSplit.length;i++) {
		    	if(existingSplit[i].equals(fileName)) {
		    		return existing;
		    	}
		    }
		    return "changed:"+existing+";"+fileName;
		}
	}
	
	private String getSessionToken(HttpServletRequest request, HttpServletResponse response) {
		String sessionToken = request.getParameter("session-token");
		String[] sessionTokenSplit = sessionToken.split("_");
		if(sessionTokenSplit.length == 2) {
		    return sessionToken;
		} else {
			return sessionToken + "_" + System.currentTimeMillis();
		}
	}

}
