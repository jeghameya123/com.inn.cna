package com.inn.cna.POJO;

import jakarta.persistence.*;


import lombok.*;
import org.hibernate.annotations.DynamicInsert;


import org.hibernate.annotations.DynamicUpdate;


import java.io.Serializable;
@NamedQuery(name="User.findByEmailId", query = "select u from User u where u.email=:email")

@NamedQuery(name="User.updateStatus",query ="update User u set u.status=:status where u.id=:id")

@NamedQuery(name=".getAllUser",query ="select new  com.inn.cna.wrapper.UserWrapper(u.id,u.name,u.email,u.contactNumber,u.status) from User u where u.role='user'")

@NamedQuery(name=".getAllAdmin",query ="select u.email User u where u.role='admin'")

@Data
@Entity

@Table(name= "user")

@Getter
@Setter
@AllArgsConstructor


public class User implements Serializable {

    private static final long serialVersionUID = 1L ;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")


    private Integer id ;

    @Column(name = "name")
    private String name;

    @Column(name = "contactNumber")
    private String contactNumber;

    @Column(name= "email")
    private String email;


    @Column(name= " password ")
    private String password;

    @Column(name="status")
    private String status;

    @Column(name="role")
    private String role;

    public User(){

    }
    public User(String name,String contactNumber,String email,String password,String status,String role) {
        this.name=name;
        this.contactNumber=contactNumber;
        this.email=email;
        this.password=password;
        this.status=status;
        this.role=role;

    }



}

