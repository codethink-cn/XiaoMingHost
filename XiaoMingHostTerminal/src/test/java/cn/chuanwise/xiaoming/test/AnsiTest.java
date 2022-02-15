package cn.chuanwise.xiaoming.test;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class AnsiTest {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        final Ansi reset = Ansi.ansi().eraseScreen().render("@|red qwq").reset();
        System.out.println(reset);
        System.out.println("reset: " + reset.toString());
    }
}