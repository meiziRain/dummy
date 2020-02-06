package com.meizi.admin;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;

/**
 * @program: dummy
 * @description:
 * @author: Meizi
 * @create: 2020-01-03 16:24
 **/
public class Demo {
    @Value("${mybatis-plus.mapper-locations}")
    static String classpath;
    @Value("#{11*2}")
    static int i;

    @Test
    public void demo() {
        ArrayList list=new ArrayList();
        System.out.println(list==null);
    }

}
