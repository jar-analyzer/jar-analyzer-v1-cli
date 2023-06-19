package me.n1ar4.analyze;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.python.util.PythonInterpreter;

import java.io.*;
import java.nio.file.Path;
import java.util.Objects;
import java.util.jar.JarInputStream;
import java.util.logging.Level;

public class AnalyzerRunner {
    private static final Logger logger = LogManager.getLogger(AnalyzerRunner.class);

    public static void start(Path pyPath, Path dbPath, String script, String input, String out) {
        java.util.logging.Logger.getLogger("org.python").setLevel(Level.OFF);

        PythonInterpreter interpreter = new PythonInterpreter();

        interpreter.set("input", input);
        interpreter.set("output", out);

        if(pyPath != null){
            String absPath = pyPath.toAbsolutePath().toString();
            logger.info("analyze script file: {}", absPath);
            logger.info("run script: {}", absPath);
            interpreter.execfile(absPath);
            interpreter.close();
            logger.info("close python interpreter success");
            return;
        }

        logger.info("analyze database file: {}", dbPath.toAbsolutePath().toString());


        String resourceName = String.format("scripts/%s.py", script);
        InputStream in = AnalyzerRunner.class.getClassLoader().getResourceAsStream(resourceName);
        if (in == null) {
            logger.error("script not exist");
            return;
        }

        logger.info("run script: {}", script);
        interpreter.execfile(in);
        interpreter.close();
        logger.info("close python interpreter success");
    }

    public static void printList() {
        // http://www.vandermeer.de/projects/skb/java/asciitable/
        AsciiTable at = new AsciiTable();
        at.addRule();

        at.addRow("module-name", "params");
        at.addRule();

        at.addRow("search-class-name", "[class-name]");
        at.addRule();

        at.addRow("search-class-member", "[member-name]|[member-type]");
        at.addRule();

        at.addRow("search-member-in-class", "[class-name]");
        at.addRule();

        at.addRow("search-method-callee", "[callee-class-name]|[callee-method-name]|[optional-desc]");
        at.addRule();

        at.addRow("search-method-caller", "[caller-class-name]|[caller-method-name]|[optional-desc]");
        at.addRule();

        at.addRow("search-method-name", "[method-name]");
        at.addRule();

        at.addRow("search-spring-controller", "none");
        at.addRule();

        at.addRow("search-spring-mapping", "none");
        at.addRule();

        at.addRow("search-string", "[string-name]");
        at.addRule();

        at.addRow("search-string-in-class", "[class-name]");
        at.addRule();

        at.getRenderer().setCWC(new CWC_LongestWordMin(5));
        String rend = at.render();
        System.out.println(rend);
    }
}
