package chenj.android.spriteweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * 建议
 * Created by Administrator on 2017/11/17 0017.
 */

public class Suggestion {
    public Air air;

    @SerializedName("cw")
    public WashCar washCar;

    @SerializedName("drsg")
    public Dress dress;

    @SerializedName("trav")
    public Tourism tourism;

    @SerializedName("uv")
    public Ultraviolet ultraviolet;

    public Sport sport;


    public class Air{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class WashCar{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Dress{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Sport{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Tourism{
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

    public class Ultraviolet {
        @SerializedName("brf")
        public String brf;
        @SerializedName("txt")
        public String info;
    }

}
