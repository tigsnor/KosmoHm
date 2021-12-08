package hm.board;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import common.JDBConnect;

public class BoardDAO extends JDBConnect{
	public BoardDAO(ServletContext application) {
		super(application);
	}
	
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		
		String query = "SELECT COUNT(*) FROM board";
		if (map.get("searchWord")!=null){
				query += " WHERE " + map.get("searchField") + " "
						+ " LIKE '%" + map.get("searchWord") + "%'";
		}
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			totalCount = rs.getInt(1);
		}
		catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}
		
		return totalCount;
	}
	
	public List<BoardDTO> selectList(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>();
		
		String query = "SELECT * FROM board ";
		if (map.get("searchWord")!=null){
			query += " WHERE " + map.get("searchField") + " "
					+ " LIKE '%" + map.get("searchWord") + "%' ";
		}
		query += " ORDER BY num DESC ";
		
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			while (rs.next()) {
				
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				bbs.add(dto);
			}
		}
		catch (Exception e) {
			System.out.println("게시물 조회  예외 발생");
		e.printStackTrace();
		}	
		return bbs;	
	}
	
	//게시판의 페이징 처리를 위한 메서드
	public List<BoardDTO> selectListPage(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>();
		
		
		//3개의서브 쿼리문을 통한
		String query = "SELECT * FROM ("
				+ " SELECT Tb.*, ROWNUM rNum FROM ( "
				+ " SELECT * FROM board ";
		
		// 검색 조건 추가 (검색어가 있는 경에만 where절이 추가됨
		if (map.get("searchWord")!=null){
			query += " WHERE " + map.get("searchField")
					+ " LIKE '%" + map.get("searchWord") + "%' ";
		}
		query += " ORDER BY num DESC "
				+ " ) Tb "
				+ " ) "
				+ " WHERE rNum BETWEEN ? AND ? ";
		//JSP에서 계산된 게시물의 구간을 인라미터로 처리함
		
		try {
			// 쿼리 실행을 위한 prepared객체 생성
			psmt = con.prepareStatement(query);
			//인파라미터설정 : 구간을 위한 start, end를 설정함
			psmt.setString(1, map.get("start").toString());
			psmt.setString(2, map.get("end").toString());
			//쿼리문 실행
			rs = psmt.executeQuery();
			//select한 게시물의 갯수만큼 반복함
			while (rs.next()) {
				// 한 행(게시물 하나)의 데이터를 DTO에 저장
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				bbs.add(dto);
			}
		}
		catch (Exception e) {
			System.out.println("게시물 조회  예외 발생");
		e.printStackTrace();
		}	
		return bbs;	
	}
	
	public int insertWrite(BoardDTO dto) {
		int result = 0;
		
		try {
			String query = "INSERT INTO board ( "
					+ " num,title,content,id,visitcount) "
					+ " VALUES ("
					+ " seq_board_num.NEXTVAL, ?, ?, ?, 0)";
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());
			
			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 입력 중 예외 발생");
			e.printStackTrace();
		}
		
		return result;
	}
	
	//상세보기 
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO();
		
		String query = "SELECT B.*, M.name "
				+ " FROM member M INNER JOIN board B "
				+ " ON M.id=B.id "
				+ " WHERE num=?";
		
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();
			
			if (rs.next()) {
				dto.setNum(rs.getString(1));
				dto.setTitle(rs.getString(2));
				dto.setContent(rs.getString("content"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setId(rs.getString("id"));
				dto.setVisitcount(rs.getString(6));
				dto.setName(rs.getString("name"));
			}
		}
		catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		
		return dto;
	}
	//조회
	public void updateVisitCount(String num) {
		String query = "UPDATE board SET "
				+ " visitcount=visitcount+1 "
				+ " WHERE num=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeQuery();
		}
		catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	//게물 수정 :수정할 내용을 DTO객체에 저장후 매개변수로 저장
	public int updateEdit(BoardDTO dto) {
		int result = 0;
		
		try {
			//update를 위한 커리
			String query = "UPDATE board SET "
					+ " title=?, content=? "
					+ " WHERE num=?";
			//prepare객체 생성
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getNum());
			
			result = psmt.executeUpdate();	
		}
		catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생 ");
		e.printStackTrace();
		}
		return result;
	}
	
	//DTO객체를 받아 게시물 삭제
	public int deletePost(BoardDTO dto) {
		int result = 0;
		
		try {
			//쿼리문 작성
			String query = "DELETE FROM board WHERE num=? ";
			//prepared객체 생성 및 인파라미터 설정
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getNum());
			//쿼리실행
			result = psmt.executeUpdate();
		}
		catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
}
