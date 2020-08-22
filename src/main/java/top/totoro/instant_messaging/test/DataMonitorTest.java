package top.totoro.instant_messaging.test;

import com.sun.istack.internal.NotNull;
import top.totoro.instant_messaging.monitor.DataMonitor;

/**
 * 创建时间 2020/5/30 20:18
 *
 * @author dragon
 * @version 1.0
 */
public class DataMonitorTest implements DataMonitor<String> {

    private String name;
    private String ready = "";

    public DataMonitorTest() {
    }

    public DataMonitorTest(@NotNull String ready) {
        this.ready = ready;
    }

    @Override
    public void setData(String s) {
        name = s;
    }

    @Override
    public String getData() {
        return name;
    }

    @Override
    public boolean dataReady() {
        return ready.equals(name);
    }
}
