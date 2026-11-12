package org.example.WebSocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.ViolationAlertMapper;
import org.example.pojo.Student;
import org.example.pojo.ViolationAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint("/disciplinaryNotice/{id}")
@Component
public class DisciplinaryNoticeWebSocket {
    private static final ConcurrentHashMap<String, Session> map = new ConcurrentHashMap<>();

    private static ViolationAlertMapper violationAlertMapper;
    @Autowired
    public void setViolationAlertMapper(ViolationAlertMapper mapper) {
        DisciplinaryNoticeWebSocket.violationAlertMapper = mapper;
    }

    @OnOpen
    public void onOpen(@PathParam("id") Integer id, Session session) {
        map.put(id.toString(), session);
        log.info("用户{}已连接", id);

        // 1. 只查未读消息
        List<ViolationAlert> violationAlerts = violationAlertMapper.selectHasAlert(id);
        if(violationAlerts != null && !violationAlerts.isEmpty()){
            log.info("用户{}有{}条未读违纪通知", id, violationAlerts.size());
            for (ViolationAlert alert : violationAlerts) {
                String msg = alert.getStudentName() + "同学在" + alert.getCreateTime()
                        + "违纪扣除" + alert.getViolationScore() + "分，请及时教导处理";

                // 2. 直接通过当前的 session 发，千万别再调 sendMessage 了！
                session.getAsyncRemote().sendText(msg);

                // 3. 发完直接删掉数据库记录
                violationAlertMapper.deleteViolationAlert(alert);
            }
        }
    }

    @OnMessage
    public void onMessage(@PathParam("id") Integer id, String message) {
        log.info("用户{}发送消息:{}", id, message);
    }

    @OnClose
    public void onClose(@PathParam("id") Integer id) {
        map.remove(id.toString());
        log.info("用户{}已断开", id);
    }

    // 业务层调用这个静态方法
    public static void sendMessage(String message, ViolationAlert violationAlert) {
        Session session = map.get(violationAlert.getMasterId().toString());

        violationAlertMapper.insertViolationAlert(violationAlert);

        if (session != null && session.isOpen()) {
            // 在线：发送并删除（或者更新已读，看你怎么选，这里推荐阅后即焚）
            session.getAsyncRemote().sendText(message);
            log.info("实时推送给班主任 {} 成功", violationAlert.getMasterId());
            violationAlertMapper.deleteViolationAlert(violationAlert);
        } else {
            // 离线：留库里面
            log.info("班主任 {} 未连接，消息已入库等待上线", violationAlert.getMasterId());
        }
    }
}