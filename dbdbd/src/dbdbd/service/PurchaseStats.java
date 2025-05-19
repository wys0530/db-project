package dbdbd.service;

import java.sql.*;
import java.util.Scanner;

public class PurchaseStats {
	
	public void run(Connection conn) {
		
	    Statement stmt = null;
		ResultSet rs = null;
		
		String sql = """
	            select title, game_price
	            from game
	            where game_price < (
	                select avg(game_price)
	                from game
	                where release_date >= '2020-01-01'
	            );
	        """;
		
		try {
			stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
	
			System.out.println("[2020년 이후 평균보다 저렴한 게임 목록]");
	        System.out.println("--------------------------------------");
				
	        boolean hasResult = false;
            while (rs.next()) {
                String title = rs.getString("title");
                int price = rs.getInt("game_price");
                System.out.println(" - " + title + " : " + price + "원");
                hasResult = true;
            }
            
            if (!hasResult) {
                System.out.println("해당 조건에 맞는 게임이 없습니다.");
            }
            
		}catch (SQLException e) {
            System.err.println("게임 가격 통계 조회 중 오류 발생:");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
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


