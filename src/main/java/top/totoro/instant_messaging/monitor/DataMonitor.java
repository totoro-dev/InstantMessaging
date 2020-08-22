package top.totoro.instant_messaging.monitor;

import java.io.Serializable;

/**
 * 数据的监控者。
 * 泛型Data : 可被监控就绪状态的数据类型。
 * 创建时间 2020/5/30 18:33
 *
 * @author dragon
 * @version 1.0
 */
public interface DataMonitor<Data extends Serializable> {

    /**
     * 设置将被监控的数据。
     *
     * @param data 数据
     */
    void setData(Data data);

    /**
     * 获取当前被监控的数据。
     *
     * @return 当前被监控的数据
     */
    Data getData();

    /**
     * 获取数据是否就绪。
     * 需要自己实现对当前监控数据的状态判断，如果数据已经满足返回时则就绪。
     *
     * @return 是否就绪
     */
    boolean dataReady();
}
