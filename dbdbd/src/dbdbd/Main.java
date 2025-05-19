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
