package cn.chuanwise.xiaoming.test;

public class FreeMemoryTest {
    public static void main(String[] args) {
        final double l = (double) Runtime.getRuntime().freeMemory() / Runtime.getRuntime().maxMemory();
        System.out.println(l);
        System.out.println(l / 1024);
        System.out.println(l / 1024 / 1024);
        System.out.println(l / 1024 / 1024 / 1024);
    }
}
