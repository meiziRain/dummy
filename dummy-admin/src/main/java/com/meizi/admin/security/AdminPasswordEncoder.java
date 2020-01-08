package com.meizi.admin.security;

import com.meizi.admin.constants.DefaultSecurityConstants;
import com.meizi.admin.util.SmCipher;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * 自定义密码加密
 *
 * @author TianLei
 */
public class AdminPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        if (Objects.isNull(rawPassword) || rawPassword.length() == 0) {
            return "";
        }
        return this.encrypt(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (Objects.isNull(rawPassword) || rawPassword.length() == 0 || Objects.isNull(encodedPassword) || encodedPassword.length() == 0) {
            return false;
        }
        String decrypt = this.decrypt(encodedPassword);
        return rawPassword.equals(decrypt);
    }

    private String decrypt(String decrypt){
        if(StringUtils.isBlank(decrypt)){
            return "";
        }
        return SmCipher.instance().sm4().decrypt(DefaultSecurityConstants.SM4_ENCRYPT_ECB_KEY, decrypt);
    }

    private String encrypt(String encrypt){
        if(StringUtils.isBlank(encrypt)){
            return "";
        }
        return SmCipher.instance().sm4().encrypt(DefaultSecurityConstants.SM4_ENCRYPT_ECB_KEY, encrypt);
    }
}
