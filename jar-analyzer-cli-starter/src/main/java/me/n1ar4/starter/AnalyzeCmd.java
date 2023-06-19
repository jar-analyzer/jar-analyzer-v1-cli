package me.n1ar4.starter;

import com.beust.jcommander.Parameter;

public class AnalyzeCmd {
    @Parameter(names = {"-f", "--file"}, description = "analyze py file")
    private String pyFile;
    @Parameter(names = {"-d", "--db"}, description = "database file")
    private String dbFile;
    @Parameter(names = {"-i", "--input"}, description = "input params")
    private String input;
    @Parameter(names = {"-o", "--output"}, description = "output file")
    private String out;
    @Parameter(names = {"-s", "--script"}, description = "builtin script file")
    private String script;

    @Parameter(names = {"-l", "--list"}, description = "list search modules")
    private boolean list;

    public String getPyFile() {
        return pyFile;
    }

    public String getDbFile() {
        return dbFile;
    }

    public String getOut() {
        return out;
    }

    public String getInput() {
        return input;
    }

    public String getScript() {
        return script;
    }

    public boolean isList() {
        return list;
    }
}
