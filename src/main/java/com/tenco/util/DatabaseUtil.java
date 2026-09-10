package com.tenco.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/library?serverTimezone=Asia/Seoul";
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");

    private static final HikariDataSource dataSource;

    // static 블록은 이 클래스가 처음 사용되는 순간에만 한 번 실행
    // 즉, 첫 getConnection() 호출 시에 생성되고 그 후로는 재사용


    static {
        HikariConfig config = new HikariConfig();

        // 1. 기본 연결 정보 설정
        config.setJdbcUrl(URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);

        // 2. 커넥션 풀 크기 설정
        config.setMaximumPoolSize(10); // 동시 최대 10개 연결
        config.setMinimumIdle(5); // 요청이 없어도 최소 5개는 준비 상태

        // 3. 풀이 가득 찼을 때 빈 연결을 기다리는 최대 시간 (밀리초)
        config.setConnectionTimeout(3000);

        // 4. 풀 생성(실제 DB 연결 객체 N개가 생성되는 시점)
        dataSource = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // 프로그램 종료 시 전체 풀 정리
    public static void close() {
        if (!dataSource.isClosed()) {
            close();
        }
    }
}
