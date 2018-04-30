package com.company.project.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author shanchao
 * @date 2018-04-27
 */
@Data
@Accessors(chain = true)
public class PageFilter<T> {

    @NotNull(message = "页数不能为空")
    @Min(value = 1, message = "页数最小为1")
    private Integer page = 1;
    @NotNull(message = "每页条数不能为空")
    @Max(value = 100, message = "每页条数不能超过100")
    @Min(value = 1, message = "每页条数最小为1")
    @JsonProperty("page_size")
    private Integer pageSize = 10;
    @JsonProperty("order_by")
    private List<OrderBy> orderBy;

    /**
     * 其他的筛选参数组成的Bean
     */
    private T t;

    public String exportOrderByToString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (orderBy != null && orderBy.size() > 0) {
            for (OrderBy order : orderBy) {
                stringBuilder.append(order.getField() + " " + order.getOrder().name() + ",");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        } else {
            return "id DESC";
        }
    }


}
