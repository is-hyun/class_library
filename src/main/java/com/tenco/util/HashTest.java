package com.tenco.util;

import org.mindrot.jbcrypt.BCrypt;

public class HashTest {
    public static void main(String[] args) {

        String password = "admin123";

        // 1. 같은 비밀번호를 각각 두 번 해시하면 결과가 다름 (솔트가 매번 다르기 때문)
        String hash1 = BCrypt.hashpw(password, BCrypt.gensalt(10));
        String hash2 = BCrypt.hashpw(password, BCrypt.gensalt(10));

        System.out.println(hash1); // >> $2a$10$8EhyQM52RWQKUEkRZQe4yezZQnGCgaeaicfCth4eFSsy5Ttkc/ivy
        System.out.println(hash2); // >> $2a$10$azb2IQ2mgznewMnF1pB3XOt1J51hKn7WZdWwbY9xsFfT/zFeSl7Wq
        System.out.println("두 해시 비교 : " + hash1.equals(hash2));

        // 2. 원문을 비교하면 동일
        System.out.println(BCrypt.checkpw(password, hash1));
        System.out.println(BCrypt.checkpw(password, hash2));

        // 3. 틀린 비밀번호는 거부
        System.out.println(BCrypt.checkpw("12345", hash1)); // >> false

        // 4. 의도적으로 느리게 진행. 비용을 올리면 두 배씩 느려짐
        for (int cost = 10; cost <= 12; cost++) {
            long start = System.nanoTime();
            BCrypt.hashpw(password, BCrypt.gensalt(cost));
            long ms = (System.nanoTime() - start) / 1000000;
            System.out.println("비용 : " + cost + " > " + ms + "ms");
        }
    }
}
