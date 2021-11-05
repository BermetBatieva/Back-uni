package com.example.Backuni.entity;

import org.springframework.security.core.GrantedAuthority;

public enum ERole implements GrantedAuthority {
    ROLE_ADMIN("ADMIN",1);

    ERole(String name, int id){
        this.name = name;
        this.id = id;
    }

    private int id;


    public static ERole getRole(int id ){
        for(ERole role : ERole.values())
            if(role.getId() == id)
                return role;
        return null;
    }


    @Override
    public String getAuthority() {
        return null;
    }

    public final String name;

    private int getId() {
        return id;
    }
}
