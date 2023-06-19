package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.StringEntity;

import java.util.List;

public interface StringMapper {
    int insertString(List<StringEntity> str);
}
