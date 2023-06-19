package me.n1ar4.starter;

import com.beust.jcommander.Parameter;

public class BuildCmd {
    @Parameter(names = {"-j", "--jar"}, description = "jar file or jars dir")
    private String jar;

    public String getJar() {
        return jar;
    }
}
