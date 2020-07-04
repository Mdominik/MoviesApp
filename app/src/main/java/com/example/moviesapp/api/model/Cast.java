package com.example.moviesapp.api.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cast  implements Parcelable {

    @SerializedName("cast_id")
    @Expose
    private Integer castId;
    @SerializedName("character")
    @Expose
    private String character;
    @SerializedName("credit_id")
    @Expose
    private String creditId;
    @SerializedName("gender")
    @Expose
    private Integer gender;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("profile_path")
    @Expose
    private String profilePath;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("job")
    @Expose
    private String job;

    protected Cast(Parcel in) {
        if (in.readByte() == 0) {
            castId = null;
        } else {
            castId = in.readInt();
        }
        character = in.readString();
        creditId = in.readString();
        if (in.readByte() == 0) {
            gender = null;
        } else {
            gender = in.readInt();
        }
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            order = null;
        } else {
            order = in.readInt();
        }
        department = in.readString();
        job = in.readString();
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public Integer getCastId() {
        return castId;
    }

    public void setCastId(Integer castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (castId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(castId);
        }
        parcel.writeString(character);
        parcel.writeString(creditId);
        if (gender == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(gender);
        }
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(name);
        if (order == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(order);
        }
        parcel.writeString(department);
        parcel.writeString(job);
    }
}