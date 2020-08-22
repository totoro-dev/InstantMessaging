package top.totoro.instant_messaging.connect;

import top.totoro.instant_messaging.monitor.DataMonitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 单ip多监控者的长连接
 * 实现即时通讯的长连接，当数据就绪时会触发长连接的response。
 * 创建时间 2020/5/31 11:20
 *
 * @author dragon
 * @version 1.0
 */
public abstract class AbstractMultipleLongConnect<D extends Serializable> extends LongConnect<D> {
    /**
     * 存储当前所有连接状态为{@see ConnectState#CONNECTING}的所有长连接例表。
     */
    protected static final List<DataMonitor> Multiple_LONG_CONNECT_LIST = new ArrayList<>();

}
