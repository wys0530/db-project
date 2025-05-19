package dbdbd.service;

import java.sql.*;

import java.util.Scanner;

public class UserScoreReset {

	public void run(Connection conn) {
		
		Scanner sc = new Scanner(System.in);
		
		
		//Statement myState = null;
		ResultSet myResSet = null;
		PreparedStatement pstmt=null;
		
		String sql_update="";
		String sql_select="";
		
		try {
			
			//1. 사용자 로그인
			
			System.out.print("당신의 user_id를 입력하세요 :");
			int id=sc.nextInt();
			sc.nextLine();
			String sqlFindID="""
					select * 
					from users 
					where user_id=?
					""";
			pstmt=conn.prepareStatement(sqlFindID);
			pstmt.setInt(1, id);
			myResSet=pstmt.executeQuery();
			if(myResSet.next()) {
				System.out.println("안녕하세요"+id+"님 로그인되셨습니다.");
			}
			else {
				System.out.println("해당 user_id는 존재하지 않습니다.");
			}
			myResSet.close();
			pstmt.close();
		
			
			
			//2. 조건에 맞는 게임 목록 조회
			sql_select="""
					select ph.user_id,title,total_playtime,last_play,user_score
					from play_history ph
					left outer join review r on ph.user_id = r.user_id AND ph.game_id = r.game_id
					inner join game g on ph.game_id = g.game_id
					where ph.user_id=? and (ph.total_playtime<=1500 or ph.last_play<=date_sub(now(),interval 6 month))
			""";
			pstmt=conn.prepareStatement(sql_select);
			pstmt.setInt(1, id);
			myResSet=pstmt.executeQuery();
			while(myResSet.next()) {
				String title = myResSet.getString("title");
		                int playtime = myResSet.getInt("total_playtime");
		                Timestamp lastPlay = myResSet.getTimestamp("last_play");
		                int score = myResSet.getInt("user_score");
		
		                System.out.printf("제목: %s | 플레이타임: %d | 마지막 플레이: %s | 점수: %d%n",
		                        title, playtime, lastPlay, score);
			}
			
			//3. user_score를 초기화할 게임 선 >> title 기준
			System.out.print("user_score을 0으로 초기화할 게임 제목을 입력 :");
			String selectedT=sc.nextLine();
			
			//4. 해당 title을 기준으로 업데이트
			sql_update="""
					UPDATE play_history
					SET user_score = 0
			                    WHERE user_id = ?
			                    AND game_id = (
			                    SELECT game_id FROM game WHERE title = ?)
					
					""";
			PreparedStatement updatepstmt=conn.prepareStatement(sql_update);
			updatepstmt.setInt(1, id);
			updatepstmt.setString(2, selectedT);
			int a=updatepstmt.executeUpdate();
			
			if(a>0) {
				System.out.println("업데이트가 성공적으로 진행되었습니다.");
				
			}
			else {
				System.out.println("실패 ");
			}
			
		
			updatepstmt.close();
			
		}catch(SQLException e) {
			e.printStackTrace();	
		}finally {
            // 리소스 정리
            try {
                if (myResSet != null) myResSet.close();
                if (pstmt != null) pstmt.close();
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
		
		sc.close();

	}
	}
}
