package top.totoro.instant_messaging.connect;

import com.sun.istack.internal.NotNull;
import top.totoro.instant_messaging.monitor.DataMonitor;

import java.io.Serializable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 泛型D : Data，监控者监控的数据类型。
 * 创建时间 2020/5/30 18:45
 *
 * @author dragon
 * @version 1.0
 */
public interface Connect<Data extends Serializable> {

    // 连接轮询服务
    ScheduledExecutorService CONNECT_POLL_SERVICE = Executors.newScheduledThreadPool(2);

    /**
     * 打开长链接，连接的数据监听者在创建连接对象的时候必须已设置。
     * @return 是否连接成功
     */
    boolean connect();

    /**
     * 打开长链接，同时设置连接的数据监听者
     * @param dataMonitor 数据监听者
     * @return 是否连接成功
     */
    boolean connect(@NotNull DataMonitor<Data> dataMonitor);

    /**
     * 断开当前连接，但暂时不从连接列表中清除。
     *
     * @return 是否成功断开
     */
    boolean disconnect();

    /**
     * 销毁当前连接，同时从连接列表中清除。
     */
    void destroy();

    /**
     * 响应连接后，当数据就绪时进行返回。
     *
     * @return
     */
    Data response();

}
