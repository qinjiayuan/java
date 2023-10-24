package clientReview;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

public class ConnectDba {
    private static final Logger log = LoggerFactory.getLogger(Test.class);

    public SqlSession connecttion() {
        try {
            InputStream in = Resources.getResourceAsStream("mybatis-config.xml");
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(in);
            SqlSession session = factory.openSession(true);
            return session;

        } catch (Exception e) {
            log.error("Error connecting to database", e);
        }
        return null;
    }
}
