package dbdbd;

import dbdbd.DBConnection;
import dbdbd.service.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;
		// 연결 테스트
		try{
			conn = DBConnection.getConnection();
            System.out.println(">>> DB 연결 성공!");
       
            
//			 테스트할 때 본인 것 주석 풀고 테스트
//			 * 본인 브랜치의 Main.java 푸시 하지 말 것!!!! * 
//           * 본인 브랜치에서 작성한 각 기능 java 파일만, 본인 브랜치에 푸시 * 
//				            
//            PivotAnalysis.run();
//            PriceCompare.run();
//            PurchaseStats.run();
//            RevenueOlap.run();
//            ReviewDelete.run();
//            ReviewWrite.run();
//            UserScoreReset.run();
//            UserTotalPurchase.run();			
//            GameRevenueStats.run();			
           

        } catch (Exception e) {
            System.err.println(">>> DB 연결 실패!");
            e.printStackTrace();
        }finally {
            // 자원 해제
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("... Close Connection"+ conn.toString()+" ...");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

	}

}
}
