package com.company.project.core;

import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;

/**
 * @author shanchao
 * @date 2018-04-27
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ListResult<T> {

    private long total;

    private List<T> results;

    public ListResult(){
        this.total = 0;
        this.results = Collections.emptyList();
    }

    public ListResult(PageInfo<T> pageInfo){
        this.total = pageInfo.getTotal();
        this.results = pageInfo.getList();
    }

    public ListResult emptyListResult(){
        return new ListResult();
    }
}
