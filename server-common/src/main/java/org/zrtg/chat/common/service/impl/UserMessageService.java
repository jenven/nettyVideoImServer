package org.zrtg.chat.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.zrtg.chat.common.domain.UserMessageEntity;
import org.zrtg.chat.common.mapper.UserMessageMapper;
import org.zrtg.chat.common.service.IUserMessageService;

/**
 * @author wangq
 * @create_at 2021-4-8 15:50
 */
@Service
public class UserMessageService extends ServiceImpl<UserMessageMapper, UserMessageEntity> implements IUserMessageService
{
}
