package com.function.ianchang.devtools;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by martin.du on 2017/9/21.
 */

public class SignUtils {
    private static final String RSA_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALX4Lo/0juTNZR9RFFvTmvZ1DaTdLE2HWS5k+izx1RNrTbd7Vg7/slYYhmRY/TcU1J9/Tdb9KChE9TSEMQPU1LpbaJhT5BxACulkLZDj2FufubhzIgbCYdBFkFy3xW0BVvTjLaIQDY4Qr1thg97KRa8ZiJ85mw/bt2NQQVnzAwUxAgMBAAECgYA2cMzFZr5Fd6m0R9aWbmVjLLvrQfxaKY8l0TIDtkowKB81MKIblKYvGeFDLlh7XTELktOf6VdVTOQrVQ0w/0CxCeyhSUfxATamgEcmpxPdqmbVtSkzBoGy8kaAGUCB672b2gPcJ5V21XloY+RfOVNakeb0R55C6vLdpLBblNfNwQJBAPipyKAsy5XtH5cEuUvkGnit0gqnGt3dfU1Pou9OJRdNjoSEjlYPIqTfqCkJIbdH/B8NdU9JuZgJSZeSwUx1p/kCQQC7VqSx4ptfRIE1NjneCPPXVGN9c0E78n1MqF0jDrjCQm+vGw8B4Atqw350hEFaqftY/Z9dWh32NIyZTp0tuMT5AkEA6mSgiNuwzBJIxMHfKHpLuZWfeAsseBZgFpAKtiijLeQdgyywPs7liSSKDqRc87cXIO4+tg54s6eNhyL+smP1gQJBAJYYSUQspwehP1R+6cY3rgZsGno8iZuaIUH18wlPlkAuMoU9TLzX4M3da8e23xXg8vzN141X0oGcgLmj/tLPIyECQGHCODCsAuZbKLkhUYPVbtebdqJgizbfte/YqbVj1ywQrmZh+3AV8uTyvnSa5Kv5VB2G8yjM5B/iyMLyP6nzQ68=";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content) {
        return sign(content, RSA_KEY);
    }

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return new String(Base64.encode(signed, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
