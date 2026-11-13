package com.winter.app.controller;

import cn.hutool.core.util.StrUtil;
import com.winter.app.pojo.form.CallbackParams;
import com.winter.entity.Dvr;
import com.winter.entity.Live;
import com.winter.enums.LiveStatus;
import com.winter.mapper.DvrMapper;
import com.winter.mapper.LiveMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/live")
@RestController
@Slf4j
public class LiveCallbackController {

    @Autowired
    private LiveMapper liveMapper;

    @Autowired
    private DvrMapper dvrMapper;

    @Value("${server.servlet.context-path}")
    private String apiPath;

    @Value("${app.signature-expired-time}")
    private Integer signatureExpiredTime;
    /**
     * 推流回调接口
     *
     * @return
     */
    @PostMapping("/publish")
    public String onPublish(@RequestBody CallbackParams body) {
        log.debug("========推流========");
        CallbackParams.Param param;
        try {
            param = body.parseParam();
        }catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            // 参数错误
            return "{\"code\":500}";
        }
        String streamUrl = param.getStreamUrl();
        String signature = param.getSignature();
        String liveId = param.getLiveId();
        String timestamp = param.getTimestamp();
        // 校验直播是否存在
        Live live = this.liveMapper.selectById(liveId);
        if (live == null) {
            log.debug("资源不存在");
            return "{\"code\":500}";
        }
        String secret = live.getSecret();
        // 拼接推流参数
        String str = StrUtil.format("streamUrl=/live/{}&liveId={}&timestamp={}&secret={}", liveId, liveId, timestamp, secret);
        // 将参数计算md5，得到签名
        String signature2 = DigestUtils.md5Hex(str);
        // 对比两次签名是否一致
        if(!signature.equals(signature2)) {
            log.debug("参数签名错误");
            return "{\"code\":500}";
        }
        // 判断是否已过期
        long $timestamp = Long.parseLong(param.getTimestamp());
        if(System.currentTimeMillis() - $timestamp * 1000 > signatureExpiredTime * 1000) {
            // 密钥已过期，不允许推流
            log.debug("参数已过期");
            return "{\"code\":500}";
        }
        // 验证通过，允许推流，修改直播间状态为在线
        Live updateLive = new Live();
        updateLive.setId(Integer.valueOf(liveId)).setStatus(LiveStatus.ON_LINE);
        this.liveMapper.updateById(updateLive);
        return "{\"code\":0}";
    }

    /**
     * 结束推流
     *
     * @return
     */
    @PostMapping("/unpublish")
    public String onUnpublish(@RequestBody CallbackParams body) {
        log.debug("========结束推流========");
        CallbackParams.Param param;
        try {
            param = body.parseParam();
        }catch (IllegalArgumentException e) {
            // 参数错误
            log.error(e.getMessage());
            return "{\"code\":500}";
        }
        String streamUrl = param.getStreamUrl();
        String signature = param.getSignature();
        String liveId = param.getLiveId();
        String timestamp = param.getTimestamp();
        // 校验直播是否存在
        Live live = this.liveMapper.selectById(liveId);
        if (live == null) {
            log.debug("资源不存在");
            return "{\"code\":500}";
        }
        String secret = live.getSecret();
        // 拼接推流参数
        String str = StrUtil.format("streamUrl=/live/{}&liveId={}&timestamp={}&secret={}", liveId, liveId, timestamp, secret);
        // 将参数计算md5，得到签名
        String signature2 = DigestUtils.md5Hex(str);
        // 对比两次签名是否一致
        if(signature.equals(signature2)) {
            // 如果一致，修改直播状态为离线
            Live updateLive = new Live();
            updateLive.setId(Integer.valueOf(liveId)).setStatus(LiveStatus.OFF_LINE);
            this.liveMapper.updateById(updateLive);
            return "{\"code\":0}";
        }else {
            log.debug("参数签名错误");
            return "{\"code\":500}";
        }
    }

    /**
     * 客户端开始播放
     *
     * @return
     */
    @PostMapping("/play")
    public String onPlay(@RequestBody CallbackParams body) {
        log.debug("========播放========");
        body.getParam();
        String token;
        // 房间人数 + 1
        return "{\"code\":0}";
    }


    /**
     * 客户端停止播放
     *
     * @return
     */
    @PostMapping("/stop")
    public String onStop(@RequestBody CallbackParams body) {
        log.debug("========结束播放========");
        // 房间人数 - 1
        return "{\"code\":0}";
    }

    /**
     * DVR录制关闭一个flv文件，生成了一个录播的文件
     *
     * @return
     */
    @PostMapping("/dvrs")
    public String onDvrs(@RequestBody CallbackParams body) {
        log.debug("上传录播文件");
        log.debug(body.toString());
        String liveId = body.getStream();
        // 保存录播文件地址
        String url = body.getFile().replace("/home/live", this.apiPath + "/dvrs");
        int lastIndex = url.lastIndexOf("/");
        String fileName = url.substring(lastIndex + 1);
        Dvr dvr = new Dvr();
        dvr.setLiveId(Integer.valueOf(liveId)).setUrl(url).setFileName(fileName);
        dvrMapper.insert(dvr);
        return "{\"code\":0}";
    }
}
