package me.n1ar4.core;

import me.n1ar4.core.asm.DiscoveryClassVisitor;
import me.n1ar4.db.entity.ClassFileEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import java.util.Map;
import java.util.Set;

public class Discovery {
    private static final Logger logger = LogManager.getLogger(Discovery.class);

    public static void start(Set<ClassFileEntity> classFileList,
                             Set<ClassReference> discoveredClasses,
                             Set<MethodReference> discoveredMethods,
                             Map<ClassReference.Handle, ClassReference> classMap,
                             Map<MethodReference.Handle, MethodReference> methodMap) {
        logger.info("开始初步Class信息分析与收集");
        for (ClassFileEntity file : classFileList) {
            try {
                DiscoveryClassVisitor dcv = new DiscoveryClassVisitor(discoveredClasses,
                        discoveredMethods, file.getJarName());
                ClassReader cr = new ClassReader(file.getFile());
                cr.accept(dcv, ClassReader.EXPAND_FRAMES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (ClassReference clazz : discoveredClasses) {
            classMap.put(clazz.getHandle(), clazz);
        }
        for (MethodReference method : discoveredMethods) {
            methodMap.put(method.getHandle(), method);
        }
    }
}
