package chenj.android.spriteweather.db;

import org.litepal.crud.DataSupport;

/**
 * 市
 * Created by Administrator on 2017/11/16 0016.
 */

public class City extends DataSupport {
    private int id;//数据库的id
    private String name;//城市名称
    private int cityId;//城市对应ID
    private int provinceId;//该城市对应省份列表
    public City() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
