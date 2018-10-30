package com.excelib;

import javassist.*;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class PerfMonXformer implements ClassFileTransformer {
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        byte[] transformed = null;
        System.out.println("Transforming "+className);
        ClassPool pool = ClassPool.getDefault();
        CtClass cl = null;
        try {
            cl = pool.makeClass(new ByteArrayInputStream(classfileBuffer));
            if (!cl.isInterface()){
                CtMethod[] methods = cl.getDeclaredMethods();
                for (CtMethod method:methods) {
                    if (!method.isEmpty()){
                        //修改method字节码
                        doMethod(pool,methods[1]);
                    }
                }
            }
            transformed = cl.toBytecode();
        }catch (Exception ex){
            System.err.println("Could not instrument "+className+", exception:"+ ex);
        } finally {
            if (cl!=null){
                cl.detach();
            }
        }
        return transformed;
    }

    private void doMethod(ClassPool pool, CtMethod method) throws CannotCompileException, NotFoundException {
        method.addLocalVariable("startTime",pool.get("long"));
        method.addLocalVariable("endTime",pool.get("long"));
        method.insertBefore("startTime = System.nanoTime();");
        method.insertAfter("endTime = System.nanoTime();");
        method.insertAfter("System.out.println(\"leave "+method.getName()+" and time:\"+(endTime-startTime));");
    }

    @Test
    public void testModify() throws Exception {
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.get("com.excelib.Student");
        CtMethod method = ctClass.getDeclaredMethod("showMe");
        method.addLocalVariable("startTime",pool.get("long"));
        method.addLocalVariable("endTime",pool.get("long"));
        method.insertBefore("startTime = System.nanoTime();");
        method.insertAfter("endTime = System.nanoTime();");
        method.insertAfter("System.out.println(\"leave "+method.getName()+" and time:\"+(endTime-startTime));");
        System.out.println(method.getMethodInfo());
        byte[] byteArr = ctClass.toBytecode();
        FileOutputStream fos = new FileOutputStream(new File("E:\\source\\springtest\\testagent\\target\\classes\\com\\excelib\\Student1.class"));
        fos.write(byteArr);
        fos.close();
    }
}
