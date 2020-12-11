package cn.riversky.service;

import cn.riversky.domain.UmsAdmin;
import cn.riversky.generic.GenericService;

public interface  UmsAdminService extends GenericService<UmsAdmin> {

    boolean authenticate(String email, String password);

    UmsAdmin findByEmail(String email);
}
