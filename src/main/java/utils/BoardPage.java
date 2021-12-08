package utils;

public class BoardPage {
	//매개변수(전체게시물갯수,한페이지출력갯수, 한블럭당출력페이지수, 현재페이지번호, 현제페이지명)
	public static String pagingStr(int totalCount, int pageSize, int blockPage,
			int pageNum, String reqUrl) {
		String pagingStr = "";
		//전체페이지수 계수
		int totalPages = (int) (Math.ceil(((double) totalCount / pageSize)));
		
		//페이지 블럭의 첫번째 수를 계산
		int pageTemp = (((pageNum - 1) / blockPage) * blockPage)+1;
		
		//이전블럭으로 바로가기 링크(첫번째 블럭에서는 숨김처리)
		pagingStr += "<ul class='pagination justify-content-center'>";
		if (pageTemp == 1) {
			pagingStr += "<li class='page-item disabled'><a href='" + reqUrl + "?pageNum=1'"
					+ "class='page-link'>"
					+ "<i class='bi bi-skip-backward-fill'></i>"
					+ "</a></li>";
			pagingStr += "<li class='page-item disabled'><a href='" + reqUrl + "?pageNum=" + (pageTemp - 1)
					+ "' class='page-link' href='#'>"
					+ "<i class='bi bi-skip-start-fill'></i>"
					+ "</a></li>";
		}
		if (pageTemp != 1) {
			pagingStr += "<li class='page-item'><a href='" + reqUrl + "?pageNum=1'"
					+ "class='page-link'>"
					+ "<i class='bi bi-skip-backward-fill'></i>"
					+ "</a></li>";
			pagingStr += "<li class='page-item'><a href='" + reqUrl + "?pageNum=" + (pageTemp - 1)
					+ "' class='page-link' href='#'>"
					+ "<i class='bi bi-skip-start-fill'></i>"
					+ "</a></li>";
		}
		//각 페이지로 바로가기 링크blockPage 수 만큼출력됨)
		int blockCount = 1;
		while (blockCount <= blockPage && pageTemp <= totalPages) {
			if(pageTemp == pageNum) {
				pagingStr += "<li class='page-item active'><a class='page-link'>" + pageTemp + "</a></li>";
			}
			else {
				pagingStr += "<li class='page-item'><a href='" + reqUrl + "?pageNum=" + pageTemp
						+ "'class='page-link'>" + pageTemp + "</a></li>";
			}
			pageTemp++;
			blockCount++;
		}
		//다음블럭으로 바로가기 링크 
		if (pageTemp <= totalPages) {
			pagingStr += "<li class='page-item'><a href='" + reqUrl + "?pageNum=" + pageTemp
					+ "' class='page-link' href='#'>"
					+ "<i class='bi bi-skip-end-fill'></i>"
					+ "</a></li>";
			
			pagingStr += "<li class='page-item'><a href='" + reqUrl + "?pageNum=" + totalPages
					+ "' class='page-link' href='#'>"
					+ "<i class='bi bi-skip-forward-fill'></i>"
					+ "</a></li>";
		}
		
		if (pageTemp > totalPages) {
			pagingStr += "<li class='page-item disabled'><a href='" + reqUrl + "?pageNum=" + pageTemp
					+ "' class='page-link' href='#'>"
					+ "<i class='bi bi-skip-end-fill'></i>"
					+ "</a></li>";
			
			pagingStr += "<li class='page-item disabled'><a href='" + reqUrl + "?pageNum=" + totalPages
					+ "' class='page-link' href='#'>"
					+ "<i class='bi bi-skip-forward-fill'></i>"
					+ "</a></li>";
		}
		
		pagingStr += "</ul>";
		
		return pagingStr;
	}
}
