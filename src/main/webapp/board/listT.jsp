<%@page import="utils.BoardPage"%>
<%@page import="hm.board.BoardDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="hm.board.BoardDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="./commons/header.jsp" %>
<%
BoardDAO dao = new BoardDAO(application);

Map<String, Object> param = new HashMap<String, Object>();
String searchField = request.getParameter("searchField");
String searchWord = request.getParameter("searchWord");
if (searchWord != null){
	param.put("searchField", searchField);
	param.put("searchWord", searchWord);
}

int totalCount = dao.selectCount(param);

//페이지 처리를 위한 코드 추가 부분....
int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));
int totalPage = (int)Math.ceil((double)totalCount / pageSize);

int pageNum = 1;
String pageTemp = request.getParameter("pageNum");
if (pageTemp != null && !pageTemp.equals(""))
	pageNum = Integer.parseInt(pageTemp);

int start = (pageNum -1) * pageSize + 1;
int end = pageNum * pageSize;
param.put("start", start);
param.put("end", end);


//출력할 레코드 추출
List<BoardDTO> boardLists = dao.selectListPage(param);
dao.close();
%>
<body> 
<div class="container">
    <!-- Top영역 -->
    <%@ include file="./commons/top.jsp" %>
    <!-- Body영역 -->
    <div class="row">
        <!-- Left메뉴영역 -->
        <%@ include file="./commons/left.jsp" %>
        <!-- Contents영역 -->
        <div class="col-9 pt-3">
            <h3>게시판 목록 - <small>자유게시판</small></h3>
            <!-- 검색 -->
            <div class="row">
                <form action="">
                    <div class="input-group ms-auto" style="width: 400px;">
                        <select name="searchField" class="form-control">
                            <option value="title">제목</option>
                            <option value="content">내용</option>
                            <option value="id">작성자</option>
                        </select>
                        <input type="text" name="searchWord" class="form-control" placeholder="Search" style="width: 200px;">
                        <button class="btn btn-success" type="submit">
                            <i class="bi-search" style="font-size: 1rem; color: white;"></i>
                        </button>
                    </div>
                </form>
            </div>
            <!-- 게시판 리스트 -->
            <div class="row mt-3 mx-1">
                <table class="table table-bordered table-hover table-striped">
                <thead>
                    <tr class="text-center">
                        <th>번호</th>
                        <th>제목</th>
                        <th>작성자</th>
                        <th>작성일</th>
                        <th>조회수</th>
                        <th>첨부</th>
                    </tr>
                </thead>
                <tbody>
					<%
        if (boardLists.isEmpty()){
        %>
        	<tr>
        		<td colspan="5" align="center">
        			등록된 게시물이 없습니다
        		</td>
        	</tr>
        <%
        }
        else {
        	int virtualNum = 0;
        	int countNum = 0;
        	for (BoardDTO dto : boardLists)
        	{
        		virtualNum = totalCount - (((pageNum -1) * pageSize) + countNum++);
        %>
        		<tr align="center">
        			<td><%= virtualNum %></td>
        			<td align="left">
        				<a href="viewT.jsp?num=<%= dto.getNum() %>"><%= dto.getTitle() %></a>
        			</td>
        			<td align="center"><%= dto.getId() %></td>
        			<td align="center"><%= dto.getVisitcount() %></td>
        			<td align="center"><%= dto.getPostdate() %></td>
        			<td><i class="bi-pin-angle-fill" style="font-size: 1rem;"></i></td>
        		</tr>
        <%
        	}
        }
        %>
                                   
                    
                </tbody>
                </table>
            </div>
            <!-- 각종버튼 -->
            <div class="row">
                <div class="col d-flex justify-content-end">
                    <button type="button" class="btn btn-primary" onclick="location.href='writeT.jsp';">글쓰기</button>
                    <button type="button" class="btn btn-secondary">수정하기</button>
                    <button type="button" class="btn btn-success">삭제하기</button>
                    <button type="button" class="btn btn-info">답글쓰기</button>
                    <button type="button" class="btn btn-warning">목록보기</button>
                    <button type="button" class="btn btn-danger">전송하기</button>
                    <button type="button" class="btn btn-dark">다시쓰기</button>
                </div>
            </div>
            <!-- 페이지 번호 -->
            <div class="row mt-3">
                <div class="col">
                    	<%= BoardPage.pagingStr(totalCount, pageSize,
	        			blockPage, pageNum, request.getRequestURI()) %>
                </div>
            </div>
        </div>
    </div>
    <!-- Copyright영역 -->
    <%@ include file="./commons/copyright.jsp" %>
</div>
</body>
</html>