package top.totoro.instant_messaging.connect;

/**
 * 一个连接的状态量。
 * 创建时间 2020/5/30 16:44
 *
 * @author dragon
 * @version 1.0
 */
public enum  ConnectState {
    // 连接失败：连接数超过最大允许连接数
    CONNECT_FAILED,
    // 连接成功，并连接中
    CONNECTING,
    // 断开连接，但未从连接列表中清除
    DISCONNECTED,
    // 已断开连接，并已从连接列表中清除
    DESTROYED
}
