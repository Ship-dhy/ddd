package chenj.android.spriteweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/11/17 0017.
 */

public class ForeCast {
    public String date;

    @SerializedName("tmp")
    public Temperature temperature;

    @SerializedName("cond")
    public More more;
    public class More{
        @SerializedName("txt_d")
        public String info;
        public String code_d;
    }
    public class Temperature{
        public String max;
        public String min;
    }

    public Wind wind;
}
