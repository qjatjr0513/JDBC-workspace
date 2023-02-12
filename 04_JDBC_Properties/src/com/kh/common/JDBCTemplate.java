package com.kh.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCTemplate {
	/*
	 * JDBCTemplate 생성이유
	 * 1. 공통적인 부분 추출
	 * 
	 * 만약에 JDBC로 접근하는 DBMS의 IP주소가 바뀌거나, 계정비밀번호가 바뀌는 경우?
	 * 모든코드 112 수정해줘야함.
	 * 
	 * 따라서 이런 불편함을 없애기 위해 JDBC과정중 반복적으로 쓰이는 구문들은 각각의 메소드로 정의해둘곳을 만듬.
	 * 
	 * "재사용할 목적"으로 공통 템플릿 작업 진행.
	 * 
	 * 이 클래스에서 모든 메소드들은 다 static 메소드로 만들것.
	 * 
	 * 싱글톤 패턴 : 객체를 최초 한번만 생성(메모리공간에 할당)해서 제공하는 클래스. 
	 * 
	 * 
	 */
	
	//공통적인 부분 뽑아내기
	// 1. DB와 접속된 Connection 객체를 생성해서 반환시켜주는 메소드
	
	// 외부 클래스에서 인스턴스 생성 못하도록 막기
	private JDBCTemplate() {
		
	}
	
	//Connection 객체를 담을 그릇 생성.
	private static Connection conn = null;
	// 3번 해결방법.
	//private static final Connection conn = DriverManager.getConnection("ip주소", '아이디', '비밀번호');
	
	// 동적코딩방식을 위해 사용할 Properties객체 생성.
	private static Properties prop = new Properties();
	
	public /*synchronized*/ static Connection getConnection() { // 1번 해결방법
		
		/*
		 * 기존의 방식 : JDBC Driver 구문, 내가 접속할 DB의 url정보, 접속할 계정명, 비밀번호들을 
		 * 			   자바소스코드 내에 명시적으로 작성함 => 정적 코딩 방식(하드코딩)
		 * 
		 * - 문제점 : DBMS가 변경이 되었을 경우 / 접속할 URL, 계정명, 비밀번호가 변경되었을 경우 자바소슼코드를 수정해야함.
		 * 			 수정된 내용을 반영시키고자한다면 프로그램 재구동 해야함.
		 * 			 (사용자 입장에서도 프로그램 사용 중 비정상적으로 종료되었다가 다시 구동될 수 있음)
		 * 			 * 유지보수에 불편하다.
		 * 
		 * - 해결방식 : DB관련된 정보들을 별도로 관리하는 외부파일(.properties)로 만들어서 관리
		 * 			  외부파일로 key에 대한 value를 읽어들여서 반영 => 동적코딩 방식
		 */
		
		try {
			prop.load(new FileInputStream("resources/driver.properties"));
			
			// 1) JDBC 드라이버 등록.
			Class.forName(prop.getProperty("driver"));
			
			// 2) Connection 객체 생성
			if(conn == null) {
				synchronized(JDBCTemplate.class) { // 2번 해결방법.
					//한번더검사
					if(conn == null) {
						conn = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
					}
				}
			}
			/*
			 * 단점 : 멀티스레드 환경에서 안전하지 않음.
			 * 		ex) 동시에 여러명의 사용자가 getConnection을 실행했는데 
			 * 			당시 서버의 Connection 객체가 null이었다면,
			 * 			그만큼의 conn객체가 생성되었을것.
			 * 
			 * 해결방법 1) 메서드에 synchronized 키워드를 이용하는방법.
			 * 			 멀티스레드 환경에서 발생하는 문제는 해결되지만,
			 * 			 getConnection을 이용해 객체를 가져올때마다 lock이 걸리기 때문에 좋지 않음.
			 * 
			 * 해결방법 2) 블록에 synchronized 키워드 이용
			 * if(conn == null){
			 * 	synchronized(클래스 이름){
			 * 		분리시킬 코드
			 * 	}	
			 * }
			 * 
			 * 해결방법 3) 이론초기화
			 * private static final Connection conn = DriverManager.getConnection("ip주소", '아이디', '비밀번호');
			 * static변수는 서버시작과 동시에 static메모리 영역으로 들어가기때문에 스레드로부터 안전함
			 * 단, 인스턴스 만드는 과정이 길고, 메모리를 많이 사용하면 단점이 될 수 있다.
			 */
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	// 2. 전달받은 JDBC용 객체를 반납시켜주는 메소드(각 객체별로)
	// 2_1) Connection 객체를 전달받아서 반납시켜주는 메서드
	public static void close() {
		
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}
	
	// 2_2) Statement 객체를 전달받아서 반납시켜주는 메소드(오버로딩 사용)
	// 		=> 다형성으로 인해 PreparedStatement 객체 또한 매개변수로 전달가능.
	public static void close(Statement stmt) {
		try {
			if(stmt != null && stmt.isClosed()) {
				stmt.close();		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 2_3) ResultSet 객체를 전달받아서 반납시켜주는 메소드(오버로딩 적용)
	public static void close(ResultSet rset) {
		
		try {
			if(rset != null && !rset.isClosed()) {
				rset.close();	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 3. 전달받은 Connection 객체를 가지고 트랜잭션 처리를 해주는 메소드
	// 3_1) 전달받은 Connection 객체를 Commit 시켜주는 메소드
	
	public static void commit() {
		
		try {
			if(conn != null && conn.isClosed()) {
				conn.commit();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// 3_2) 전달받은 Connection 객체를 ROLLBACK 시켜주는 메소드
	public static void rollback() {
		
		try {
			if(conn != null && !conn.isClosed()) {
				conn.rollback();				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
