package top.totoro.instant_messaging.test.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.totoro.instant_messaging.connect.AbstractSingleLongConnect;
import top.totoro.instant_messaging.connect.def.MultipleLongConnect;
import top.totoro.instant_messaging.connect.def.SingleLongConnect;
import top.totoro.instant_messaging.test.DataMonitorTest;
import top.totoro.instant_messaging.utils.IPAddress;

import javax.servlet.http.HttpServletRequest;

/**
 * 创建时间 2020/5/30 20:15
 *
 * @author dragon
 * @version 1.0
 */
@SuppressWarnings("ALL")
@RestController
public class LongConnectController {

    String name;

    /**
     * 同一个ip连接时都按第一个传入的监控者处理
     *
     * @param ready
     * @param request
     * @return
     */
    @RequestMapping("/singleLongConnect")
    private String singleLongConnect(@RequestParam("ready") String ready, HttpServletRequest request) {
        SingleLongConnect<String> connect = AbstractSingleLongConnect.getInstance(request, SingleLongConnect.class, new DataMonitorTest(ready));
        connect.connect();
        new Thread(() -> {
            while (true) {
                connect.getDataMonitor().setData(name);
            }
        }).start();
        return connect.response();
    }

    @RequestMapping("/multipleLongConnect")
    private String multipleLongConnect(@RequestParam("ready") String ready, HttpServletRequest request) {
        MultipleLongConnect<String> connect = new MultipleLongConnect<>(new DataMonitorTest(ready));
        connect.connect();
        new Thread(() -> {
            while (true) {
                connect.getDataMonitor().setData(name);
            }
        }).start();
        return connect.response();
    }

    @RequestMapping("/changeName")
    private String changeName(@RequestParam("name") String n) {
        name = n;
        return name;
    }
}
