package store.code.extension.exmail.way1.common;

import store.code.extension.exmail.way1.common.bean.AccessToken;

/**
 * Access Token 服务类。
 * @see <a href="https://exmail.qq.com/qy_mng_logic/doc#10003">获取ACCESS_TOKEN</a>
 * @author Kahle
 */
public interface AccessTokenService {

    /**
     * 获取当前实例缓存的 Access Token 的值，
     * 如果不存在或者已过期，则重新从腾讯获取并缓存。
     */
    String token();

    /**
     * 获取当前实例缓存的 Access Token 对象，
     * 如果不存在或者已过期，则重新从腾讯获取并缓存。
     */
    AccessToken getToken();

    /**
     * 根据 corpId 和 corpSecret 调用腾讯获取 Access Token 对象（无缓存）。
     */
    AccessToken getToken(String corpId, String corpSecret);

}
