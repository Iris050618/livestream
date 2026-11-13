package com.winter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.winter.enums.LiveStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("biz_live")
public class Live {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String banner;
    private String summary;
    private Integer userId;
    private String secret;
    private LiveStatus status;
    private Integer groupId;
    private Date createTime;
}
