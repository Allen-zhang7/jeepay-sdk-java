package com.jeequan.jeepay;

import com.alibaba.fastjson.JSONObject;
import com.jeequan.jeepay.exception.JeepayException;
import com.jeequan.jeepay.net.APIJeepayRequest;
import com.jeequan.jeepay.net.APIResource;
import com.jeequan.jeepay.net.RequestOptions;
import com.jeequan.jeepay.request.JeepayRequest;
import com.jeequan.jeepay.response.JeepayResponse;
import com.jeequan.jeepay.util.JSONWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Jeepay sdk客户端
 *
 * @author jmdhappy
 * @site https://www.jeepay.vip
 * @date 2021-06-08 11:00
 */
public class JeepayClient extends APIResource {

    private static final Map<String, JeepayClient> clientMap = new HashMap<String, JeepayClient>();

    private String appId;
    private String signType = Jeepay.DEFAULT_SIGN_TYPE;
    private String apiKey = Jeepay.apiKey;
    private String apiBase = Jeepay.getApiBase();

    public JeepayClient(String apiBase, String signType, String apiKey) {
        this.apiBase = apiBase;
        this.signType = signType;
        this.apiKey = apiKey;
    }

    public JeepayClient(String apiBase, String apiKey) {
        this.apiBase = apiBase;
        this.apiKey = apiKey;
    }

    public JeepayClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public JeepayClient() {
    }

    public static synchronized JeepayClient getInstance(String appId, String apiKey,
        String apiBase) {
        JeepayClient client = clientMap.get(appId);
        if (client != null) {
            return client;
        }
        client = new JeepayClient();
        clientMap.put(appId, client);
        client.appId = appId;
        client.apiKey = apiKey;
        client.apiBase = apiBase;
        return client;
    }

    public static synchronized JeepayClient getInstance(String appId, String apiKey) {
        JeepayClient client = clientMap.get(appId);
        if (client != null) {
            return client;
        }
        client = new JeepayClient();
        clientMap.put(appId, client);
        client.appId = appId;
        client.apiKey = apiKey;
        return client;
    }

    public static synchronized JeepayClient getInstance(String appId) {
        JeepayClient client = clientMap.get(appId);
        if (client != null) {
            return client;
        }
        client = new JeepayClient();
        clientMap.put(appId, client);
        client.appId = appId;
        return client;
    }

    public String getAppId() {
        return appId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiBase() {
        return apiBase;
    }

    public void setApiBase(String apiBase) {
        this.apiBase = apiBase;
    }

    public <T extends JeepayResponse> T execute(JeepayRequest<T> request) throws JeepayException {

        // 支持用户自己设置RequestOptions
        if (request.getRequestOptions() == null) {
            RequestOptions options = RequestOptions.builder()
                .setVersion(request.getApiVersion())
                .setUri(request.getApiUri())
                .setAppId(this.appId)
                .setApiKey(this.apiKey)
                .build();
            request.setRequestOptions(options);
        }

        return execute(request, RequestMethod.POST, this.apiBase);
    }


    public String getRequestUrl(JeepayRequest request) throws JeepayException {
        // 支持用户自己设置RequestOptions
        if (request.getRequestOptions() == null) {
            RequestOptions options = RequestOptions.builder()
                .setVersion(request.getApiVersion())
                .setUri(request.getApiUri())
                .setAppId(this.appId)
                .setApiKey(this.apiKey)
                .build();
            request.setRequestOptions(options);
        }
        String jsonParam = new JSONWriter().write(request.getBizModel(), true);

        JSONObject params = JSONObject.parseObject(jsonParam);
        request.getRequestOptions();

        return APIJeepayRequest.buildURLWithSign(this.apiBase, params, request.getRequestOptions()).toString();
    }
}
