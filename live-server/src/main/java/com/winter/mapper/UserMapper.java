package com.winter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.winter.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
