package com.winter.app.pojo.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LiveInfoVO {
    private Integer liveId;
    private Integer groupId;
    private String groupName;
    private String title;
    private String liveUrl;
    private String streamKey;
    private String banner;
}
