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
    private static final Path dbPath = Paths.get("jar-analyzer.db");

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
        try {
            Files.delete(dbPath);
        } catch (Exception ignored) {
        }
        Runner.run(jarPath);
    }
}
