package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Member;

/*
 *  DAO(Data Access object)
 *  controller를 통해서 호출
 *  controller에서 요청받은 그 실질적인 기능을 수행하기 위해서 db에 직접접근한 후, sql문을 실행하여 실행결과를 받음
 */
public class MemberDao {
	/*
	 * JDBC객체
	 * - Connection : DB와의 연결정보를 담고있는 객체 (IP주소, PORT번호, 계정명, 비밀번호)
	 * - (Prepared)Statement : 해당 DB에 SQL문을 전달하고 실행한 후 결과를 받아내는 객체.
	 * - ResultSet : 만일 실행한 sql문이 SELECT문일 경우 조회된 결과들이 담겨있는 객체.
	 * 
	 * PreparedStatement 특징 : SQL문을 바로 실행하지 않고 잠시 보관하는 개념.
	 * 						   미완성된 SQL문을 먼저 전달하고 실행하기 전에 완성 형태로 만든 후 실행만 하면 됨.
	 * 						   => 미완성된 SQL문 만들기(사용자가 입력한 값들이 들어갈 수 있는 공간을 ?로 확보
	 * 							  각 ?(위치 홀더)에 맞는 값들을 셋팅.)
	 * 
	 * Statement와 PreparedStatement(자식) 관계임.
	 * 둘의 차이점
	 * 1) Statement는 완성된 SQL문, PreparedStatement는 미완성된 sql문
	 * 
	 * 2) Statement 객체 생성시 			stmt = conn.createStatement();
	 * 	  PreparedStatement 객체 생성 시   psmt = conn.prepareStatement(sql);
	 * 
	 * 3) Statement로 SQL문 실행시 		결과 = stmt.executeXXX(sql);
	 * 	  PreparedStatement로 sql문 실행시 ?로 표현된 빈 공간을 실제 값으로 채워주는 과정을 거친 후 실행한다.
	 * 									  psmt.setString(?위치, 실제값);
	 * 									  psmt.setInt(?위치, 실제값);
	 * 								      결과 = psmt.executeXXX(); 
	 * 
	 * JDBC 처리 순서
	 * 1) JDBC DRIVER 등록 : 해당 DBMS가 제공하는 클래스 등록.
	 * 2) Connection 객체 생성 : 접속하고자하는 db의 정보를 입력해서 DB에 접속하면서 생성(URL, 정명, 비밀번호);
	 * 3_1) PreparedStatement 객체 생성 : Connection 객체를 이용해서 생성(미완성된 sql문을 담은채로 생성)
	 * 3_2) 현재 미완성된 sql문을 완성형태로 채우기
	 * 		=> 미완성된 경우에만 해당됨 / 완성된 경우에는 생략가능.
	 * 4) SQL문 실행 : executeXXX() => sql매개변수 없음.
	 * 	  > SELECT문 실행시 : executeQuery()호출
	 * 	  > 나머지 dml문 실행시 : executeUpdate() 호출.
	 * 5) 결과받기
	 * 6_1) ResultSet에 담겨있는 데이터들을 하나하나 뽑아서 vo객체에 담기(ArrayList 묶어서 관리)
	 * 6_2) 트랜잭션 처리(성공이면 commit, 실패면 rollback)
	 * 7) 다쓴 JDBC자원 반납 => 생성된 순서의 역순으로.
	 * 8) 결과반환(Controller)
	 */
	
	public int insertMember(Connection conn, Member m) {
		//0) 필요한 변수들 먼저 셋팅.
		int result = 0;
		
		PreparedStatement psmt = null;
		
		String sql = "INSERT INTO MEMBER"
				+ " VALUES(SEQ_USERNO.NEXTVAL , ?, ?, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
		
		try {
			psmt = conn.prepareStatement(sql);
			
			psmt.setString(1, m.getUserId());
			psmt.setString(2, m.getUserpwd());
			psmt.setString(3, m.getUserName());
			psmt.setString(4, m.getGender());
			psmt.setInt(5, m.getAge());
			psmt.setString(6, m.getEmail());
			psmt.setString(7, m.getPhone());
			psmt.setString(8, m.getAddress());
			psmt.setString(9, m.getHobby());
			
			
			// sql문 실행하고 db로부터 결과값 받기(행의 갯수)
			result = psmt.executeUpdate();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(psmt);
		}
		
		return result;
	}
	
