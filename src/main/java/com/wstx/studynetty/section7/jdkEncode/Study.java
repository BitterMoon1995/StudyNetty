package com.wstx.studynetty.section7.jdkEncode;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;

@Slf4j
public class Study {
    @Test
    public void test() throws IOException {
        Girl girl = new Girl("小骚", 20, 8.0d);

        ObjectOutputStream oOs = new ObjectOutputStream(new FileOutputStream("E:\\person.txt"));
        oOs.writeObject(girl);
        oOs.close();

    }
}
