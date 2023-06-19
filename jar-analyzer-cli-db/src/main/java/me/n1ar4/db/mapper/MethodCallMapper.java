package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.MethodCallEntity;

import java.util.List;

public interface MethodCallMapper {
    int insertMethodCall(List<MethodCallEntity> mce);
}
