package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

/*
 * Service : 기존의 Dao의 역할을 분담
 * 			 컨트롤러에서 서비스 호출(Connection 객체 생성)후 서비스를 거쳐서 dao로 호출
 * 			 DAO 호출시 커넥션 객체와 기존에 넘기고자했던 매개변수를 같이 넘겨줌.
 * 			 DAO처리가 끝나면 서비스단에서 결과에 따른 트랜잭션 처리도 같이 해줌.
 * 			 => 서비스단을 추가함으로써 DAO는 순수하게 SQL문을 처리하는 부분만 남게됨.
 */
public class MemberService {

	MemberDao md = new MemberDao();
	
	public int insertMember(Member m) {
		
		// Connection 객체를 생성한다.
		Connection conn = JDBCTemplate.getConnection();
		
		// DAO호출할때 Connection 객체와 기존에 넘기고자했던 객체를 매개변수로 같이 넘겨준다.
		int result = md.insertMember(conn, m);
		
		// 결과에 따른 트랜잭션 처리.
		if(result > 0) { // 성공
			JDBCTemplate.commit();
		}else {
			JDBCTemplate.rollback();
		}
		
		// connection 객체 반납.
		JDBCTemplate.close();
		
		// 결과반환
		return result;
	}
	
	public ArrayList<Member> selectAllMember(){
		
		// 1) Connection 객체생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) 결과값을 담을 변수 만들고 DAO호출해서 리턴값 받기.
		// 	  단, Connection 객체와 컨트롤러부터 넘기고자하는 값이 있다면 함께 넘겨줌.
		ArrayList<Member> list = md.selectAllMember(conn);
		
		// 3) 사용한 Connection객체 반납
		JDBCTemplate.close();
		
		// 4) 결과값 리턴.
		return list;
		
	}
	
	public Member selectByUserId(String userId) {
		
		//1) Connection 객체 생성
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) 결과값을 담을 변수 만들고 dao 호출.
		Member m = md.selectByUserId(conn, userId);
		
		// 3) 사용한 Connection객체 반납.
		JDBCTemplate.close();
		
		// 4) 결과값 리턴.
		return m;
	}
	
	public ArrayList<Member> selectByUserName(String keyword){
		// 1) Connection 
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) 반환받을 변수 선언하고 dao연결
		ArrayList<Member> list = md.selectByUserName(conn, keyword);
		
		// 3) close
		JDBCTemplate.close();
		
		// 4) 리턴
		return list;
	}
	
	public int updateMember(Member m) {
		// 1) 
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) 
		int result = md.updateMember(conn, m);
		
		// 3) 트랜잭션 처리
		if(result > 0) { //성공
			JDBCTemplate.commit();
		} else { //실패
			JDBCTemplate.rollback();
		}
		
		// 4) conn 객체 반납
		JDBCTemplate.close();
		
		// 5) 
		return result;
		
	}
	
	public int deleteMember(String userId) {
		// 1)
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) 
		int result = md.deleteMember(conn, userId);
		
		// 3)트랜잭션 처리
		if(result > 0) {
			JDBCTemplate.commit();
		} else {
			JDBCTemplate.rollback();
		}
		
		// 4)
		JDBCTemplate.close();
		
		//5) 
		return result;
		
		
	}
	
	
	
	
	
	
	
	
	
}
