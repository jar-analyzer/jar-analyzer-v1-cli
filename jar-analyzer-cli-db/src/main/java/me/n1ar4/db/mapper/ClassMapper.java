package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.ClassEntity;

import java.util.List;

public interface ClassMapper {
    int insertClass(List<ClassEntity> c);
}
