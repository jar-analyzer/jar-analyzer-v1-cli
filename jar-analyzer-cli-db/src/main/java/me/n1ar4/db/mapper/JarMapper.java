package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.JarEntity;

import java.util.List;

public interface JarMapper {
    int insertJar(List<JarEntity> jar);
}
