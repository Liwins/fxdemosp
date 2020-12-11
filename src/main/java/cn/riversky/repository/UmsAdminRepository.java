package cn.riversky.repository;

import cn.riversky.domain.UmsAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author riversky
 * @date 2020/12/11
 **/
@Repository
public interface UmsAdminRepository extends JpaRepository<UmsAdmin, Long> {
    UmsAdmin findByEmail(String email);
}
