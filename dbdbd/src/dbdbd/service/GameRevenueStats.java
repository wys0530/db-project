package dbdbd.service;

import java.sql.*;

public class GameRevenueStats {

    public static void run(Connection conn) {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = """
            SELECT g.title, 
                   p.item_type, 
                   p.purchase_price,
                   SUM(p.purchase_price) 
                   OVER (PARTITION BY p.game_id ORDER BY p.purchase_price DESC) AS cumulative_revenue
            FROM purchase p
            JOIN game g ON p.game_id = g.game_id
            WHERE p.user_id = 1;
        """;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            System.out.println("\n[게임별 누적 판매 수익 (user_id = 1 기준)]");
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
            try {
                if (rs != null) {
                    rs.close();
                    System.out.println("... Close ResultSet ...");
                }
                if (pstmt != null) {
                    pstmt.close();
                    System.out.println("... Close PreparedStatement ...");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
