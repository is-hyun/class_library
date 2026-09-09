package com.tenco;

import com.tenco.dao.StudentDAO;

//TIP 코드를 <b>실행</b>하려면 <shortcut actionId="Run"/>을(를) 누르거나
// 에디터 여백에 있는 <icon src="AllIcons.Actions.Execute"/> 아이콘을 클릭하세요.
public class Main {
    public static void main(String[] args) {
        // 학생 전체 조회 테스트
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.getAllStudent();
        // 학생 학번 조회 테스트

    }
}