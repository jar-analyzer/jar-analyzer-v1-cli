package me.n1ar4.db.mapper;

import me.n1ar4.db.entity.MemberEntity;

import java.util.List;

public interface MemberMapper {
    int insertMember(List<MemberEntity> Member);
}
