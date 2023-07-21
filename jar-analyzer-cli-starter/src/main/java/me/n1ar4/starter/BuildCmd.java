package me.n1ar4.starter;

import com.beust.jcommander.Parameter;

@SuppressWarnings("unused")
public class BuildCmd {
    @Parameter(names = {"-j", "--jar"}, description = "Jar文件或者Jar目录")
    private String jar;
    @Parameter(names = {"--delete-exist"}, description = "是否删除旧数据库")
    private boolean deleteExist;
    @Parameter(names = {"--del-cache"}, description = "是否删除缓存文件")
    private boolean delCache;

    public String getJar() {
        return jar;
    }

    public boolean isDeleteExist() {
        return deleteExist;
    }

    public boolean isDelCache() {
        return delCache;
    }
}
