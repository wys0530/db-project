package dbdbd.service;

import java.sql.*;
import java.util.Scanner;



public class GameRevenueStats {

	public static void run(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		Scanner scanner = new Scanner(System.in);
		
		// 사용자에게 user_id 입력 받기
		System.out.print("\n금액을 조회할 사용자 ID를 입력하세요: ");
		int userId = scanner.nextInt();
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

		String sql = """
				    SELECT g.title, 
				           p.item_type, 
				           p.purchase_price,
				           SUM(p.purchase_price) 
				           OVER (PARTITION BY p.game_id ORDER BY p.purchase_price DESC) AS cumulative_revenue
				    FROM purchase p
				    JOIN game g ON p.game_id = g.game_id
				    WHERE p.user_id = ?;
				""";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			System.out.println("\n[게임별 누적 구매액]");
			System.out.println("----------------------------------------------------");

			boolean hasResult = false;
			while (rs.next()) {
				String title = rs.getString("title");
				String itemType = rs.getString("item_type");
				int price = rs.getInt("purchase_price");
				int cumulative = rs.getInt("cumulative_revenue");
				System.out.printf(" - %s | %s | %d원 | 누적: %d원\n", title, itemType, price, cumulative);
				hasResult = true;
			}

			if (!hasResult) {
				System.out.println("해당 유저의 구매 내역이 없습니다.");
			}

		} catch (SQLException e) {
			System.err.println("누적 수익 조회 중 오류 발생:");
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
