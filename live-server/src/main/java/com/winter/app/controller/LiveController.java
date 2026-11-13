package com.winter.app.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.winter.app.pojo.form.LiveSettingForm;
import com.winter.app.pojo.response.DvrVO;
import com.winter.app.pojo.response.GroupVO;
import com.winter.app.pojo.response.LiveInfoVO;
import com.winter.app.pojo.response.LiveVO;
import com.winter.common.pojo.Response;
import com.winter.common.pojo.ResponseType;
import com.winter.common.pojo.UserToken;
import com.winter.entity.Dvr;
import com.winter.entity.Group;
import com.winter.entity.Live;
import com.winter.mapper.DvrMapper;
import com.winter.mapper.GroupMapper;
import com.winter.mapper.LiveMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/live")
public class LiveController {
    @Autowired
    private GroupMapper groupMapper;
    @Autowired(required = false)
    private HttpServletRequest request;
    @Autowired
    private LiveMapper liveMapper;
    @Autowired
    private DvrMapper dvrMapper;

    @Value("${app.live-url}")
    private String liveUrl;

    @Value("${app.play-url}")
    private String playUrl;

    /**
     * 直播列表
     *
     * @param currentPage
     * @param pageSize
     * @param groupId
     * @return
     */
    @GetMapping("/page")
    public Response<Page<LiveVO>> getList(@RequestParam Long currentPage, @RequestParam Long pageSize, Integer groupId) {
        Page<LiveVO> page = new Page(currentPage, pageSize);
        page = liveMapper.selectPage(page, groupId);
        return Response.success(page);
    }

    /**
     * 直播详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail")
    public Response<LiveVO> getDetail(@RequestParam Integer id) {
        LiveVO detail = liveMapper.selectDetail(id);
        if (detail == null) {
            return Response.failed(ResponseType.NOT_FOUND);
        }
        String playUrl = StrUtil.format("{}/{}.flv", this.playUrl, id);
        detail.setPlayUrl(playUrl);
        return Response.success(detail);
    }

    /**
     * 获取录播列表
     * @param currentPage
     * @param pageSize
     * @param liveId
     * @return
     */
    @GetMapping("/dvr/page")
    public Response<Page<DvrVO>> getDvrPage(@RequestParam Long currentPage, @RequestParam Long pageSize, @RequestParam Integer liveId) {
        Page<Dvr> page = new Page<>(currentPage, pageSize);
        QueryWrapper<Dvr> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("live_id", liveId).orderByDesc("id");
        this.dvrMapper.selectPage(page, queryWrapper);
        Page<DvrVO> result = new Page<>(currentPage, pageSize);
        result.setTotal(page.getTotal());
        List<DvrVO> list = BeanUtil.copyToList(page.getRecords(), DvrVO.class);
        result.setRecords(list);
        return Response.success(result);
    }

    /**
     * 分组
     *
     * @return
     */
    @GetMapping("/group/list")
    public Response<List<GroupVO>> getGroup() {
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("sort").orderByAsc("id").last("limit 200");
        List<Group> groupList = groupMapper.selectList(queryWrapper);
        List<GroupVO> list = BeanUtil.copyToList(groupList, GroupVO.class);
        List<GroupVO> result = new ArrayList();
        Map<Integer, GroupVO> temp = new HashMap();
        for (GroupVO item : list) {
            temp.put(item.getId(), item);
        }
        for (GroupVO item : list) {
            Integer parentId = item.getParentId();
            if (parentId == 0) {
                result.add(item);
            } else {
                GroupVO parent = temp.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(item);
                }
            }
        }
        return Response.success(result);
    }

    /**
     * 保存直播设置
     *
     * @param form
     * @return
     */
    @PostMapping("/setting/save")
    public Response<String> saveSetting(@RequestBody LiveSettingForm form) {
        UserToken userToken = (UserToken) request.getAttribute("user");
        Integer userId = userToken.getId();
        Live live = new Live();
        live.setUserId(userId)
                .setName(form.getTitle())
                .setGroupId(form.getGroupId())
                .setBanner(form.getBanner());
        UpdateWrapper<Live> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", userId);
        this.liveMapper.update(live, updateWrapper);
        return Response.success();
    }

    /**
     * 获取直播设置
     *
     * @return
     */
    @GetMapping("/setting")
    public Response<LiveInfoVO> getSetting() {
        UserToken userToken = (UserToken) request.getAttribute("user");
        Integer userId = userToken.getId();
        QueryWrapper<Live> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).last("limit 1");
        Live live = this.liveMapper.selectOne(queryWrapper);
        LiveInfoVO info = new LiveInfoVO();
        Integer liveId = live.getId();
        Long timestamp = System.currentTimeMillis() / 1000;
        String secret = live.getSecret();
        // 拼接推流参数
        String param = StrUtil.format("streamUrl=/live/{}&liveId={}&timestamp={}&secret={}", liveId, liveId, timestamp, secret);
        // 将参数计算md5，得到签名
        String signature = DigestUtils.md5Hex(param);
        String streamKey = StrUtil.format("/{}?signature={}&liveId={}&timestamp={}", liveId, signature, liveId, timestamp);
        info.setLiveId(liveId)
                .setGroupId(live.getGroupId())
                .setGroupName(null)
                .setTitle(live.getName())
                .setLiveUrl(this.liveUrl)
                .setStreamKey(streamKey)
                .setBanner(live.getBanner());
        return Response.success(info);
    }
}
