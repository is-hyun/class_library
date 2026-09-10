package com.tenco.view;

import com.tenco.dto.Book;
import com.tenco.dto.Borrow;
import com.tenco.dto.Student;
import com.tenco.service.LibraryService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

// 키보드 입력을 받아 Service에 전달하고 결과를 화면에 출력
// SQL을 직접 실행하지 않고, 업무 규칙도 판단하지 않음
// 입력 형식을 검사하고 객체 내의 값ㅇ르 구해서 위임
public class LibraryView_self {

    private final LibraryService libraryService = new LibraryService();
    private final Scanner scanner = new Scanner(System.in);

    // 현재 로그인한 학생 정보가 null이 아니라면 로그인된 상태로 본다
    // 만약 null이라면 로그인이 필요한 기능에서 로그인 요청을 먼저 유도해야 함
    private Integer currentStudentId = null;
    private String currentStudentName = null;
    private Student currentStudent = null;

    public static void printBook(List<Book> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        for (int i =0; i < list.size(); i++) {
            String available = list.get(i).isAvailable() ? "대출 가능" : "대출 중";

            System.out.printf("ID : %d | %s | %s | %s | %d | %s | %s \n",
                    list.get(i).getId(),
                    list.get(i).getTitle(),
                    list.get(i).getAuthor(),
                    list.get(i).getPublisher(),
                    list.get(i).getPublicationYear(),
                    list.get(i).getIsbn(),
                    available.toString()
            );
        }
    }

    public static void printBorrow(List<Borrow> list) {
        for (int i =0; i < list.size(); i++) {

            System.out.printf("대출 ID : %d | 도서 ID : %d | %s | 학생 ID : %d | %s | %s | %s \n",
                    list.get(i).getId(),
                    list.get(i).getBookId(),
                    list.get(i).getBookTitle(),
                    list.get(i).getStudentId(),
                    list.get(i).getStudentName(),
                    list.get(i).getBorrowDate()
            );
        }
    }

    public static void printStudnet(List<Student> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("ID : %d | %s | %s \n",
                    list.get(i).getId(),
                    list.get(i).getName(),
                    list.get(i).getStudentId()
            );
        }
    }
    // 프로그램 메인 루프
    // 1. 메뉴 출력
    // 2. 번호 입력
    // 3. 번호에 맞는 메서드 출력
    // 4. SQLException 발생 시 초기화
    // 5. 0번을 입력하면 종료
    public void start() throws SQLException {
        System.out.println("==== 도서 관리 프로그램 실행 ====");
        while (true) {
            System.out.println("\n==== 메뉴 (종료 : 0) ====");
            System.out.println("1. 도서 추가  |  2. 전체 도서 목록  |  3. 도서 검색");
            System.out.println("4. 도서 대출  |  5. 대출 기록 확인  |  6. 도서 반납");
            System.out.println("7. 학생 등록  |  8. 전체 학생 조회  |  9. 로그인 ");
            System.out.print("번호를 입력하세요 : ");
            String choice = scanner.nextLine();

            if (choice.equals("4") | choice.equals("6")) {
                if (currentStudent == null | currentStudentId == null | currentStudentName == null) {
                    System.out.println("로그인 후 이용 가능합니다.");
                }
                continue;
            }

            if (choice.equals("1")) {
                System.out.println("추가할 도서의 정보를 입력하세요");
                System.out.print("도서명 : ");
                String title = scanner.nextLine();
                System.out.print("저자명 : ");
                String author = scanner.nextLine();
                System.out.print("출판사 : ");
                String publisher = scanner.nextLine();
                System.out.print("출판년도 : ");
                int publicationYear = scanner.nextInt();
                System.out.print("ISBN : ");
                String isbn = scanner.nextLine();

                Book book = new Book(title, author, publisher, publicationYear, isbn, true);
                libraryService.addBook(book);

            } else if (choice.equals("2")) {
                System.out.println("\n[전체 도서]");
                printBook(libraryService.getAllBook());

            } else if (choice.equals("3")) {
                System.out.print("검색할 도서명을 입력하세요 : ");
                String targetTitle = scanner.nextLine();
                if (!libraryService.getBookByTitle(targetTitle).isEmpty()) {

                    System.out.println("\n[검색 결과]");
                    printBook(libraryService.getBookByTitle(targetTitle));
                }
            } else if (choice.equals("4")) {
                System.out.print("대출할 도서의 ID를 입력하세요 : ");
                int targetBookId = scanner.nextInt();
                libraryService.borrowBook(targetBookId, currentStudentId);
                System.out.println("대출이 완료되었습니다.");

            } else if (choice.equals("5")) {
                System.out.println("\n[대출 기록 열람]");
                printBorrow(libraryService.getBorrowedBook());

            } else if (choice.equals("6")) {
                System.out.print("반납할 도서의 ID를 입력하세요 : ");
                int targetBookId = scanner.nextInt();
                libraryService.returnBook(targetBookId, currentStudentId);
                System.out.println("반납이 완료되었습니다.");
            } else if (choice.equals("7")) {
                System.out.println("등록할 학생의 정보를 입력하세요");
                System.out.print("이름 : ");
                String name = scanner.nextLine();
                System.out.print("학번 : ");
                String studentId = scanner.nextLine();
                Student student = new Student(name, studentId);
                libraryService.addStudent(student);
            } else if (choice.equals("8")) {
                System.out.println("\n[전체 학생 조회]");
                printStudnet(libraryService.getAllStudent());
            } else if (choice.equals("9")) {
                System.out.print("학번으로 로그인 : ");
                String loginId = scanner.nextLine();
                currentStudentName = libraryService.getStudentById(loginId).getName();
                currentStudentId = libraryService.getStudentById(loginId).getId();
                currentStudent = libraryService.getStudentById(loginId);
                System.out.println(currentStudentName + "님, 환영합니다.");
            } else if (choice.equals("0")) {
                currentStudent = null;
                currentStudentId = null;
                currentStudentName = null;
                System.out.println("자동으로 로그아웃 되었습니다.");
                System.out.println("==== 도서 관리 프로그램 종료 ====");
                return;
            } else {
                System.out.println();
                System.err.println("잘못된 번호입니다.");
            }

        }
    }
}
