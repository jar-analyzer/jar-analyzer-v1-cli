package me.n1ar4.starter;

import com.beust.jcommander.JCommander;
import me.n1ar4.analyze.AnalyzerRunner;
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
    private static final AnalyzeCmd analyzeCmd = new AnalyzeCmd();

    private static final Path dbPath = Paths.get("jar-analyzer.db");

    public static void main(String[] args) {
        Logo.printLogo();
        Application main = new Application();
        JCommander commander = JCommander.newBuilder()
                .addObject(main)
                .addCommand("build", buildCmd)
                .addCommand("analyze", analyzeCmd)
                .build();
        commander.parse(args);
        if (StringUtil.isNull(commander.getParsedCommand())) {
            logger.error("command is none");
            return;
        }
        if (commander.getParsedCommand().equals("build")) {
            main.build();
        } else if (commander.getParsedCommand().equals("analyze")) {
            main.analyze();
        }
    }

    private void analyze() {
        if(analyzeCmd.isList()){
            AnalyzerRunner.printList();
            return;
        }
        Path pyPath;
        if (StringUtil.isNull(analyzeCmd.getPyFile())) {
            pyPath = null;
            if (StringUtil.isNull(analyzeCmd.getScript())) {
                logger.error("please input script or file");
                return;
            }
        } else {
            pyPath = Paths.get(analyzeCmd.getPyFile());
        }
        if (StringUtil.isNull(analyzeCmd.getDbFile())) {
            logger.error("please input db file");
            return;
        }
        Path dbPath = Paths.get(analyzeCmd.getDbFile());
        if (!Files.exists(dbPath)) {
            logger.error("database file not exist");
            return;
        }
        AnalyzerRunner.start(pyPath, dbPath, analyzeCmd.getScript(),
                analyzeCmd.getInput(), analyzeCmd.getOut());
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
