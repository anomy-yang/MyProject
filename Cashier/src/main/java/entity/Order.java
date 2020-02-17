package entity;

import common.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private String id;
    private Integer account_id;
    private String account_name;
    private String create_time;
    private String finish_time;
    private Integer actual_mount;
    private Integer total_money;

    private OrderStatus order_status;

    public OrderStatus getOrder_status(){
        return order_status;
    }
    //浏览订单使用，返回其对应的描述
    public String getOrder_statusDesc(){
        return order_status.getDesc();
    }

    public List<OrderItem> orderItemList = new ArrayList<>();

    public double getTotal_Money(){
        return total_money * 1.0 / 100;
    }

    public int getTotal_MoneyInt(){
        return total_money;
    }

    public double getActual_Mount(){
        return actual_mount * 1.0 / 100;
    }

    public int getActual_MountInt(){
        return actual_mount;
    }

    public double getDiscount(){
        return Double.valueOf(String.format("%2f",this.getTotal_Money() - this.getActual_Mount()));
    }

}
