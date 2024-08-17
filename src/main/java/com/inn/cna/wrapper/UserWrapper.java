package com.inn.cna.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {


    private Integer id;
    private String name;
    private String email;
    private String contactNumber;
    private String Status;

    public UserWrapper(Integer id,String name,String email,String contactNumber,String Status) {
        this.id=id;
        this.contactNumber=contactNumber;
        this.name=name;
        this.email=email;
        this.Status=Status;


    }
}
