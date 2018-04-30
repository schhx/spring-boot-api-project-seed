package com.company.project.core;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderBy {

    private String field;
    private OrderBy.OrderType order;

    public OrderBy() {
    }

    public OrderBy(String field, OrderBy.OrderType order) {
        this.field = field;
        this.order = order;
    }

    public static enum OrderType {
        ASC(1),
        DESC(-1);

        private int value;

        private OrderType(int order) {
            this.value = order;
        }

        public int getValue() {
            return this.value;
        }
    }
}
