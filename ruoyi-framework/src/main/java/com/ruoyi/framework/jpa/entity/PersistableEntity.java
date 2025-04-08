package com.ruoyi.framework.jpa.entity;

import cn.hutool.core.util.IdUtil;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
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
        this.setId(IdUtil.getSnowflakeNextIdStr());
    }

    @Transient
    public abstract void setId(String id);

}
