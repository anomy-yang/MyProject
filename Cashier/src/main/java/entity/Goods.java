package entity;

import lombok.Data;

@Data
public class Goods {
    private Integer id;
    private String name;
    private String introduce;
    private Integer stock;
    private String unit;//单位  包、袋
    private Integer price;//以分为单位，存入数据库为整数
    private Integer discount;

    private Integer buyGoodsNum;//购买数量

    public double getPrice(){
        return price * 1.0 / 100;
    }

    public int getPriceInt(){
        return price;
    }

}
