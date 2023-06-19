package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.MethodEntity;

import java.util.List;

public interface MethodMapper {
    int insertMethod(List<MethodEntity> method);
}
