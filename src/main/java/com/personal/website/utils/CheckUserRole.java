package com.personal.website.utils;

import com.personal.website.entity.RoleEntinty;
import com.personal.website.enumconstants.ERole;

import java.util.List;

public class CheckUserRole {
    public static boolean isAdmin(List<RoleEntinty> roles) {
        boolean flag = false;
        for (RoleEntinty role : roles) {
            if (role.getName().name().equals(ERole.ROLE_ADMIN.name())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    public static boolean isSubscriber(List<RoleEntinty> roles)
    {
        return roles.size() == 1;
    }
}
