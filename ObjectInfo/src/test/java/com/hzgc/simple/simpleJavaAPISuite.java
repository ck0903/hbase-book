package com.hzgc.simple;

import org.junit.Test;

import java.io.File;

public class simpleJavaAPISuite {

    @Test
    public void testDemo(){
        System.out.println(System.getProperty("user.dir")  + File.separator);
    }
}
