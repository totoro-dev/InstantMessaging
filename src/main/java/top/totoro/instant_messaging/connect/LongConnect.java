package top.totoro.instant_messaging.connect;

import top.totoro.instant_messaging.monitor.DataMonitor;

import java.io.Serializable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

/**
 * 创建时间 2020/5/31 11:30
 *
 * @author dragon
 * @version 1.0
 */
public abstract class LongConnect<D extends Serializable> implements Connect<D> {

    private ConnectState mConnectState = ConnectState.CONNECT_FAILED;
    private DataMonitor<D> mDataMonitor;
    private static long mMaxConnectSize = 100;
    // 轮询数据状态的时间间隔（ms）
    private long mPollTime = 10;

    public LongConnect() {
    }

    protected LongConnect(DataMonitor<D> monitor) {
        setDataMonitor(monitor);
    }

    public ConnectState getConnectState() {
        return mConnectState;
    }

    public void setConnectState(ConnectState mConnectState) {
        this.mConnectState = mConnectState;
    }

    public long getMaxConnectSize() {
        return mMaxConnectSize;
    }

    public void setMaxConnectSize(long maxConnectSize) {
        if (maxConnectSize < 0) return;
        mMaxConnectSize = maxConnectSize;
    }

    public long getPollTime() {
        return mPollTime;
    }

    public void setPollTime(long mPollTime) {
        this.mPollTime = mPollTime;
    }

    public void setDataMonitor(DataMonitor<D> mDataMonitor) {
        this.mDataMonitor = mDataMonitor;
    }

    public DataMonitor<D> getDataMonitor() {
        return mDataMonitor;
    }

    @Override
    public D response() {
        // 只有连接状态为CONNECTING时才走异步响应，否则直接返回当前数据
        if (mConnectState.equals(ConnectState.CONNECTING)) {
            ScheduledFuture<D> future = CONNECT_POLL_SERVICE.schedule(() -> {
                while (true) {
                    if (mDataMonitor.dataReady()) {
                        return mDataMonitor.getData();
                    }
                    sleep(mPollTime);
                }
            }, 0, TimeUnit.MILLISECONDS);
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return mDataMonitor.getData();
    }

    @Override
    public boolean disconnect() {
        if (mConnectState.equals(ConnectState.CONNECTING)) {
            mConnectState = ConnectState.DISCONNECTED;
            return true;
        }
        return false;
    }

}
