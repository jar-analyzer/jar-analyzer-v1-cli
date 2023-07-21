package me.n1ar4.starter;

import com.beust.jcommander.JCommander;
import me.n1ar4.core.Runner;
import me.n1ar4.util.DirUtil;
import me.n1ar4.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    private static final Logger logger = LogManager.getLogger();
    private static final BuildCmd buildCmd = new BuildCmd();

    public static void main(String[] args) {
        Logo.printLogo();
        Application main = new Application();
        JCommander commander = JCommander.newBuilder()
                .addObject(main)
                .addCommand("build", buildCmd)
                .build();
        commander.parse(args);
        if (StringUtil.isNull(commander.getParsedCommand())) {
            logger.error("命令不能为空");
            return;
        }
        if (commander.getParsedCommand().equals("build")) {
            main.build();
        }
    }

    private void build() {
        if (Files.exists(Paths.get("jar-analyzer-cli-temp"))) {
            logger.info("旧缓存文件存在");
            if (buildCmd.isDelCache()) {
                try {
                    logger.info("删除旧缓存文件");
                    DirUtil.removeDir(new File("jar-analyzer-cli-temp"));
                } catch (Exception ex) {
                    logger.warn("删除旧缓存文件错误: {}", ex.toString());
                }
            } else {
                logger.warn("请手动删除旧缓存文件或使用--del-cache参数删除");
                return;
            }
        }
        if (StringUtil.isNull(buildCmd.getJar())) {
            logger.error("必须输入jar参数(可以是单个也可以是目录)");
            return;
        }
        Path jarPath = Paths.get(buildCmd.getJar());
        if (!Files.exists(jarPath)) {
            logger.error("输入的jar文件或目录不存在");
            return;
        }
        Path outDBPath = Paths.get("jar-analyzer.db");
        if (Files.exists(outDBPath)) {
            logger.info("输出数据库文件已存在");
            if (buildCmd.isDeleteExist()) {
                try {
                    Files.delete(outDBPath);
                    logger.info("删除旧数据库成功");
                } catch (Exception ex) {
                    logger.warn("删除旧数据库错误: {}", ex.toString());
                    return;
                }
            } else {
                logger.warn("停止运行因为旧的数据库文件存在(--delete-exist)");
                return;
            }
        }
        Runner.run(jarPath);
    }
}
