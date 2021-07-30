package com.changgou.oauth;


import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    @Test
    public void testParseToken()
    {
        String token="eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJlM2IwZGVlMS0xOTA0LTRiMjMtODk5ZS0zYjZkN2U5MjVkYzkiLCJzdWIiOiJ7XCJyb2xlXCI6XCJVU0VSXCIsXCJzdWNjZXNzXCI6XCJTVUNDRVNTXCIsXCJ1c2VybmFtZVwiOlwiY2hhbmdnb3VcIn0iLCJpc3MiOiJhZG1pbiIsImlhdCI6MTYyNzUyMjI4NCwiZXhwIjoxNjI3NTI1ODg0fQ.O7M88NBnFuFcWJWBCcuxqwri87ka_JF5A_M1UmygYF0";
        String publickey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmOMYXsb2YvHWxiRH3H3CSeGBJaDkfOqxcmYQYJbl6N+5G/Pt7rIJCccG5hp5DBOW6FcSjnUR5z06DSJiBKNdtTpLVHkrRMjPOJYaknjgBue1xs3U36WjtEVkmwMreu8MjHPIgd5OZcLOuwra3W/BIt3KqhbkFxCsteSJwtyZ5Rm/8Jc+Uh34xfPGfEkqQn6qbYXUPx5OgAnJiowWM4Mccus3O0lazriOIYNuyys8lFPEIqitgHVNI+zGPAhwgQEF7M6CK78JtaWPfXvdypjjd0YoQmk/tWjKLlrtxTZWUbIMOV9Ym/rDZAy5KBZA3TvKC3vs4rIb6evcERcpOdp0kQIDAQAB-----END PUBLIC KEY-----";
        Jwt jwt= JwtHelper.decodeAndVerify(token,new RsaVerifier(publickey));
        String claims=jwt.getClaims();
        System.out.println(claims);

        String encoded=jwt.getEncoded();
        System.out.println(encoded);
    }
}
