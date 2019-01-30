package com.joo.model.dto.limited;


import java.util.List;

/**
 * repository에서 interface 확인 용도로 만듦
 */
public interface LimitedBoard {


    public Long getIdx();
    public String getSubject();
    public String getContents();
    public int getHits();
    public List<LimitedFile> getFileList();
}
