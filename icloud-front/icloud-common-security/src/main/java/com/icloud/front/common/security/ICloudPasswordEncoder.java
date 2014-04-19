package com.icloud.front.common.security;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.icloud.front.common.utils.StringEncoder;
import com.travelzen.framework.core.util.TZUtil;

@Component("icloudPasswordEncoder")
public class ICloudPasswordEncoder implements PasswordEncoder {

    @Override
    public String encodePassword(String rawPass, Object salt) {
        return StringEncoder.encrypt(rawPass);
    }

    @Override
    public boolean isPasswordValid(String encPass, String rawPass, Object salt) {
        if (TZUtil.isEmpty(rawPass)) {
            return false;
        }
        return StringEncoder.encrypt(rawPass).equals(encPass);
    }
}
