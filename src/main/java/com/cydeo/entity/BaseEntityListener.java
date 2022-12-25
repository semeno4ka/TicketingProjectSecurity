package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {
    @PrePersist//should be called before entity is inserted to DB
    private void onPrePersist(BaseEntity baseEntity){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //who logged in
    baseEntity.setInsertDateTime(LocalDateTime.now());
    baseEntity.setLastUpdateDateTime(LocalDateTime.now());
    if(authentication != null && !authentication.getName().equals("anonymousUser")){
        Object principal = authentication.getPrincipal();
        baseEntity.setInsertUserId(((UserPrincipal) principal).getId());
        // gets Id from whoever logged in from UserPrinciple
        baseEntity.setLastUpdateUserId( ((UserPrincipal) principal).getId());
        }
    }
    @PreUpdate//should be called before updates on entity are performed
    private void onPreUpdate(BaseEntity baseEntity){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        baseEntity.setLastUpdateDateTime(LocalDateTime.now());
        if(authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.setLastUpdateUserId( ((UserPrincipal) principal).getId());
        }
    }



}
