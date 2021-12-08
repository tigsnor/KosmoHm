<%@page import="hm.board.BoardDAO"%>
<%@page import="hm.board.BoardDTO"%>
<%@page import="utils.JSFunction"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%
 String title = request.getParameter("title");
 String content = request.getParameter("content");
 
 BoardDTO dto = new BoardDTO();
 dto.setTitle(title);
 dto.setContent(content);
 dto.setId(session.getAttribute("UserId").toString());
 
 BoardDAO dao = new BoardDAO(application);
 /* 기존에 1개씩 입력하는 방 */
 int iResult = dao.insertWrite(dto);
 
 /* 더미데이터 100개를 한꺼번에 입력하 */
/*  int iResult = 0;
 for(int i=1 ; i<=100 ; i++){
	 dto.setTitle(title +"-"+ i);
	 iResult = dao.insertWrite(dto);
 } */
 
 dao.close();
 
 if (iResult == 1){
	 response.sendRedirect("listT.jsp");
 }
 else{
	 JSFunction.alertBack("글쓰기에 실패하였습니다.", out);
 }
 %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

</body>
</html>