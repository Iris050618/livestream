package com.winter.app.pojo.form;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.winter.exception.ServiceException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Data
@Accessors(chain = true)
public class CallbackParams {
    private String serverId;
    private String serviceId;
    private String action;
    private String clientId;
    private String id;
    private String vhost;
    private String app;
    private String tcUrl;
    private String stream;
    private String param;
    private String cwd;
    // 录制的视频文件地址
    private String file;
    private String streamUrl;
    private String streamId;

    @Data
    @Accessors(chain = true)
    public static class Param{
        private String streamUrl;
        private String signature;
        private String liveId;
        private String timestamp;
    }

    /**
     * 解析url参数
     * @return
     */
    public CallbackParams.Param parseParam() {
        MultiValueMap<String, String> params = UriComponentsBuilder.fromUriString(this.param).build().getQueryParams();
        List<String> signatureParam = params.get("signature");
        List<String> liveIdParam = params.get("liveId");
        List<String> timestampParam = params.get("timestamp");
        if (ObjectUtil.isEmpty(signatureParam)) {
            throw new IllegalArgumentException("signature参数缺失");
        }
        if (ObjectUtil.isEmpty(liveIdParam)) {
            throw new IllegalArgumentException("liveId参数缺失");
        }
        if (ObjectUtil.isEmpty(timestampParam)) {
            throw new IllegalArgumentException("timestamp参数缺失");
        }
        String signature = signatureParam.get(0);
        String liveId = liveIdParam.get(0);
        String timestamp = timestampParam.get(0);
        Param data = new Param();
        data.setSignature(signature).setLiveId(liveId).setTimestamp(timestamp).setStreamUrl(this.streamUrl);
        return data;
    }
}
