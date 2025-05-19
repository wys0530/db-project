package dbdbd;

import dbdbd.DBConnection;
import java.sql.Connection;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// 연결 테스트
		try (Connection conn = DBConnection.getConnection()) {
            System.out.println(">>> DB 연결 성공!");


        } catch (Exception e) {
            System.err.println(">>> DB 연결 실패!");
            e.printStackTrace();
        }

	}

}
