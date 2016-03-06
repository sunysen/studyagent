package com.blueware.agent;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;

//定义扫描待修改class的visitor，visitor就是访问者模式
public class TimeClassVisitor extends ClassVisitor {
    private String className;
    private String annotationDesc;

    public TimeClassVisitor(ClassVisitor cv, String className) {
        super(Opcodes.ASM5, cv);
        this.className = className;
    }

    @Override public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        this.annotationDesc = desc;
        return super.visitAnnotation(desc, visible);
    }

    //扫描到每个方法都会进入，参数详情下一篇博文详细分析
    @Override public MethodVisitor visitMethod(int access, final String name, final String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (annotationDesc != null && annotationDesc.equals(Type.getDescriptor(ExclusiveTime.class))) {
            final String key = className + name + desc;
            //过来待修改类的构造函数
            if (!name.equals("<init>") && mv != null) {
                mv = new AdviceAdapter(Opcodes.ASM5, mv, access, name, desc) {
                    //方法进入时获取开始时间
                    @Override public void onMethodEnter() {
                        //相当于com.blueware.agent.TimeUtil.setStartTime("key");
                        this.visitLdcInsn(key);
                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/TimeUtil", "setStartTime", "(Ljava/lang/String;)V", false);
                    }

                    //方法退出时获取结束时间并计算执行时间
                    @Override public void onMethodExit(int opcode) {
                        //相当于com.blueware.agent.TimeUtil.setEndTime("key");
                        this.visitLdcInsn(key);
                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/TimeUtil", "setEndTime", "(Ljava/lang/String;)V", false);
                        //向栈中压入类名称
                        this.visitLdcInsn(className);
                        //向栈中压入方法名
                        this.visitLdcInsn(name);
                        //向栈中压入方法描述
                        this.visitLdcInsn(desc);
                        //相当于com.blueware.agent.TimeUtil.getExclusiveTime("com/blueware/agent/TestTime","testTime");
                        this.visitMethodInsn(Opcodes.INVOKESTATIC, "com/blueware/agent/TimeUtil", "getExclusiveTime", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                                false);
                    }
                };
            }
        }
        return mv;
    }
}
