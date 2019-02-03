<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<title>Jar's Classes Locator</title>
<style>
input[type='button'],input[type='submit']{
    border:0px;
    height:25px;
    width:80px;
    color:white;
}
#add{
background-color:darkgreen;
border-top-left-radius:10px;
border-bottom-left-radius:10px;
}
#analyze{
background-color:blue;
border-top-right-radius:10px;
border-bottom-right-radius:10px;
}
table{
margin-top:30px;
border-collapse:collapse;
}
tr{
border:1px solid black;
border-color:orange;
}
td{
padding:10px;
}
p{
margin:0px;
font-size:14px;
}
</style>
</head>
<body>
<form id="mainForm" enctype="multipart/form-data" method="POST" action="listFiles">
<center><table>
<tr>
<td>Folder to look: </td><td><input type="file" name="files"></td><td><input type="submit" name="actionBtn" id="add" value="Add"><input type="submit" name="actionBtn" id="analyze" value="Analyze"></td>
</tr>
<% String fNamesStr = (String)(request.getAttribute("fNames")==null?"":request.getAttribute("fNames"));
   String[] fNamesSplit = fNamesStr.split(";");
   int count = 0;
%>
<% for(int i=0;i<fNamesSplit.length;i++){ 
       if(!fNamesSplit[i].equals("")) {
    	   count++;
%>
<tr><td colspan="3"><p><%= (count + "). " + fNamesSplit[i]) %></p></td></tr>
<%     }
   } %>
</table></center>
<input type="text" id="fNames" name="fNames" value="<%= (request.getAttribute("fNames")==null?"":request.getAttribute("fNames")) %>" hidden>
<input type="text" id="session-token" name="session-token"  hidden>
</form>
<script>
function addJar() {
	var mainForm = document.querySelector("#mainForm");
	mainForm.submit();
}
function analyze() {
	var mainForm = document.querySelector("#mainForm");
	mainForm.submit();
}
<% if(request.getAttribute("session-token") == null) { %>
	   document.querySelector("#session-token").value=parseInt(Math.random()*1000);
<% } else { %>
       document.querySelector("#session-token").value="<%= request.getAttribute("session-token") %>";
<% } %>
</script>
</body>
</html>