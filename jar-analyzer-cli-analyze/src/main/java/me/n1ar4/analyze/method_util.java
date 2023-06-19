package me.n1ar4.analyze;

import org.objectweb.asm.Type;

import java.lang.reflect.Modifier;

@SuppressWarnings("all")
public class method_util {
    public static String desc_to_str(String desc) {
        Type type = Type.getMethodType(desc);
        Type[] argTypes = type.getArgumentTypes();
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < argTypes.length; i++) {
            sb.append(argTypes[i].getClassName());
            if (i != argTypes.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(") -> ");
        sb.append(type.getReturnType().getClassName());
        return sb.toString();
    }

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
        if (Modifier.isNative(access)) {
            accessStr.append("native ");
        }
        if (Modifier.isAbstract(access)) {
            accessStr.append("abstract ");
        }
        if (Modifier.isStrict(access)) {
            accessStr.append("strict ");
        }
        return accessStr.toString().trim();
    }
}