package com.blueware.agent;

import java.lang.instrument.Instrumentation;

public class StartAgent {
    //代理程序入口函数
    public static void premain(String args, Instrumentation inst) {
        //添加字节码转换器
        inst.addTransformer(new PrintTimeTransformer());
    }
}
