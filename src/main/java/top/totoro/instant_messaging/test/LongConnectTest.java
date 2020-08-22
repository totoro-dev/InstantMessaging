package top.totoro.instant_messaging.test;

import top.totoro.instant_messaging.connect.def.SingleLongConnect;
import top.totoro.instant_messaging.monitor.DataMonitor;

import static java.lang.Thread.sleep;

/**
 * 创建时间 2020/5/30 19:53
 *
 * @author dragon
 * @version 1.0
 */
public class LongConnectTest {
    public static void main(String[] args) {
        SingleLongConnect<String> singleLongConnect1 = SingleLongConnect.getInstance(null, SingleLongConnect.class);
        SingleLongConnect<String> singleLongConnect2 = SingleLongConnect.getInstance(null, SingleLongConnect.class);
        DataMonitor<String> dataMonitor1 = new DataMonitorTest("黄龙淼");
        DataMonitor<String> dataMonitor2 = new DataMonitorTest("1234");
        singleLongConnect1.connect(dataMonitor1);
        singleLongConnect2.connect(dataMonitor2);
        System.out.println("defaultLongConnect1调用了connect()，当前连接状态 = " + singleLongConnect1.getConnectState());
        System.out.println("defaultLongConnect2调用了connect()，当前连接状态 = " + singleLongConnect2.getConnectState());
        new Thread(() -> {
            try {
                sleep(2000);
                dataMonitor2.setData("1234");
                sleep(2000);
                dataMonitor1.setData("黄龙淼");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String data1 = singleLongConnect1.response();
            String data2 = singleLongConnect2.response();
            System.out.println("defaultLongConnect1调用了response()，response = " + data1);
            System.out.println("defaultLongConnect2调用了response()，response = " + data2);
        }).start();
    }

}
