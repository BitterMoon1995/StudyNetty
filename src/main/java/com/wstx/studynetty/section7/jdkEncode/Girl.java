package com.wstx.studynetty.section7.jdkEncode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Girl implements Serializable {
    String name;
    int age;
    double score;
}
