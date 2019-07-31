package com.laka.live.bean;
/*
 * @ClassName: BaseComment
 * @Description: 评论实体的基类
 * @Author: chuan
 * @Version: 1.0
 * @Date: 11/23/16
 */

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.laka.live.util.Common;

public class BaseComment implements Parcelable {

    private boolean isPraised = false;

    @Expose
    @SerializedName(Common.ID)
    private int id;

    @Expose
    @SerializedName("course_id")
    private int courseId;

    @Expose
    @SerializedName(Common.CONTENT)
    private String content;

    @Expose
    @SerializedName(Common.USER_ID)
    private int userId;

    @Expose
    @SerializedName(Common.CREATE_TIME)
    private int cerateTime;

    @Expose
    @SerializedName(Common.NICK_NAME)
    private String nickname;

    @Expose
    @SerializedName(Common.PRAISE_COUNT)
    private int praiseCount;

    @Expose
    @SerializedName(Common.AVATAR)
    private String avatar;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCerateTime() {
        return cerateTime;
    }

    public void setCerateTime(int cerateTime) {
        this.cerateTime = cerateTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

//    public boolean isPraised() {
//        return isPraised;
//    }
//
//    public void setPraised(boolean praised) {
//        isPraised = praised;
//    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isPraised ? (byte) 1 : (byte) 0);
        dest.writeInt(this.id);
        dest.writeInt(this.courseId);
        dest.writeString(this.content);
        dest.writeInt(this.userId);
        dest.writeInt(this.cerateTime);
        dest.writeString(this.nickname);
        dest.writeInt(this.praiseCount);
        dest.writeString(this.avatar);
    }

    public BaseComment() {
    }

    protected BaseComment(Parcel in) {
        this.isPraised = in.readByte() != 0;
        this.id = in.readInt();
        this.courseId = in.readInt();
        this.content = in.readString();
        this.userId = in.readInt();
        this.cerateTime = in.readInt();
        this.nickname = in.readString();
        this.praiseCount = in.readInt();
        this.avatar = in.readString();
    }

    public static final Creator<BaseComment> CREATOR = new Creator<BaseComment>() {
        @Override
        public BaseComment createFromParcel(Parcel source) {
            return new BaseComment(source);
        }

        @Override
        public BaseComment[] newArray(int size) {
            return new BaseComment[size];
        }
    };

    @Override
    public String toString() {
        return "BaseComment{" +
                "isPraised=" + isPraised +
                ", id=" + id +
                ", courseId=" + courseId +
                ", content='" + content + '\'' +
                ", userId=" + userId +
                ", cerateTime=" + cerateTime +
                ", nickname='" + nickname + '\'' +
                ", praiseCount=" + praiseCount +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
