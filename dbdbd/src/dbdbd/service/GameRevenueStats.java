package dbdbd.service;

import java.sql.*;

public class UserTotalPurchase {

    public static void run() {
        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/dbdbd?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "*****"; // ← 본인 비밀번호로 수정

        Connection conn = null;

        String sql = """
            SELECT u.username, 
                   SUM(p.purchase_price) AS total_spent
            FROM purchase p
            JOIN users u ON p.user_id = u.user_id
            GROUP BY u.username
            HAVING SUM(p.purchase_price) >= 100000;
        """;

        try {
            conn = DriverManager.getConnection(url, user, password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("[10만 원 이상 구매한 유저 목록]");
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

            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            System.err.println("조회 중 오류 발생:");
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                    System.out.println("... Close Connection ...");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
