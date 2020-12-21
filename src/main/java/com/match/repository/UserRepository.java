package com.match.repository;

import com.match.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByName(String userName);

    User findUserByPhoneNumber(String telephone);

    /**
     * 修改昵称  用户名
     * @param id
     * @param name
     */
    @Modifying
    @Query("update User as user set user.name = :name where id = :id")
    void updateUsername(@Param(value = "id") Long id, @Param(value = "name") String name);

    /**
     * 修改email
     * @param id
     * @param email
     */
    @Modifying
    @Query("update User as user set user.email = :email where id = :id")
    void updateEmail(@Param(value = "id") Long id, @Param(value = "email") String email);

    /**
     * 修改密码
     * @param id
     * @param password
     */
    @Modifying
    @Query("update User as user set user.password = :password where id = :id")
    void updatePassword(@Param(value = "id") Long id, @Param(value = "password") String password);
}
