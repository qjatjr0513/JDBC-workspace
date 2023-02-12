package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

/*
 *  DAO(Data Access object)
 *  controller를 통해서 호출
 *  controller에서 요청받은 그 실질적인 기능을 수행하기 위해서 db에 직접접근한 후, sql문을 실행하여 실행결과를 받음
 */
public class MemberDao {
	/*
	 *  JDBC 용 객체
	 *  - Connection : db의 연결정보를 담고 있는 객체(ip주소, port번호, 계정명, 비밀번호)
	 *  - Statement : 해당 db에 sql문을 전달하고 실행한 후 결과를 받아내는 객체
	 *  - ResultSet : 만일 실행한 sql문이 select문일 경우 조회된 결과들이 담겨있는 객체
	 *  
	 *  *JDBC 처리순서
	 *  1) 	JDBC Driver 등록 : 해당 dbms가 제공하는 클래스 등록.
	 *  2)	Connection 생성 : 접속하고자 하는 DB정보를 입력해서 DB에 접속하면서 생성
	 *  3) Statement 생성 : Connection 객체를 이용해서 생성
	 *  4) SQL문을 전달하면서 실행 : Statement 객체를 이용해서 sql문 실행
	 *  									> Select문 실행시 -	executeQuery() 메소드 이용하여 실행
	 *  									> 나머지 dml문 - executeUpdate() 메소드를 이용하여 실행
	 *  5) 결과 받기
	 *  				> 	SELECT 문일 경우 - ResultSet객체(조회된 데이터들이 담겨있음)로 받기
	 *  				> 	나머지 dml문일 경우 - int형 변수(처리된 행의 갯수)로 받기
	 *  
	 *  6) ResultSet 객체에 담긴 데이터들을 하나씩 뽑아서 VO 객체에 담기 (ArrayList로 묶어서 관리함)
	 *  7) 다 쓴 JDBC용 객체들을 반드시 자원반납시켜줌 -> 생성된 순서의 역순으로
	 *  8) SQL문 수행 결과를 Controller에 반환
	 *  		>	select문일 경우 -> 6번 결과값
	 *  		> 	나머지 dml문일 경우 - int 형 값
	 * 
	 *  statement 특징 : 완성된 SQL문을 실행할 수 있는 객체
	 *  
	 *  완성된 SQL문)
	 *  SELECT * FROM EMPLOYEE;
	 */
	

	/**
	 *  사용자가 회원 추가 요청시 입력했던 값을 가지고 INSERT문을 실행하는 메소드.
	 * @param m : 사용자가 입력했던 아이디 ~ 취미까지의 값이 담겨있는 Member객체.
	 * @return : INSERT문을 수행한 결과 처리된 행의 갯수.
	 */ // Alt +shift + J(메소드 주석)
	public int insertMember(Member m) {
		// 0) 필요한 변수들 셋팅.
		int result = 0; // 처리된 결과를 담아줄 변수.
		Connection conn = null; //접속된 db의 연결정보를 담는 변수.
		Statement stmt = null;
		
		String sql = "INSERT INTO MEMBER VALUES (SEQ_USERNO.NEXTVAL , '"
																	  +m.getUserId()+"' , '"
																	  +m.getUserpwd()+"' , '"
																	  +m.getUserName()+"' , '"
																	  +m.getGender()+"' , "
																	  +m.getAge()+" , '"
																	  +m.getEmail()+"' , '"
																	  +m.getPhone()+"' , '"
																	  +m.getAddress()+"' , '"
																	  +m.getHobby()+"' , DEFAULT"+
																	  ")";
		try {
			//	1) 	JDBC 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("jdbc 드라이버 등록 성공.");
			// 에러나는 케이스 2
			// 1) 오타가 있을 경우
			// 2) ojdbc6.jar를 누락시켰을 경우.
			
			// 2) Connection 객체 생성 --> db와 연결시키겠다.
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC" );
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4) db에 완성된 sql문을 전달하면서 실행 후  5) 결과 받기. 
			result = stmt.executeUpdate(sql);
			
