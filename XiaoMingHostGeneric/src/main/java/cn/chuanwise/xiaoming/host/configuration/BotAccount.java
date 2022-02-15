package cn.chuanwise.xiaoming.host.configuration;

import cn.chuanwise.util.MessageDigests;
import lombok.*;

import java.util.Objects;

/**
 * 机器人 QQ 账号密码，只存储密码的 MD5
 * @author Chuanwise
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BotAccount {
    long qq = 0;
    byte[] md5;
    String password;

    public void setPassword(String password) {
        this.password = password;
        if (Objects.nonNull(password)) {
            md5 = MessageDigests.MD5.digest(password.getBytes());
        }
    }

    public BotAccount(long qq, String password) {
        setQq(qq);
        setPassword(password);
    }

    public BotAccount(long qq, byte[] md5) {
        setQq(qq);
        setMd5(md5);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BotAccount that = (BotAccount) o;
        return qq == that.getQq();
    }

    @Override
    public int hashCode() {
        return Objects.hash(qq);
    }
}
