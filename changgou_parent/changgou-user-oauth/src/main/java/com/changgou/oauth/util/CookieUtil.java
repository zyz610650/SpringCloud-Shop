package com.changgou.oauth.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2018/3/18.
 */
public class CookieUtil {

    public static void addCookie(HttpServletResponse response,String domain,String path,String name,String value,int maxAge,boolean httpOnly)
    {
        Cookie cookie=new Cookie(name,value);
        cookie.setPath(path);
        cookie.setDomain(domain);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static Map<String,String> readCookie(HttpServletRequest request,String ... cookieNames)
    {
        Map<String,String> map=new HashMap<>();
        Cookie[] cookies=request.getCookies();
        if (cookies==null) return map;

            for (int i=0;i<cookieNames.length;i++)
            {
                for (int j=0;j<cookies.length;j++)
                {
                    if (cookieNames[i]==cookies[j].getName())
                    {
                        map.put(cookieNames[i],cookies[j].getValue());
                    }
                }
            }
        return map;
    }
}
