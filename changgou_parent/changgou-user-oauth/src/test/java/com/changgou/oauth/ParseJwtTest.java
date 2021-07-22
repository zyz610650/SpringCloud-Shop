package com.changgou.oauth;


import org.junit.Test;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

public class ParseJwtTest {

    @Test
    public void testParseToken()
    {
        String token="eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlcyI6IlJPTEVfVklQLFJPTEVfVVNFUiIsIm5hbWUiOiJpdGhlaW1hIiwiaWQiOjF9.OadnyKlXfP8v5yDehK_VOwmQdL8b-lp4ZwcXCmg4jpFoOzfsRJHv1tdNrN-Q217ouMyveNFb1UlulGFGPVESBwlG20SM0wh0T5Inu2sErOsy7OVjjn0BPV0sJWgb1pYmUbwRg3TcNgwf7svlPOI_EX8nKS8X-xQ8HPYveJjcWnXAxZ6m5FZZ7s8-P2osvWYniu4xxq8SPUJuQyMQEKVS0LWKllN07qerWBPocU_2J_HS9yq9DRdOFyZNdLqSb37eoa7EI-pyVtL6kgMf6zpN6fQ2AqjHc53FGB3e-vIwfGY3WHuxNRK-lwSZrAJYTNURjrapEt8GB3bAdU30hKFjag";
        String publickey="-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmOMYXsb2YvHWxiRH3H3CSeGBJaDkfOqxcmYQYJbl6N+5G/Pt7rIJCccG5hp5DBOW6FcSjnUR5z06DSJiBKNdtTpLVHkrRMjPOJYaknjgBue1xs3U36WjtEVkmwMreu8MjHPIgd5OZcLOuwra3W/BIt3KqhbkFxCsteSJwtyZ5Rm/8Jc+Uh34xfPGfEkqQn6qbYXUPx5OgAnJiowWM4Mccus3O0lazriOIYNuyys8lFPEIqitgHVNI+zGPAhwgQEF7M6CK78JtaWPfXvdypjjd0YoQmk/tWjKLlrtxTZWUbIMOV9Ym/rDZAy5KBZA3TvKC3vs4rIb6evcERcpOdp0kQIDAQAB-----END PUBLIC KEY-----";
        Jwt jwt= JwtHelper.decodeAndVerify(token,new RsaVerifier(publickey));
        String claims=jwt.getClaims();
        System.out.println(claims);

        String encoded=jwt.getEncoded();
        System.out.println(encoded);
    }
}
