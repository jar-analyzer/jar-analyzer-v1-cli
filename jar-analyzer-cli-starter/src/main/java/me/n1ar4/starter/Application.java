package me.n1ar4.starter;

import com.beust.jcommander.JCommander;
import me.n1ar4.core.Runner;
import me.n1ar4.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Application {
    private static final Logger logger = LogManager.getLogger(Application.class);
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
            logger.error("command is none");
            return;
        }
        if (commander.getParsedCommand().equals("build")) {
            main.build();
        }
    }

    private void build() {
        if (StringUtil.isNull(buildCmd.getJar())) {
            logger.error("please input jar param");
            return;
        }
        Path jarPath = Paths.get(buildCmd.getJar());
        if (!Files.exists(jarPath)) {
            logger.error("jar file not exist");
            return;
        }
        String outDB = buildCmd.getOutput();
        if (StringUtil.isNull(outDB)) {
            outDB = "jar-analyzer.db";
            logger.info("use default output: jar-analyzer.db");
        } else {
            logger.info("use output: {}", buildCmd.getOutput());
        }
        Path outDBPath = Paths.get(outDB);
        if (Files.exists(outDBPath)) {
            logger.info("output db file exists");
            if (buildCmd.isDeleteExist()) {
                try {
                    Files.delete(outDBPath);
                    logger.info("delete old success");
                } catch (Exception ex) {
                    logger.warn("delete error: {}", ex.toString());
                    return;
                }
            } else {
                logger.warn("stop because db file exists");
                return;
            }
        }
        Runner.run(jarPath, outDBPath);
    }
}
