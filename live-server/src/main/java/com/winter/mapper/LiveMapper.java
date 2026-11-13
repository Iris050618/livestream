package com.winter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.winter.app.pojo.response.LiveVO;
import com.winter.entity.Live;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface LiveMapper extends BaseMapper<Live> {

    @Select("<script>" +
            "SELECT l.id, l.name as title, l.group_id, g.name as group_name, l.banner, l.summary, l.status, u.name as nick_name, u.avatar " +
            "FROM biz_live l " +
            "LEFT JOIN biz_group g ON g.id = l.group_id " +
            "LEFT JOIN sys_user u ON u.id = l.user_id " +
            "WHERE l.status = 'ON_LINE' " +
            "<if test='groupId != null'> AND l.group_id = #{groupId} </if>" +
            "ORDER BY l.id DESC" +
            "</script>")
    Page<LiveVO> selectPage(Page page, Integer groupId);

    @Select("SELECT l.id, l.name as title, l.group_id, g.name as group_name, l.banner, l.summary, l.status, u.name as nick_name, u.avatar " +
            "FROM biz_live l " +
            "LEFT JOIN biz_group g ON g.id = l.group_id " +
            "LEFT JOIN sys_user u ON u.id = l.user_id " +
            "WHERE l.id = #{liveId}"
    )
    LiveVO selectDetail(Integer liveId);
}
