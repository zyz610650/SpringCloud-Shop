package com.changgou.filter;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class URLFilter {

    private static String path="/api/user/add,/api/user/login";

    public static boolean hasAuthorize(String uri)
    {
        String[] urls=path.split(",");
        for (String url:urls)
        {
            if (url.equals(uri)) return true;
        }
        return false;
    }
}
