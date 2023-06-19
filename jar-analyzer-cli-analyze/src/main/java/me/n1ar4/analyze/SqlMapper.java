package me.n1ar4.analyze;

import java.util.List;
import java.util.Map;

public interface SqlMapper {
    List<Map<String,Object>>executeSql(String sql);
}
