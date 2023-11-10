package store.code.extension.exmail.way1.common.bean;

/**
 * Access Token 对象。
 * @author Kahle
 */
public class AccessToken extends BaseResult {
    /**
     * 获取到的凭证。长度为64至512个字节。
     */
    private String accessToken;
    /**
     * 凭证的有效时间（秒）。
     */
    private String expiresIn;

    public String getAccessToken() {

        return accessToken;
    }

    public void setAccessToken(String accessToken) {

        this.accessToken = accessToken;
    }

    public String getExpiresIn() {

        return expiresIn;
    }

    public void setExpiresIn(String expiresIn) {

        this.expiresIn = expiresIn;
    }

}
