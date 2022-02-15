package cn.chuanwise.xiaoming.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggerTest {
    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            log.info("qwq!" + i);
        }
    }
}
