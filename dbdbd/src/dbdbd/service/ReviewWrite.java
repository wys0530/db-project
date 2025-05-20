package dbdbd.service;   
import java.util.Scanner;
import java.sql.*;


public class ReviewWrite {

	public static void run(Connection myConn) {
		String username="";

		int userId=0;
		int gameId=0;
		int audiovisual_rating=0;
		int immersion_rating=0;
		int story_rating=0;
		int total_rating=0;

		String sql_select = "";
		String sql_insert = "";
		String sql_finduser="";
		String sql_games="";

		PreparedStatement pstmt = null;
		ResultSet myResSet = null;

		Scanner scanner = new Scanner(System.in);   

		try {
			// 1. 사용자 이름 입력 → user_id 조회
			System.out.print("\n사용자 이름을 입력하세요: ");
			username = scanner.nextLine();

			sql_finduser = "SELECT user_id, username FROM users WHERE username = ?";
			pstmt = myConn.prepareStatement(sql_finduser);
			pstmt.setString(1, username);
			myResSet = pstmt.executeQuery();

			boolean hasResult= false;
			System.out.println("\n=====================================");
			while (myResSet.next()) {
				userId = myResSet.getInt("user_id");
				System.out.println("- " + username + " (user_id: " + userId + ")");
				hasResult = true;
			}
			System.out.println("=====================================");
			System.out.print("-> 다음 사용자 목록 중 본인의 user_id를 입력하세요: ");
			userId = scanner.nextInt();

			if(!hasResult){
				System.out.println("해당 사용자가 존재하지 않습니다.");
				return;
			}

			myResSet.close();
			pstmt.close();

			// 2. 리뷰 안 쓴 게임 목록 출력
			sql_games = """
					     select user_id, game_id, title
					from review_list
					where review_id is null and user_id=?;
					     """;

			pstmt = myConn.prepareStatement(sql_games);
			pstmt.setInt(1, userId);
			myResSet = pstmt.executeQuery();

			System.out.println("\n\n==== [아직 리뷰가 작성되지 않은 게임 목록] ====\n");
			hasResult = false;
			while (myResSet.next()) {
				int gid = myResSet.getInt("game_id");
				String title = myResSet.getString("title");
				System.out.println(" - " + title + " (game_id: " + gid + ")");
				hasResult = true;
			}

			if (!hasResult) {
				System.out.println("모든 게임에 대한 리뷰가 작성되었습니다.");
				return;
			}

			myResSet.close();
			pstmt.close();

			// 3. game_id 및 점수 입력
			System.out.println("\n=====================================");
			System.out.print("\n-> 리뷰를 작성할 game_id를 입력하세요: ");
			gameId = scanner.nextInt();

			System.out.print("시청각 점수 (0~5): ");
			audiovisual_rating = scanner.nextInt();

			System.out.print("몰입도 점수 (0~5): ");
			immersion_rating = scanner.nextInt();

			System.out.print("스토리 점수 (0~5): ");
			story_rating = scanner.nextInt();

			System.out.print("총 평점 (0~5): ");
			total_rating = scanner.nextInt();

			// 4. INSERT
			sql_insert = """
					   insert into review(
					   user_id, 
					   game_id, 
					   audiovisual_rating, 
					   immersion_rating, 
					   story_rating, 
					   total_rating) 
					VALUES (?, ?, ?, ?, ?, ?);
					   """;

			pstmt = myConn.prepareStatement(sql_insert);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, gameId);
			pstmt.setInt(3, audiovisual_rating);
			pstmt.setInt(4, immersion_rating);
			pstmt.setInt(5, story_rating);
			pstmt.setInt(6, total_rating);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println("\n리뷰가 성공적으로 저장되었습니다!\n");
			} else {
				System.out.println("\n리뷰 저장에 실패했습니다.\n");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if (myResSet != null) {
				try {
					myResSet.close();
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
