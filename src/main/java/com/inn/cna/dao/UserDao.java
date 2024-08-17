package com.inn.cna.dao;

import com.inn.cna.wrapper.UserWrapper;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.inn.cna.POJO.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface UserDao extends JpaRepository <User , Integer > {

    User findByEmailId(@Param("email")String email);

    List <UserWrapper> getAllUser();

    List <String> getAllAdmin();


    @Transactional
    @Modifying
    Integer updateStatus(@Param("id") Integer id, @Param("status")String status);

    List<UserWrapper> getAlluser();
}
