package dbdbd.service;

import java.sql.*;
import java.util.Scanner;

public class ReviewDelete {

	public static void run(Connection conn) {
		Scanner scanner = new Scanner(System.in);

		int userId=0;
		String title="";
		String sql_select="";
		String sql_delete="";

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 1. 사용자 ID 입력
			System.out.print("\n리뷰를 삭제할 사용자 ID를 입력하세요: ");
			userId = scanner.nextInt();
			scanner.nextLine(); 
			
			String userNameSql = "SELECT username FROM users WHERE user_id = ?";

	        try {
	            pstmt = conn.prepareStatement(userNameSql);
	            pstmt.setInt(1, userId);
	            rs = pstmt.executeQuery();

	            if (rs.next()) {
	                String username = rs.getString("username");
	                System.out.println("\n안녕하세요, '" + username + "'님");
	            } 
	            else {
	                System.out.println("해당 ID의 사용자가 존재하지 않습니다.");
	                return; // 중단
	            }

	            rs.close();
	            pstmt.close();
	        } 
	        catch (SQLException e) {
	            System.err.println("사용자 이름 조회 중 오류 발생:");
	            e.printStackTrace();
	            return;
	        }

			// 2. 해당 사용자의 리뷰 목록 출력
            sql_select = """
            	    select r.review_id, g.title
            	    from review r
            	    inner join game g on r.game_id = g.game_id
            	    where r.review_id is not null and r.user_id = ?
            	""";

            
            pstmt = conn.prepareStatement(sql_select);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

			System.out.println("\n해당 사용자가 작성한 리뷰 목록:");
            System.out.println("=====================================");
            boolean hasReview = false;
            while (rs.next()) {
                int reviewId = rs.getInt("review_id");
                String gameTitle = rs.getString("title");

                System.out.println("리뷰 ID: " + reviewId);
                System.out.println("게임명: " + gameTitle);
                System.out.println("=====================================");
                hasReview = true;
            }
            rs.close(); 
            pstmt.close();
            
            if (!hasReview) {
                System.out.println("해당 사용자가 작성한 리뷰가 없습니다.");
                return;
            }
            
            // 3. 삭제할 게임명 입력
         	System.out.print("삭제할 리뷰의 게임 제목을 입력하세요: ");
         	title = scanner.nextLine();
            
			// 4. DELETE 쿼리 준비
			sql_delete = """
					delete review
					from review
					join game on review.game_id = game.game_id
					WHERE review.user_id = ? AND game.title = ?;
					""";

			pstmt = conn.prepareStatement(sql_delete);
			pstmt.setInt(1, userId);
			pstmt.setString(2, title);

			// 5. 실행 및 결과 처리
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
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}