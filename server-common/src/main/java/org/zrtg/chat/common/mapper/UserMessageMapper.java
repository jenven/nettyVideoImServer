package org.zrtg.chat.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.zrtg.chat.common.domain.UserMessageEntity;

/**
 * @author wangq
 * @create_at 2021-4-8 15:48
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessageEntity>
{

}
