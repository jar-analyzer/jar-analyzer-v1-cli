package me.n1ar4.analyze;

import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

@SuppressWarnings("all")
public class member_util {
    public static String access_to_str(int access) {
        StringBuilder accessStr = new StringBuilder();
        if (Modifier.isPublic(access)) {
            accessStr.append("public ");
        }
        if (Modifier.isPrivate(access)) {
            accessStr.append("private ");
        }
        if (Modifier.isProtected(access)) {
            accessStr.append("protected ");
        }
        if (Modifier.isStatic(access)) {
            accessStr.append("static ");
        }
        if (Modifier.isFinal(access)) {
            accessStr.append("final ");
        }
        if (Modifier.isSynchronized(access)) {
            accessStr.append("synchronized ");
        }
        if (Modifier.isVolatile(access)) {
            accessStr.append("volatile ");
        }
        if (Modifier.isTransient(access)) {
            accessStr.append("transient ");
        }
        if (Modifier.isNative(access)) {
            accessStr.append("native ");
        }
        if (Modifier.isAbstract(access)) {
            accessStr.append("abstract ");
        }
        if (Modifier.isStrict(access)) {
            accessStr.append("strict ");
        }
        if ((access & Opcodes.ACC_SYNTHETIC) != 0) {
            accessStr.append("synthetic ");
        }
        if ((access & Opcodes.ACC_ENUM) != 0) {
            accessStr.append("enum ");
        }
        return accessStr.toString().trim();
    }
}
