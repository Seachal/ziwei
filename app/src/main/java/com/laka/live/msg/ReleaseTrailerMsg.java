package com.laka.live.msg;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

/**
 * Created by Lyf on 2017/4/7.
 */

public class ReleaseTrailerMsg extends Msg {

    @Expose
    @SerializedName(Common.DATA)
    public releaseTrialer data;


    public class releaseTrialer{

        @Expose
        @SerializedName(Common.TRAILER_ID)
        public String trailer_id; // 课程预告id

        @Expose
        @SerializedName(Common.COURSE_ID)
        public String course_id; // 课程详情id

    }


    public String getCourseId(){
        return data.course_id;
    }

    public String getTrailerId(){
        return data.trailer_id;
    }

}
