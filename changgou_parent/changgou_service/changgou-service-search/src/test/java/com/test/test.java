package com.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zyz
 * @title:
 * @seq:
 * @address:
 * @idea:
 */
public class test {
    public static void main(String[] args) {
        List<?> list=new ArrayList<>();

        List list1=new ArrayList<>();
        list1.add(2);
        list1.add("2");
        for (Iterator i = list1.iterator();i.hasNext();)
        {
            i= (Iterator) i.next();
        }
    }
}
