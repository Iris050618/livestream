package com.winter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("biz_dvr")
public class Dvr extends Model<Dvr> {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer liveId;
    private String url;
    private String fileName;
    private Date createTime;
}
