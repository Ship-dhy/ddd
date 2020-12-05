package chenj.android.spriteweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县
 * Created by Administrator on 2017/11/16 0016.
 */

public class County extends DataSupport {
    private int id;//县对应数据库ID
    private String name;//县名称
    private String weatherId;//县对应天气预报的ID
    private int cityId;//县对应市的id
    public County() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
