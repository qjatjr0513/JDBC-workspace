package com.kh.controller;

import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

// Controller : view를 통해서 요청이 들어오면 요청한 기능을 처리
//				해당 메소드로 전달된 데이터들을 가동처리 한후 Dao 메소드 호출시 전달.
//				Dao로 부터 반환받은 결과에 따라 사용자가 보게 될 화면(view단의 메소드를 호출)을 지정해줌. (Response)
public class MemberController {

	//view에서 전달한 값을 통해 Member 객체를 만들고 memberDao로 전달시키는 메서드
	/**
	 * 사용자의 회원 추가 요청을 처리해주는 메소드
	 * @param userId : 추가할 아이디
	 * @param userPwd : 추가할 비밀번호
	 * @param userName
	 * @param gender
	 * @param age
	 * @param email
	 * @param phone
	 * @param address
	 * @param hobby
	 * => 사용자가 요청시 입력한 값들.
	 */
	MemberDao md = new MemberDao();
	public void insertMember(String userId, String userPwd, String userName, 
			String gender, int age, String email, String phone, String address, String hobby ) {
		// 1. 전달된 데이터들을 Member 객체에 담기 => 가공처리.
		Member m = new Member(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
		//2. Dao의 insertMember 메소드 호출.
		int result = md.insertMember(m);
		
		//3. 결과값에 따라서 사용자가 보게될 화면 지정.
		if(result > 0) { //회원 추가 성공
			//성공 메세지를 띄워주는 화면 호출.
			new MemberView().displaySucess("회원추가 성공");
		} else { //회원 추가 실패
			//실패 메시지를 띄워주는 화면 호출.
			new MemberView().displayFail("회원추가 실패");
		}
	}
	
	
	/**
	 * 사용자의 회원 전체 조회 요청을 처리해주는 메소드.
	 */
	public void selectAllMember() {
		
		//	결과값을 담을 변수
		// 	Select -> ResultSet -> ArrayList<Member>
		
		ArrayList<Member> list = md.selectAllMember();
		
		// 조회 결과가 있는지 없는지 판단 후 사용자가 보게될 view 화면 지정.
		if(list.isEmpty()) { // 텅빈 리스트일 경우 -> 조회결과가 없음.
			new MemberView().displayNoData("전체 조회 결과가 없습니다.");
		}else { // 조회가 되었을 경우 조회결과 있음.
			new MemberView().displayMembers(list);
		}
	}
	
	
	/**
	 * 사용자의 아이디로 검색 요청을 처리해주는 메소드
	 * @param userId : 사용자가 입력했던 검색하고자하는 아이디
	 */
	public void selectByUserId(String userId) {
		
		// Select -> ResultSet -> Member
		Member m = md.selectByUserId(userId);
		
		if(m == null) { // 조회결과가 없는 경우.
			new MemberView().displayNoData(userId+"에 해당하는 검색 결과가 없습니다.");			
		}else { // 조회결과가 있는 경우.
			new MemberView().displayMember(m);
		}
		
	}
	
	public void selectByUserName(String keyword) {
		
		// SELECT -> ResultSet -> ArrayList<Member>
		ArrayList<Member> list = md.selectByUserName(keyword);		
		
		
		if(list.isEmpty()) { // 검색결과가 없는 경우.
			new MemberView().displayNoData(keyword+"에 해당하는 검색결과가 없습니다.");
		}else { //만약 조회결과가 있는경우
			new MemberView().displayMembers(list);
		}
	}
	
	
	public void updateMember(String userId, String newPwd, String newEmail, String newPhone, String newAddress) {
		
		// VO객체에 입력받은 값들 담기(가공처리)
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserpwd(newPwd);
		m.setEmail(newEmail);
		m.setPhone(newPhone);
		m.setAddress(newAddress);
		
		int result = md.updateMember(m);
		
		if(result > 0) { //성공
			new MemberView().displaySucess("회원 정보 변경 성공");
		}else { //실패
			new MemberView().displayFail("회원 정보 변경 실패");
		}
	}
	
	public void deleteMember(String userId) {
		
		int result = md.deleteMember(userId);
		
		if(result > 0) { // 성공
			new MemberView().displaySucess("회원 탈퇴 성공");
		}else { // 실패
			new MemberView().displayFail("회원 탈퇴 실패");
		}
	}
	
}
