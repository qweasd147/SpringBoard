package com.joo.model.dto.limited;

/**
 * repository에서 interface 확인 용도로 만듦
 */
public interface LimitedFile {

    public Long getIdx();
    public String getContentType();
    public String getOriginFileName();
    public long getFileSize();
}
