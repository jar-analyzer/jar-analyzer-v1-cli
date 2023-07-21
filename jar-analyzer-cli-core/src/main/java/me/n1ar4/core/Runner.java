package me.n1ar4.core;

import me.n1ar4.core.asm.StringClassVisitor;
import me.n1ar4.db.entity.ClassFileEntity;
import me.n1ar4.util.CoreUtil;
import me.n1ar4.util.DirUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Runner {
    private static final Logger logger = LogManager.getLogger();

    public static void run(Path jarPath) {
        List<ClassFileEntity> cfs;
        if (Files.isDirectory(jarPath)) {
            logger.info("你输入的是一个Jar目录");
            List<String> files = DirUtil.GetFiles(jarPath.toAbsolutePath().toString());
            for (String s : files) {
                DB.saveJar(s);
            }
            cfs = CoreUtil.getAllClassesFromJars(files);
        } else {
            logger.info("你输入的是一个Jar文件");
            List<String> jarList = new ArrayList<>();
            jarList.add(jarPath.toAbsolutePath().toString());
            DB.saveJar(jarList.get(0));
            cfs = CoreUtil.getAllClassesFromJars(jarList);
        }
        Env.classFileList.addAll(cfs);
        logger.info("尝试获取所有Class文件");
        DB.saveClassFiles(Env.classFileList);
        Discovery.start(Env.classFileList, Env.discoveredClasses,
                Env.discoveredMethods, Env.classMap, Env.methodMap);
        DB.saveClassInfo(Env.discoveredClasses);
        DB.saveMethods(Env.discoveredMethods);
        logger.info("基础Class信息收集完毕");
        for (MethodReference mr : Env.discoveredMethods) {
            ClassReference.Handle ch = mr.getClassReference();
            if (Env.methodsInClassMap.get(ch) == null) {
                List<MethodReference> ml = new ArrayList<>();
                ml.add(mr);
                Env.methodsInClassMap.put(ch, ml);
            } else {
                List<MethodReference> ml = Env.methodsInClassMap.get(ch);
                ml.add(mr);
                Env.methodsInClassMap.put(ch, ml);
            }
        }
        MethodCall.start(Env.classFileList, Env.methodCalls);
        Env.inheritanceMap = Inheritance.derive(Env.classMap);
        logger.info("构建继承关系完毕");
        Map<MethodReference.Handle, Set<MethodReference.Handle>> implMap =
                Inheritance.getAllMethodImplementations(Env.inheritanceMap, Env.methodMap);
        DB.saveImpls(implMap);
        logger.info("构建实现关系完毕");
        for (Map.Entry<MethodReference.Handle, Set<MethodReference.Handle>> entry :
                implMap.entrySet()) {
            MethodReference.Handle k = entry.getKey();
            Set<MethodReference.Handle> v = entry.getValue();
            HashSet<MethodReference.Handle> calls = Env.methodCalls.get(k);
            calls.addAll(v);
        }
        DB.saveMethodCalls(Env.methodCalls);
        logger.info("根据继承关系构建额外的方法调用关系完毕");
        for (ClassFileEntity file : Env.classFileList) {
            try {
                StringClassVisitor dcv = new StringClassVisitor(Env.strMap, Env.classMap, Env.methodMap);
                ClassReader cr = new ClassReader(file.getFile());
                cr.accept(dcv, ClassReader.EXPAND_FRAMES);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        DB.saveStrMap(Env.strMap);
    }
}
