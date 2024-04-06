package com.ashcollege.responses;

import javax.persistence.criteria.CriteriaBuilder;

public class RegisterResponse extends BasicResponse
{
    private Integer id ;

    public RegisterResponse(Integer id) {
        this.id = id;
    }

    public RegisterResponse(boolean success, Integer errorCode, Integer id) {
        super(success, errorCode);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
