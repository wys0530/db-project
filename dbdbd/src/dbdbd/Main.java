package dbdbd;

import dbdbd.DBConnection;
import dbdbd.service.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;

		try{
			conn = DBConnection.getConnection();
			System.out.println(">>> DB 연결 성공!");

			Scanner scanner = new Scanner(System.in);
			int choice;

			do {
				System.out.println("\n========== 메뉴 ==========");
				System.out.println("[1] 분석 보기");
				System.out.println("[2] 리뷰 관리 ");
				System.out.println("[3] 게임별 누적 구매액 조회");
				System.out.println("[4] 휴면 계정 게임 점수 초기화");
				System.out.println("[0] 종료");
				System.out.println("=========================");
				System.out.print("실행할 메뉴의 번호를 입력하세요: ");

				choice = scanner.nextInt();

				switch (choice) {
				case 1:
					System.out.println("\n---------- 분석 보기 ----------");
					System.out.println("[1] 평점별 구매 금액 조회"); 				//PivotAnalysis
					System.out.println("[2] 최근 평균 가격 대비 저렴한 게임 조회");	//PurchaseStats
					System.out.println("[3] 구매액 상위 유저 조회");				//UserTotalPurchase
					System.out.println("-----------------------------");
					System.out.print("실행할 메뉴의 번호를 입력하세요: ");

					choice = scanner.nextInt();

					switch (choice) {
					case 1:
						PivotAnalysis.run(conn);
						break;
					case 2:
						PurchaseStats.run(conn);
						break;
					case 3:
						UserTotalPurchase.run(conn);
						break;
					default:
						System.out.print("잘못된 입력입니다. 분석 기능 메뉴로 돌아갑니다.");
					}

					break;
				case 2:
					System.out.println("\n---- 리뷰 관리 ----");
					System.out.println("[1] 리뷰 등록");						//ReviewWrite       
					System.out.println("[2] 리뷰 삭제");						//ReviewDelete
					System.out.println("----------------");
					System.out.print("실행할 메뉴의 번호를 입력하세요: ");

					choice = scanner.nextInt();

					switch (choice) {
					case 1:
						ReviewWrite.run(conn);
						break;
					case 2:
						ReviewDelete.run(conn);
						break;
					default:
						System.out.println("잘못된 입력입니다. 리뷰 메뉴로 돌아갑니다.");
					}

					break;
				case 3:														//GameRevenusStats
					System.out.println("게임의 품목별 누적 금액을 확인해보세요.\n ");
					GameRevenueStats.run(conn);
					break;
				case 4:														//UserScoreReset
					System.out.println("마지막 플레이가 오래되었거나, 점수가 낮아 휴면 계정으로 의심되는 게임입니다.");
					System.out.println("게임의 점수를 초기화 할 수 있습니다.\n");
					UserScoreReset.run(conn);
					break;
				case 0:
					System.out.println("\n애플리케이션을 종료합니다.");
					break;
				default:
					System.out.println("~ 잘못된 입력입니다. 다시 입력해주세요. ~");
				}
				
				System.out.println("\n_________________________________________________________________");
				
				try {
				    Thread.sleep(800); // 한 번 끝날 때마다 1초 대기
				} catch (InterruptedException e) {
				    Thread.currentThread().interrupt(); // 인터럽트 발생 시 복구
				}
				
			}while(choice != 0);

		} catch (Exception e) {
			System.err.println(">>> DB 연결 실패!");
			e.printStackTrace();
		}finally { 	// 자원 해제
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
