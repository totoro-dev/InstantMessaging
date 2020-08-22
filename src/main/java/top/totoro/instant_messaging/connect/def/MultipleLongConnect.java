package top.totoro.instant_messaging.connect.def;

import top.totoro.instant_messaging.connect.ConnectState;
import top.totoro.instant_messaging.connect.AbstractMultipleLongConnect;
import top.totoro.instant_messaging.monitor.DataMonitor;

import java.io.Serializable;

/**
 * 创建时间 2020/5/31 11:17
 *
 * @author dragon
 * @version 1.0
 */
public class MultipleLongConnect<Data extends Serializable> extends AbstractMultipleLongConnect<Data> {

    public MultipleLongConnect() {
    }

    public MultipleLongConnect(DataMonitor<Data> monitor){
        setDataMonitor(monitor);
    }

    @Override
    public boolean connect() {
        try {
            if (getDataMonitor() == null) {
                throw new NullPointerException("长连接必须在connect前设置DataMonitor：setDataMonitor 或 使用带参构造函数");
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        synchronized (Multiple_LONG_CONNECT_LIST) {
            // 请求一个连接时，若当前请求不在连接列表中，且连接数超标时，无法进行连接
            if (!Multiple_LONG_CONNECT_LIST.contains(getDataMonitor())) {
                if (Multiple_LONG_CONNECT_LIST.size() >= getMaxConnectSize()) {
                    setConnectState(ConnectState.CONNECT_FAILED);
                    return false;
                }
                Multiple_LONG_CONNECT_LIST.add(getDataMonitor());
            }
        }
        System.out.println("multiple connect by " + getDataMonitor().hashCode());
        setConnectState(ConnectState.CONNECTING);
        return false;
    }

    /**
     * 当接收一个请求时会响应连接，并将请求注册到长连接队列中。
     *
     * @param dataMonitor 用于监控数据就绪的监控者
     * @return 是否成功长连接。
     */
    @Override
    public boolean connect(DataMonitor<Data> dataMonitor) {
        setDataMonitor(dataMonitor);
        synchronized (Multiple_LONG_CONNECT_LIST) {
            // 请求一个连接时，若当前请求不在连接列表中，且连接数超标时，无法进行连接
            if (!Multiple_LONG_CONNECT_LIST.contains(dataMonitor)) {
                if (Multiple_LONG_CONNECT_LIST.size() >= getMaxConnectSize()) {
                    setConnectState(ConnectState.CONNECT_FAILED);
                    return false;
                }
                Multiple_LONG_CONNECT_LIST.add(dataMonitor);
            }
        }
        System.out.println("multiple connect by " + getDataMonitor().hashCode());
        setConnectState(ConnectState.CONNECTING);
        return true;
    }

    @Override
    public void destroy() {
        if (getConnectState().equals(ConnectState.CONNECTING)) {
            disconnect();
        }
        Multiple_LONG_CONNECT_LIST.remove(getDataMonitor());
        setConnectState(ConnectState.DESTROYED);
    }

}
