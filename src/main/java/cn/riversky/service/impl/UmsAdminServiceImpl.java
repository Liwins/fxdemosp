package cn.riversky.service.impl;

import cn.riversky.domain.UmsAdmin;
import cn.riversky.repository.UmsAdminRepository;
import cn.riversky.service.UmsAdminService;
import net.bytebuddy.implementation.bytecode.assign.primitive.PrimitiveUnboxingDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author riversky
 * @date 2020/12/11
 **/
@Service
public class UmsAdminServiceImpl implements UmsAdminService {
    @Autowired
    private UmsAdminRepository umsAdminRepository;
    @Override
    public boolean authenticate(String email, String password) {
        UmsAdmin user = this.findByEmail(email);
        if(user == null){
            return false;
        }else{
            return password.equals(user.getPassword());
        }
    }

    @Override
    public UmsAdmin findByEmail(String email) {
        return umsAdminRepository.findByEmail(email);
    }

    @Override
    public UmsAdmin save(UmsAdmin entity) {
        return umsAdminRepository.save(entity);
    }

    @Override
    public UmsAdmin update(UmsAdmin entity) {
        return umsAdminRepository.save(entity);
    }

    @Override
    public void delete(UmsAdmin entity) {
        umsAdminRepository.delete(entity);
    }

    @Override
    public void delete(Long id) {
        umsAdminRepository.deleteById(id);
    }

    @Override
    public void deleteInBatch(List<UmsAdmin> entities) {
        umsAdminRepository.deleteInBatch(entities);
    }

    @Override
    public UmsAdmin find(Long id) {
         return umsAdminRepository.findById(id).get();
    }

    @Override
    public List<UmsAdmin> findAll() {
        return umsAdminRepository.findAll();
    }
}
