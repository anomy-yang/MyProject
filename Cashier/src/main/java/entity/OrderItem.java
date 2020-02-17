package entity;

import lombok.Data;

@Data
public class OrderItem {

    private Integer id;
    private String orderId;
    private Integer goodsId;
    private String goodsName;
    private String goodsIntroduce;
    private Integer goodsNum;
    private String goodsUnit;
    private Integer goodsPrice;//以分为单位，存入数据库为整数
    private Integer goodsDiscount;


    public double getGoodsPrice(){
        return goodsPrice * 1.0 / 100;
    }

    public int getGoodsPriceInt(){
        return goodsPrice;
    }
}
