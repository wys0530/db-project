package dbdbd.service;

import java.sql.*;
import java.util.Scanner;

public class ReviewDelete {
	
	public void run(Connection conn) {
		Scanner scanner = new Scanner(System.in);
	    
		String username="";

	    int userId=0;
	    String title="";
	    String sql_delete= "";
		
	    PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 1. 사용자 ID 입력
			System.out.print("리뷰를 삭제할 사용자 ID를 입력하세요: ");
            userId = scanner.nextInt();
            scanner.nextLine(); // 개행 문자 제거
            
            // 2. 삭제할 게임명 입력
            System.out.print("삭제할 리뷰의 게임 제목을 입력하세요: ");
            title = scanner.nextLine();
            
            // 3. DELETE 쿼리 준비
            sql_delete = """
                    delete review
                    from review
                    join game on review.game_id = game.game_id
                    WHERE review.user_id = ? AND game.title = ?;
                    """;
            
            pstmt = conn.prepareStatement(sql_delete);
            pstmt.setInt(1, userId);
            pstmt.setString(2, title);
            
            // 4. 실행 및 결과 처리
            int result = pstmt.executeUpdate();
            if (result > 0) {
                System.out.println(result + "개의 리뷰가 삭제되었습니다.");
            } else {
                System.out.println("삭제할 리뷰가 없습니다.");
            }
			
		} catch (SQLException e) {
            System.err.println("리뷰 삭제 중 오류 발생:");
            e.printStackTrace();
        } finally {
            // 자원 해제
            try {
            	if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if(conn != null) {
                	conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}
	
}