			// 6) 트랜잭션관리
			if(result > 0) { // 1개 이상의 행이 INSERT되었다.(성공) -> 커밋.
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("해당하는 드라이버를 찾을 수 없습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			//7) 다쓴 JDBC 객체 반납 -> 생성된 순서의 역순으로 닫아주기
			try {
				stmt.close(); // 순서는 stmt 그다음 conn
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// 8) 결과반환 
		return 1;  // 처리된 행의 갯수.
	}
	
	public ArrayList<Member> selectAllMember(){ // select -> ResultSet -> ArrayList
		
		// 0) 필요한 변수 셋팅
		
		// Connection, Statement, Resultset -> finally에서 자원 반납하려면 try문 밖에 미리 셋팅해야함.
		Connection conn = null; // 접속된 db의 연결정보를 담는 변수
		Statement stmt = null; // sql문 실행후 결과를 받기 위한 변수
		ResultSet rset = null; // select문이 실행된 조회결과값들이 처음에 실질적으로 담길 객체
		
		// 조회된 결과를 뽑아서 담아줄 변수(ArrayList)
		ArrayList<Member> list = new ArrayList<>(); // 
		//실행할 sql문(완성된 형태로, 세미콜론 X)
		String sql = "SELECT * FROM MEMBER";
		
		
		try {
			// 1) JDBC DRIVER 등록.
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 등록
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC" );
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4 , 5) SQL문을 실행하면서 결과값을 전달.
			rset = stmt.executeQuery(sql);
			
			// 6) 현재 조회결과가 담긴 ResultSet에서 한행씩 뽑아서 VO객체에 담기.
			// next() : 커서를 한줄 아래로 옮겨주고, 해당 행이 존재할 경우 true, 아니면 false반환.
			while(rset.next()) {
				
				// 현재 rset의 커서가 가리키고 있는 해당 행의 데이터를 하나씩 뽑아서 Member 객체로 만듬.
				Member m = new Member();
				/*
				 * rset으로 부터 어떤 컬럼에 해당하는 값을 뽑을건지 제시
				 * => 컬럼명(대소문자 가리지 않음), 칼럼순번
				 * => 권장사항 : 칼럼명으로 쓰고, 대소문자로 쓰는것 권장.
				 * rset.getInt(칼럼명 또는 순번) : int형 값을 뽑을때
				 * rset.getString(칼럼명 또는 순번) : String 값을 뽑을때
				 * rset.getDate(칼럼명 또는 순번) : Date값을 뽑을때		 
				 */
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserpwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
				// 한 행에 대한 모든 칼럼의 데이터 값들을 
				// 각각의 필드에 담아 하나의 Member 객체로 만들기.
				
				// 리스트에 해당 Member객체를 담기.
				list.add(m);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 다쓴 jdbc자원 반납(생성된 순서의 역순)
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public Member selectByUserId(String userId) { // select문, => ResultSet 1개행, 반복 x
		
		// 0) 필요한 변수들 셋팅
		// 조회된 한 회원에 대한 정보를 담을 변수.
		Member m = null;
		
		//JDBC관련 객체들.
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문 필요.(완성된 형태, 세미콜론 x)
		String sql = "SELECT * FROM MEMBER WHERE USERID = '"+userId+"'";
		
		try {
			// 1)  JDBC 드라이버 등록.
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성 --> db와 연결시키겠다.
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC" );
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) SQL문 실행하고, 결과받기
			rset = stmt.executeQuery(sql);
			
			// 6) 현재 조회결과가 담긴 ResultSet에서 한행씩 뽑아서 VO 객체 담기. =>id 검색은 한행만 조회될 예정.
			if(rset.next()) { //커서를 한행 움직여보고 조회결과가 있다면 true, 없다면 false
				m = new Member();
				
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserpwd(rset.getString("USERPWD"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("EMAIL"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("ENROLLDATE"));
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 자원반납.
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return m;
		
	}
	
	public ArrayList<Member> selectByUserName(String keyword){
		
		// 0) 필요한 변수 셋팅
		ArrayList<Member> list = new ArrayList<>();
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문 작성
		
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%keyword%'
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%"+keyword+"%'";
		
		try {
			// 1)  JDBC DRIVER 연결.
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성 --> db와 연결시키겠다.
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC" );
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) SQL문 실행하고, 결과받기
			rset = stmt.executeQuery(sql);
			
			// 6_1)
			while(rset.next()) {
				list.add(new Member(
						rset.getInt("USERNO"),
						rset.getString("USERID"),
						rset.getString("USERPWD"),
						rset.getString("USERNAME"),
						rset.getString("GENDER"),
						rset.getInt("AGE"),
						rset.getString("EMAIL"),
						rset.getString("PHONE"),
						rset.getString("ADDRESS"),
						rset.getString("HOBBY"),
						rset.getDate("ENROLLDATE")));
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				rset.close();
				stmt.close();
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public int updateMember(Member m) { // UPDATE문 => 처리된 행의 갯수(INT) => 트랜잭션 처리
		
		// 0) 필요한 변수들 셋팅
		int result = 0;
		Connection conn = null;
		Statement stmt = null;
		
		//실행할 sql문
		/*
		 *	UPDATE MEMEBER
		 *	SET USERPWD = 'newPwd',
		 *		EMAIL = 'newEmail',
		 *		PHONE = 'newPhone',
		 *		ADDRESS = 'newAddress'
		 *	WHERE USERID = 'userId'
		 */
		
		String sql = "UPDATE MEMBER "
				+ "SET USERPWD = '"+m.getUserpwd()+"', "
				+ "EMAIL = '"+m.getEmail()+"', "
				+ "PHONE = '"+m.getPhone()+"', "
				+ "ADDRESS = '"+m.getAddress()+"' "
				+ "WHERE USERID = '"+m.getUserId()+"'"
				;
		
		try {
			// 1)  JDBC DRIVER 연결.
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2) Connection 객체 생성 --> db와 연결시키겠다.
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC" );
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5)
			result = stmt.executeUpdate(sql);
			
			// 6) 트랜잭션처리
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 자원반납.
			try {
				stmt.close();
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public int deleteMember(String userId) {
		
		//0) 필요한 변수 셋팅
		int result = 0;
		
		Connection conn = null;
		Statement stmt = null;
		
		// 실행할 sql문
		// DELETE FROM MEMBER WHERE USERID = 'userId'
		String sql = "DELETE FROM MEMBER WHERE USERID = '"+userId+"'";
		
		try {
			// 1)  JDBC DRIVER 연결.
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// 2) Connection 객체 생성 --> db와 연결시키겠다.
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "JDBC", "JDBC" );
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5)
			result = stmt.executeUpdate(sql);
			
			// 6) 트랜잭션처리
			if(result > 0) {
				conn.commit();
			}else {
				conn.rollback();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 7) 자원 반납
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
}

