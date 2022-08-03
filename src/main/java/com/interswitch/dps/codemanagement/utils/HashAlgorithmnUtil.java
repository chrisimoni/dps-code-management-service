package com.interswitch.dps.codemanagement.utils;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public class HashAlgorithmnUtil {
    private HmacUtils hm256;

    public HashAlgorithmnUtil(String key) {
        hm256 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, key.getBytes());
    }
    public String hmac256(String valueToDigest) {
        return hm256.hmacHex(valueToDigest);
    }
}
