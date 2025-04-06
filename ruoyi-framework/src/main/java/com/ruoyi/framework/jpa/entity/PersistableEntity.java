package com.ruoyi.framework.jpa.entity;

import com.ruoyi.common.utils.uuid.IdUtils;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
@DynamicInsert
@DynamicUpdate
public abstract class PersistableEntity implements Persistable<String> {

    @Transient
    private boolean isNew = false;

    @Transient
    public boolean isNew() {
        return isNew;
    }

    @Transient
    public void markCreated() {
        this.isNew = true;
        this.setId(IdUtils.fastUUID());
    }

    @Transient
    public abstract void setId(String id);

}
