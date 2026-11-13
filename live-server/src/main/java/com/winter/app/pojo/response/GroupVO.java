package com.winter.app.pojo.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class GroupVO {
    private Integer id;
    private String name;
    private Integer parentId;
    private String icon;
    private Integer sort;
    private List<GroupVO> children = new ArrayList<>();
}
