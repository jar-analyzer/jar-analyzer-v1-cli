package me.n1ar4.analyze;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class db_instance {
    private SqlSession session;
    private static final Logger logger = LogManager.getLogger(db_instance.class);
    private SqlMapper sqlMapper;

    public db_instance() {
        String resource = "mybatis-raw.xml";
        try {
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            sqlMapper = session.getMapper(SqlMapper.class);
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    public void close() {
        this.session.close();
    }

    public List<Map<String, Object>> execute_sql(String sql) {
        return sqlMapper.executeSql(sql);
    }
}
