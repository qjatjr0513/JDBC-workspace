package com.kh.run;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberView;

public class Run {

	public static void main(String[] args) {
		// MVC? 웹어플리케이션 역할을 크게 3등분해서 나누어서 개발하는 방법
		// M(MODEL) : 데이터 처리 담당(데이터를 담기위한 클래스(VO), 데이터들이 보관된 공간에 직접적으로 접근해서 데이터를 주고받는 클래스(DAO))
		// V(VIEW) : 화면을 담당(사용자에게 보이는 시각적인 요소, 입력 및 출력)
		// C (CONTROLLER) : 사용자의 요청을 처리해주는 담당(사용자의 요청을 처리한 후 그에 해당하는 응답화면을 지정.)
		MemberView mv = new MemberView();
		mv.mainMenu();
		
		/*
		 * Properties 복습
		 * Properties : Map 계열의 컬렉션, key + value 세트로 담는게 특징.
		 * Properties는 주로 외부 설정파일을 읽어오기 또는 파일형태로 출력할때 씀.
		 * 
		 * Properties, xml파일로 내보내기 => store(), storeXML()메서드 사용.
		 * 
		 */
//		Properties prop = new Properties();
//		
//		prop.setProperty("driver", "oracle.jdbc.driver.OracleDriver");
//		prop.setProperty("url", "jdbc:oracle:thin:@localhost:1521:xe");
//		prop.setProperty("username", "JDBC");
//		prop.setProperty("password", "JDBC");
//		
//		try {
//			//prop.store(new FileOutputStream("resources/driver.properties"), "driver.properties");
//			
//			prop.storeToXML(new FileOutputStream("resources/query.xml"), "query.xml");
//			// resources : 주로 프로젝트 내의 외부 파일들을 저장하는 역할
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// 읽어들이기 => load(), loadToXML()
		
		
	}

}
