package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.MethodImplEntity;

import java.util.List;

public interface MethodImplMapper {
    int insertMethodImpl(List<MethodImplEntity> impl);
}
