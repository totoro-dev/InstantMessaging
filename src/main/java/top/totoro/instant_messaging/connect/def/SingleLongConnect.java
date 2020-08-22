package top.totoro.instant_messaging.connect.def;

import com.sun.istack.internal.NotNull;
import top.totoro.instant_messaging.connect.AbstractSingleLongConnect;
import top.totoro.instant_messaging.connect.ConnectState;
import top.totoro.instant_messaging.monitor.DataMonitor;

import java.io.Serializable;

/**
 * 创建时间 2020/5/30 18:49
 *
 * @author dragon
 * @version 1.0
 */
public class SingleLongConnect<Data extends Serializable> extends AbstractSingleLongConnect<Data> {

    private SingleLongConnect() {
        super();
    }

    private SingleLongConnect(DataMonitor<Data> monitor){
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
        if (getConnectState().equals(ConnectState.CONNECTING)) {
            return true;
        }
        synchronized (SINGLE_LONG_CONNECT_MAP) {
            // 请求一个连接时，若当前请求不在连接列表中，且连接数超标时，无法进行连接
            if (!SINGLE_LONG_CONNECT_MAP.containsKey(getIP())) {
                if (SINGLE_LONG_CONNECT_MAP.size() >= getMaxConnectSize()) {
                    setConnectState(ConnectState.CONNECT_FAILED);
                    return false;
                }
                SINGLE_LONG_CONNECT_MAP.put(getIP(), this);
            } else {
                setDataMonitor(SINGLE_LONG_CONNECT_MAP.get(getIP()).getDataMonitor());
            }
        }
        System.out.println("single connect by ip(" + getIP() + ") monitor" + getDataMonitor().hashCode());
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
        if (getConnectState().equals(ConnectState.CONNECTING)) {
            return true;
        }
        setDataMonitor(dataMonitor);
        synchronized (SINGLE_LONG_CONNECT_MAP) {
            // 请求一个连接时，若当前请求不在连接列表中，且连接数超标时，无法进行连接
            if (!SINGLE_LONG_CONNECT_MAP.containsKey(getIP())) {
                if (SINGLE_LONG_CONNECT_MAP.size() >= getMaxConnectSize()) {
                    setConnectState(ConnectState.CONNECT_FAILED);
                    return false;
                }
                SINGLE_LONG_CONNECT_MAP.put(getIP(), this);
            } else {
                dataMonitor = SINGLE_LONG_CONNECT_MAP.get(getIP()).getDataMonitor();
                setDataMonitor(dataMonitor);
            }
        }
        System.out.println("single connect by ip(" + getIP() + ") monitor" + getDataMonitor().hashCode());
        setConnectState(ConnectState.CONNECTING);
        return true;
    }

    @Override
    public void destroy() {
        if (getConnectState().equals(ConnectState.CONNECTING)) {
            disconnect();
        }
        SINGLE_LONG_CONNECT_MAP.remove(getIP());
        setConnectState(ConnectState.DESTROYED);
    }
}