	public ArrayList<Member> selectAllMember(Connection conn) {
		// 0) 필요한 변수들 셋팅
		ArrayList<Member> list = new ArrayList<>(); 
		
		// JDBC 관력 객체들
		PreparedStatement psmt = null; 
		ResultSet rset = null;
		
		// 실행할 SQL문
		// 전체조회 같은 ?할 값이 없을때는 Statement를 사용해도 무방.
		String sql = "SELECT * FROM MEMBER";
		
		try {
			// 3_1) PreparedStatement 객체 생성
			psmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 sql문일 경우 완성시켜주기. -> 완성되어있으므로 스킵.
			
			// 4, 5) SQL문 실행한 후 결과값 받기
			rset = psmt.executeQuery();
			
			// 6_1) 현재 조회결과가 담긴 ResultSet에서 한행씩 뽑아서 VO객체에 담기.
			
			while(rset.next()) {
				
				Member m = new Member();
				
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
				
				// 리스트에 해당 Member객체를 담기.
				list.add(m);
			}
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(psmt);
		}
		return list;
	}
	
	public Member selectByUserId(Connection conn, String userId) {
		// 0) 필요한 변수 셋팅
		Member m  = null;
		
		//JDBC관련객체
		PreparedStatement psmt = null;
		ResultSet rset = null;
		
		//실행할 SQL문 작성
		// SELECT * FROM MEMBER WHERE USERID = 'XXX'
		String sql = "SELECT * FROM MEMBER WHERE USERID = ?";
		
		try {
			
			// 3_1) PreparedStatement 객체 생성
			psmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문을 완성시켜야됨.
			psmt.setString(1, userId);
			
			// 4, 5) SQL문 실행 후 결과받기.
			rset = psmt.executeQuery();
			
			// 6)
			if (rset.next()) { // rset.next() : true
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(psmt);
		}
		// 8)
		return m;
	}
	
	public ArrayList<Member> selectByUserName(Connection conn, String keyword){
		
		// 0) 
		ArrayList<Member> list = new ArrayList<>();
		


		PreparedStatement psmt = null;
		ResultSet rset = null;
		
		// 실행할 sql문.
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%xxx%'
		//String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%?%'";
		//? 값 안에 실제 값을 넣었을때 
		// SELECT * FROM MEMBER WHERE USERNAME LIKE '%'범석'%'=> 정상수행 안됨.
		
		// 방법 1) 
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%'|| ? ||'%'";
		
		// 방법 2)
		sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		// 방법 3)
		//sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE CONCAT(CONCAT('%', ?),'%')";
		
		try {
			// 3_1) PreparedStatement 객체 생성
			psmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문 완성시키기.
			psmt.setString(1, "%"+keyword+"%");
			
			// 4, 5)
			rset = psmt.executeQuery();
			
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
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(psmt);
		}
		// 8)
		return list;	
	}
	
	public int updateMember(Connection conn, Member m) {
		
		//0)
		int result = 0;
		

		PreparedStatement psmt = null;
		
		// sql문
		
		String sql = "UPDATE MEMBER "
					+ "SET USERPWD = ? ,"
					+ "EMAIL = ? ,"
					+ "PHONE = ? ,"
					+ "ADDRESS = ? "
					+ "WHERE USERID = ?";
		
		try {
			// 3_1) PreparedStatement 객체 생성
			psmt = conn.prepareStatement(sql);
			
			// 3_2) 미완성된 SQL문 완성시키기
			psmt.setString(1, m.getUserpwd());
			psmt.setString(2, m.getEmail());
			psmt.setString(3, m.getPhone());
			psmt.setString(4, m.getAddress());
			psmt.setString(5, m.getUserId());
			
			// 4, 5) 
			result = psmt.executeUpdate();
			
			// 6_2) 트랜잭션 처리
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(psmt);
		}
		// 8)
		return result;			
	}
	
	public int deleteMember(Connection conn, String userId) {
		// 0) 
		int result = 0;
		
		PreparedStatement psmt = null;
		
		//실행할 sql문
		//DELETE FROM MEMBER WHERE USERID = 'XXX'
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		
		try {
			
			// 3_1) PreparedStatement 객체 생성
			psmt = conn.prepareStatement(sql);
			
			// 3_2)  미완성된 sql문 완성시키기
			psmt.setString(1, userId);
			
			// 4번 sql문 실행 5번 결과값 받기
			result = psmt.executeUpdate();
			
			// 6_2) 트랜잭션 처리
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(psmt);
		}
		// 8)
		return result;
		
	}
	
}


