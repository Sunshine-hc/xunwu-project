package com.match.service.user;

import com.google.common.collect.Lists;
import com.match.base.LoginUserUtil;
import com.match.entity.HouseSubscribe;
import com.match.entity.Role;
import com.match.entity.User;
import com.match.repository.RoleRepository;
import com.match.repository.UserRepository;
import com.match.service.IUserService;
import com.match.service.ServiceResult;
import com.match.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final Md5PasswordEncoder passwordEncoder = new Md5PasswordEncoder();

    @Override
    public User findUserByName(String userName) {
        User user = userRepository.findByName(userName);

        if (user == null) {
            return null;
        }
        //user不为空就去查Role（findRolesByUserId()）
        List<Role> roles = roleRepository.findRolesByUserId(user.getId());

        if (roles == null || roles.isEmpty()) {
            throw new DisabledException("权限非法");
        }
        //roles不为空
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }

    @Override
    public ServiceResult<UserDTO> findById(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null){
            return ServiceResult.notFound();
        }

        UserDTO userDTO = modelMapper.map(user,UserDTO.class);

        return ServiceResult.of(userDTO);
    }

    @Override
    public User findUserByTelephone(String telephone) {
        User user = userRepository.findUserByPhoneNumber(telephone);
        if (user == null){
            return null;
        }
        List<Role> roles = roleRepository.findRolesByUserId(user.getId());
        if (roles == null || roles.isEmpty()){
            throw new DisabledException("权限非法");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
        user.setAuthorityList(authorities);
        return user;
    }

    @Override
    @Transactional//事务
    public User addUserByPhone(String telephone) {
        User user = new User();
        user.setPhoneNumber(telephone);
        user.setName(telephone.substring(0,3) + "****" + telephone.substring(7,telephone.length()));
        Date now = new Date();
        user.setCreateTime(now);
        user.setLastLoginTime(now);
        user.setLastUpdateTime(now);
        user = userRepository.save(user);

        Role role = new Role();
        role.setName("USER");//设置为普通用户的角色
        role.setUserId(user.getId());
        roleRepository.save(role);
        user.setAuthorityList(Lists.newArrayList(new SimpleGrantedAuthority("ROLE_USER")));

        return user;
    }

    @Override
    @Transactional
    public ServiceResult modifyUserProfile(String profile, String value) {
        Long userId = LoginUserUtil.getLoginUserId();
        if (profile == null || profile.isEmpty()){
            return new ServiceResult(false,"属性不可以为空");
        }
        switch (profile){
            case "name":
                userRepository.updateUsername(userId,value);
                break;
            case "email":
                userRepository.updateEmail(userId,value);
                break;
            case "password":
                userRepository.updatePassword(userId,this.passwordEncoder.encodePassword(value,userId));
                break;
            default:
                return new ServiceResult(false,"不支持的属性");
        }
        return ServiceResult.success();
    }

}
