package com.kh.run;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

public class Run {

	public static void main(String[] args) {
//		MemberDao md =  new MemberDao();
//		Member m = new Member("dlqjatjr", "1234", "이범석",
//				"M", 25, null, null, null, "영화감상");
//		
//		md.insertMember(m);
		MemberView mv = new MemberView();
		mv.mainMenu();
	}

}
