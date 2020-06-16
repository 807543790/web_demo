package com.zhangbin.service;

import com.zhangbin.mapper.UserMapper;
import com.zhangbin.model.User;
import com.zhangbin.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    //userMapper是SQL层
    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        //判断用户是否存在，如果存在即非null,更新即可。不存在未null则创建用户
        if(users.size() == 0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else{
            //更新
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarurl(user.getAvatarurl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());

            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser,example);

        }

    }
}
