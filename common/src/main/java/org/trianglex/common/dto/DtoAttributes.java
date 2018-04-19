package org.trianglex.common.dto;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;

public interface DtoAttributes<T> extends Serializable {

    /**
     * DTO object to PO object
     *
     * @return
     */
    default T toPO(T t) {
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
