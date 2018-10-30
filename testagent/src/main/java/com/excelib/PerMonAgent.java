package com.excelib;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;

public class PerMonAgent {
    private static Instrumentation inst;

    public static void premain(String agentArgs, Instrumentation _inst){
        System.out.println("PerfMonAgent.permain() was called.");
        inst = _inst;
        ClassFileTransformer trans = new PerfMonXformer();
        System.out.println("Adding a PerfMonXformer instance to the JVM.");
        inst.addTransformer(trans);
    }
}
