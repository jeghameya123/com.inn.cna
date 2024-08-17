package com.inn.cna.servicelmpl;

import com.inn.cna.JWT.CustomerUsersDetailsService;
import com.inn.cna.JWT.JwtFilter;
import com.inn.cna.JWT.JwtUtil;
import com.inn.cna.POJO.User;
import com.inn.cna.constents.CnaConstents;
import com.inn.cna.dao.UserDao;
import com.inn.cna.service.UserService;
import com.inn.cna.utils.CnaUtils;
import com.inn.cna.utils.EmailUtils;
import com.inn.cna.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;
    @Autowired
   JwtUtil jwtUtil;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;



    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CnaUtils.getResponseEntity("Successfully Registered", HttpStatus.OK);
                } else {
                    return CnaUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CnaUtils.getResponseEntity("Invalid data.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return CnaUtils.getResponseEntity(CnaConstents.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    private boolean validateSignUpMap(Map<String, String> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("password");
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));  // Note: corrected key "contactNumber"
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus(requestMap.getOrDefault("status", "active"));  // Assuming default status is "active"
        user.setRole(requestMap.getOrDefault("role", "user"));  // Assuming default role is "user"
        return user;
    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
       log.info("Inside login");
       try{
           Authentication auth= authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))

           );
           if (auth.isAuthenticated()){
               if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                   return new ResponseEntity<String>("{\"token\":\"" +
                           jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                   customerUsersDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
               }
               else{
                   return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval ."+"\"}", HttpStatus.BAD_REQUEST);

                           }
           }


       }catch(Exception ex){
           log.error("{}",ex);
       }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials ."+"\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAlluser(),HttpStatus.OK);

            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);;
            }

        }catch(Exception ex){
            ex.printStackTrace();

        }
        return new ResponseEntity<>(new ArrayList<>,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
       try{
           if(jwtFilter.isAdmin()){
              Optional<User>optional=userDao.findById(Integer.parseInt(requestMap.get("id")));
              if(!optional.isEmpty()){
                  userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                  sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
                  return CnaUtils.getResponseEntity("User Status Updated Successfully", HttpStatus.OK);
              }
              else{
                 return  CnaUtils.getResponseEntity("User id does not exist ",HttpStatus.OK);

              }


           }else{


               return CnaUtils.getResponseEntity(CnaConstents.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
           }

       }catch(Exception ex){
           ex.printStackTrace();

       }
       return CnaUtils.getResponseEntity(CnaConstents.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {

        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"account Approved","USER:-"+user+"\n is approved by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin());


        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"account disaled","USER:-"+user+"\n is disabled by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin());

        }

    }


}
