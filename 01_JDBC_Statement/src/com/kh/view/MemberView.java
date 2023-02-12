package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

/*
 * 	View : 사용자가 보게될 시각적인 요소를 담당(화면 => 입력, 출력)
 * 
 * 
 */

/**
 * @author 이범석
 *
 */
public class MemberView {

	// 전역으로 다 쓸수 있는 Scanner 객체 생성
	private Scanner sc = new Scanner(System.in);
	private MemberController mc = new MemberController();
	/**
	 * 사용자가 보게될 첫 화면
	 */
	public void mainMenu() {
		while (true) {
			System.out.println("***** 회원관리 프로그램 *****");
			System.out.println("1. 회원 추가"); // insertMember();
			System.out.println("2. 회원 전체 조회"); // selectAllMember();
			System.out.println("3. 회원 아이디로 조회"); // selectByUserId();
			System.out.println("4. 회원 이름 키워드 검색"); // selectByUserName();
			System.out.println("5. 회원 정보 변경"); // updateMember();
			System.out.println("6. 회원 탈퇴"); // deleteMember();
			System.out.println("0. 프로그램 종료");
			System.out.println("-------------------------------------------");
			System.out.print("이용할 메뉴 선택 : ");
			int menu = Integer.parseInt(sc.nextLine());

			switch (menu) {
			case 1:
				insertMember();
				break;
			case 2: 
				selectAllMember();
				break;
			case 3:
				selectByUserId();
				break;
			case 4:
				selectByUserName();
				break;
			case 5:
				updateMember();
				break;
			case 6:
				deleteMember();
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				return;
			default:
				System.out.println("잘못된 메뉴를 선택했습니다. 다시 입력해주세요.");
			}
		}
	}
	public void deleteMember() {
		System.out.println("----- 회원 정보 탈퇴 -----");
		
		System.out.print("탈퇴할 회원 ID : ");
		String userId = sc.nextLine();
		
		mc.deleteMember(userId);
	}
	
	public void updateMember() {
		System.out.println("----- 회원 정보 변경 -----");
		
		// 변경할 회원의 아이디
		System.out.print("변경할 회원의 아이디 : ");
		String userId = sc.nextLine();
		
		// 변경할 정보들
		System.out.print("변경할 비밀번호 : ");
		String newPwd = sc.nextLine();
		
		System.out.print("변경할 이메일 : ");
		String newEmail = sc.nextLine();
		
		System.out.print("변경할 휴대전화번호 (숫자만) : ");
		String newPhone = sc.nextLine();
		
		System.out.print("변경할 주소 : ");
		String newAddress = sc.nextLine();
		
		mc.updateMember(userId, newPwd, newEmail, newPhone, newAddress);
	}
	
	/**
	 * 사용자에게 검색할 회원의 이름을 입력받은후 조회 요청하는 메서드.
	 */
	public void selectByUserName() {
		System.out.println("------ 회원 이름 키워드 검색 ------");
		
		 System.out.print("회원 이름 키워드 입력 : ");
		 String keyword = sc.nextLine();
		 
		 mc.selectByUserName(keyword);
	}
	

	
	/**
	 * 사용자에게 검색할 회원의 아이디를 입력받은 후 조회요청하는 메서드.
	 */
	public void selectByUserId() {
		System.out.println("----- 회원 아이디 조회 -----");
		
		System.out.print("검색할 회원 아이디 : ");
		String userId = sc.nextLine();
		
		//입력한 아이디를 회원 아이디 검색 요청시 같이 넘김.
		mc.selectByUserId(userId);
	}
	
	/**
	 * 회원 전체를 조회할 수 있는 화면.
	 */
	public void selectAllMember() {
		System.out.println("---- 회원 전체 조회 -----");
		
		// 회원 전체 조회를 MemberController 요청.
		mc.selectAllMember();
	}
	
	/**
	 * 회원 추가용 화면
	 * 추가하고자 하는 회원의 정보를 입력받아서 추가 요청할 수 있는 화면을 제공.
	 */
	public void insertMember() {
		System.out.println("------ 회원 추가 ------");
		
		//입력
		System.out.print("아이디 : ");
		String userId = sc.nextLine();
		
		System.out.print("비밀번회 :");
		String userPwd = sc.nextLine();
		
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		
		System.out.print("성별(M/F) : ");
		// m, f ?
		String gender = String.valueOf(sc.nextLine().toUpperCase().charAt(0));
		
		System.out.print("나이 : ");
		int age = Integer.parseInt(sc.nextLine());
		
		System.out.print("이메일 : ");
		String email = sc.nextLine();
		
		System.out.print("핸드폰 번호(숫자만) : ");
		String phone = sc.nextLine();
		
		System.out.print("주소 : ");
		String address = sc.nextLine();
		
		System.out.print("취미 (취미가 여러개인 경우,로 공백없이 나열하세요): ");
		String hobby = sc.nextLine();
		
		mc.insertMember(userId, userPwd, userName, gender, age, email, phone, address, hobby);
		
	}
	
	/**
	 * 서비스요청 성공시 사용자에게 보여줄 화면
	 * @param message : 성공메세지
	 */
	public void displaySucess(String message) {
		System.out.println("\n 서비스 요청 성공 : "+message);
	}
	
	/**
	 * 서비스 요청시 사용자에게 보여줄 화면.
	 * @param message : 실패 메세지
	 */
	public void displayFail(String message) {
		System.out.println("\n 서비스 요청 실패 : "+message);
	}
	
	/**
	 * 조회 서비스 요청시 조회결과가 없는 경우 보게될 화면.
	 * @param message : 사용자에게 보여줄 메세지.
	 */
	public void displayNoData(String message) {
		System.out.println(message);
	}
	
	/**
	 * 
	 * @param list
	 */
	public void displayMembers(ArrayList<Member> list) {
		System.out.println("\n조회된 데이터는 "+list.size()+"건 입니다.\n");
		for(int i = 0; i<list.size(); i++) {
			System.out.println(list.get(i));
		}
	}
	
	public void displayMember(Member m) {
		System.out.println("\n 조회된 데이터는 다음과 같습니다.");
		System.out.println(m);
	}
}
