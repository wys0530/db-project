package dbdbd.service;

import java.sql.*;

public class UserTotalPurchase {

	public static void run(Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		String sql = """
				    SELECT u.username, 
				    		SUM(p.purchase_price) AS total_spent
				    FROM purchase p
				    JOIN users u ON p.user_id = u.user_id
				    GROUP BY u.username
				    HAVING SUM(p.purchase_price) >= 100000;
				""";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			System.out.println("\n[10만 원 이상 구매한 유저 목록]");
			System.out.println("-------------------------------");

			boolean hasResult = false;
			while (rs.next()) {
				String username = rs.getString("username");
				int total = rs.getInt("total_spent");
				System.out.println(" - " + username + " : " + total + "원");
				hasResult = true;
			}

			if (!hasResult) {
				System.out.println("조건에 맞는 유저가 없습니다.");
			}

		} catch (SQLException e) {
			System.err.println("총 구매 금액 조회 중 오류 발생:");
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
