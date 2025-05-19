package dbdbd.service;   
import java.util.Scanner;
import java.sql.*;

public class PivotAnalysis {

	public static void run(Connection conn) {
		
		Scanner sc = new Scanner(System.in);
		
	
		Statement myState = null;
		ResultSet myResSet = null;
		PreparedStatement pstmt=null;
		
		
		String sql_select="";
		try {
			//1. 전체 게임 제목 존재하
			sql_select="""
					select title
					from game
					""";
			myConn=DriverManager.getConnection(url,userID,userPW);
			myState =myConn.createStatement();
			myResSet=myState.executeQuery(sql_select);
			
			System.out.println("전체 게임 목록 : ");
			int k=1;
			while(myResSet.next()) {
				System.out.println(k+" : "+myResSet.getString("title"));
				k++;
			}
			
			myState.close();
			myResSet.close();
			
			//2. 사용자에게 title 입력받기
			System.out.print("조회할 게임 제목을 입력하세요 : ");
			String inputT=sc.nextLine();
			
			//3. 쿼리 날리기 
			String get_price="""
					select g.title, 
					sum(case when r.total_rating=5 then p.purchase_price else 0 end) as tot_5,
					sum(case when r.total_rating=4 then p.purchase_price else 0 end) as tot_4,
					sum(case when r.total_rating=3 then p.purchase_price else 0 end) as tot_3,
					sum(case when r.total_rating=2 then p.purchase_price else 0 end) as tot_2,
					sum(case when r.total_rating=1 then p.purchase_price else 0 end) as tot_1,
					sum(case when r.total_rating=0 then p.purchase_price else 0 end) as tot_0
					from game as g
					left join purchase as p on g.game_id=p.game_id
					left join review as r on r.user_id = p.user_id AND g.game_id=r.game_id
					where g.title=?
			""";
			pstmt=myConn.prepareStatement(get_price);
			pstmt.setString(1, inputT);
			myResSet=pstmt.executeQuery();
			if (myResSet.next()) {
                System.out.println("\n"+ inputT + "] 평점별 구매 금액 합계");
                for (int i = 5; i >= 0; i--) {
                    int sum = myResSet.getInt("tot_" + i);
                    System.out.printf("평점 %d점: 합계 %d원%n", i, sum);
                }
            } else {
                System.out.println("해당 게임에 대한 데이터가 존재하지 않습니다.");
            }
			
			
			
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
            // 리소스 정리
            try {
                if (myResSet != null) myResSet.close();
                if (pstmt != null) pstmt.close();
                
                if(myState !=null) myState.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
		
		sc.close();

	}
		
		


	}

}
