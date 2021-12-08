<%@page import="hm.board.BoardDAO"%>
<%@page import="hm.board.BoardDTO"%>
<%@page import="utils.JSFunction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
//수정페이지에서 전송한 폼값 받
String num = request.getParameter("num");



BoardDTO dto = new BoardDTO();
BoardDAO dao = new BoardDAO(application);
dto = dao.selectView(num);

String sessionId = session.getAttribute("UserId").toString();

int delResult = 0;

if (sessionId.equals(dto.getId())){
	dto.setNum(num);
	delResult = dao.deletePost(dto);
	dao.close();
	
	if (delResult == 1){
		//게시물삭제에성공하면 리스트로 이동한다.
		JSFunction.alertLocation("삭제되었습니다.", "listT.jsp", out);
	}
	else {
		JSFunction.alertBack("삭제에 실패하였습니다.",out);
	}
}
//작성자 본인이아니면 삭제할수 없음
else{
	JSFunction.alertBack("본인만 삭제할 수 있습니다.", out);
	return;
}

%>