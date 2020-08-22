package top.totoro.instant_messaging.connect;

import top.totoro.instant_messaging.monitor.DataMonitor;
import top.totoro.instant_messaging.utils.IPAddress;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单ip单监控者的长连接
 * 实现即时通讯的长连接，当数据就绪时会触发长连接的response。
 * 创建时间 2020/5/30 16:31
 *
 * @author dragon
 * @version 1.0
 */
public abstract class AbstractSingleLongConnect<D extends Serializable> extends LongConnect<D> {

    private String mIP;

    /**
     * 存储当前所有连接状态为{@see ConnectState#CONNECTING}的所有长连接例表。
     * key : 连接的客户端ip
     * value : 同一ip连接对象
     */
    protected static final Map<String, AbstractSingleLongConnect> SINGLE_LONG_CONNECT_MAP = new ConcurrentHashMap<>();

    public String getIP() {
        return mIP;
    }

    public void setIP(String mIP) {
        this.mIP = mIP;
    }

    protected AbstractSingleLongConnect() {
    }

    public static <Single extends AbstractSingleLongConnect> Single getInstance(HttpServletRequest request, Class<Single> ifNull) {
        String ip = IPAddress.getClientIp(request);
        AbstractSingleLongConnect instance = SINGLE_LONG_CONNECT_MAP.get(ip);
        if (instance == null || !instance.getClass().isAssignableFrom(ifNull)) {
            try {
                Constructor<Single> constructor = ifNull.getDeclaredConstructor();
                constructor.setAccessible(true);
                Single connect = constructor.newInstance();
                connect.setIP(ip);
                return connect;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return (Single) instance;
    }

    public static <Single extends AbstractSingleLongConnect> Single getInstance(HttpServletRequest request, Class<Single> ifNull, DataMonitor monitor) {
        String ip = IPAddress.getClientIp(request);
        AbstractSingleLongConnect instance = SINGLE_LONG_CONNECT_MAP.get(ip);
        if (instance == null || !instance.getClass().isAssignableFrom(ifNull)) {
            try {
                Constructor<Single> constructor = ifNull.getDeclaredConstructor(DataMonitor.class);
                constructor.setAccessible(true);
                Single connect = constructor.newInstance(monitor);
                connect.setIP(ip);
                return connect;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return (Single) instance;
    }

}
