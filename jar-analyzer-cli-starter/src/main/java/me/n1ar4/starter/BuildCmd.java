package me.n1ar4.starter;

import com.beust.jcommander.Parameter;

@SuppressWarnings("unused")
public class BuildCmd {
    @Parameter(names = {"-j", "--jar"}, description = "jar file or jars dir")
    private String jar;

    @Parameter(names = {"-o","--output"},description = "output sqlite filename")
    private String output;

    @Parameter(names = {"--delete-exist"},description = "delete exist database")
    private boolean deleteExist;

    public String getJar() {
        return jar;
    }

    public String getOutput() {
        return output;
    }

    public boolean isDeleteExist() {
        return deleteExist;
    }
}
