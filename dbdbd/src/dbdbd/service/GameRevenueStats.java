package dbdbd.service;

import java.sql.*;
import java.util.Scanner;

public class GameRevenueStats {

    public static void run() {
        // DB 연결 정보
        String url = "jdbc:mysql://localhost:3306/dbdbd?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "*****"; // ← 본인의 DB 비밀번호로 바꿔주세요

        Connection conn = null;

        Scanner scanner = new Scanner(System.in);

        try {
            conn = DriverManager.getConnection(url, user, password);

            System.out.print("유저 ID를 입력하세요: ");
            int userId = scanner.nextInt();

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

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("\n[게임별 누적 판매 수익 (해당 유저의 구매 기준)]");
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

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("누적 수익 조회 중 오류 발생:");
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
            scanner.close();
        }
    }
}
