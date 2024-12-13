package pl.dawid0604.pcForum.service.dao.impl.session;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.dawid0604.pcForum.service.dao.session.SpringSessionDaoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpringSessionDaoServiceImpl implements SpringSessionDaoService {
    private final JdbcTemplate jdbcTemplate;
    private static final String COUNT_QUERY = """
        SELECT PRINCIPAL_NAME
        FROM SPRING_SESSION
        WHERE LAST_ACCESS_TIME >= (UNIX_TIMESTAMP() * 1000) - (15 * 60 * 1000)
    """;

    @Override
    public List<String> findOnlineUsers() {
        return jdbcTemplate.query(COUNT_QUERY, (_rs, _rowNum) -> _rs.getString("PRINCIPAL_NAME"));
    }
}
