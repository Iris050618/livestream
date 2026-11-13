package com.winter.app.pojo.response;

import com.winter.enums.LiveStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LiveVO {
    private Integer id;
    private String title;
    private String banner;
    private String summary;
    private LiveStatus status;
    private Integer groupId;
    private String groupName;
    private String playUrl;
    // 用户
    private String nickName;
    private String avatar;
}
