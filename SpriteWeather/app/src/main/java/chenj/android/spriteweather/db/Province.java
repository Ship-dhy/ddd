package chenj.android.spriteweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省的类，对应数据库
 * Created by Administrator on 2017/11/16 0016.
 */

public class Province extends DataSupport {
    private int id;//省对应数据库ID
    private String name;//省名称
    private int provinceId;//省对应ID

    public Province() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
