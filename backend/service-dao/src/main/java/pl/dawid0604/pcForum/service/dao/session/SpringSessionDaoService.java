package pl.dawid0604.pcForum.service.dao.session;

import java.util.List;

public interface SpringSessionDaoService {
    List<String> findOnlineUsers();
}
