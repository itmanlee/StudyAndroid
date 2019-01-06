package com.wangkeke.daggeruse;

import javax.inject.Inject;

public class Student {

    @Inject
    public Student() {
    }

    public String getDefaultInfo(){
        return "我是一个躲在窗外旁听的学生，我没有参数，直接inject我就可以了";
    }
}
