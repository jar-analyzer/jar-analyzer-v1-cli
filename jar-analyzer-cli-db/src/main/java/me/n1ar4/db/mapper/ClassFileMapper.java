package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.ClassFileEntity;

import java.util.List;

public interface ClassFileMapper {
    int insertClassFile(List<ClassFileEntity> ct);
}
