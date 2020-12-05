package chenj.android.spriteweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class Basic {
    //城市名称
    @SerializedName("city")
    public String citiName;
    //weatherId
    @SerializedName("id")
    public String weatherId;

    //更新信息
    public Update update;

    public class Update{
        //最近更新时间
        public String loc;
    }
}
