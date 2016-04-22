package com.github.hilo.data.entity;

import java.io.Serializable;
import java.util.List;

/**
 * User Entity used in the data layer.
 */
public class UserEntity implements Serializable {
    /**
     * error : false
     * results : ["2016-04-22","2016-04-21","2016-04-20","2016-04-19","2016-04-18","2016-04-15","2016-04-14","2016-04-13","2016-04-12","2016-04-11","2016-04-08","2016-04-07","2016-04-06","2016-04-05","2016-04-01","2016-03-31","2016-03-30","2016-03-29","2016-03-28","2016-03-25","2016-03-24","2016-03-23","2016-03-22","2016-03-21","2016-03-18","2016-03-17","2016-03-16","2016-03-15","2016-03-14","2016-03-11","2016-03-10","2016-03-09","2016-03-08","2016-03-07","2016-03-04","2016-03-03","2016-03-02","2016-03-01","2016-02-29","2016-02-26","2016-02-25","2016-02-24","2016-02-23","2016-02-22","2016-02-19","2016-02-18","2016-02-17","2016-02-16","2016-02-15","2016-02-04","2016-02-03","2016-02-02","2016-02-01","2016-01-29","2016-01-28","2016-01-27","2016-01-26","2016-01-25","2016-01-22","2016-01-21","2016-01-20","2016-01-19","2016-01-18","2016-01-15","2016-01-14","2016-01-13","2016-01-12","2016-01-11","2016-01-08","2016-01-07","2016-01-06","2016-01-05","2016-01-04","2015-12-31","2015-12-30","2015-12-29","2015-12-28","2015-12-25","2015-12-24","2015-12-23","2015-12-22","2015-12-21","2015-12-18","2015-12-17","2015-12-16","2015-12-15","2015-12-14","2015-12-11","2015-12-10","2015-12-09","2015-12-08","2015-12-07","2015-12-04","2015-12-03","2015-12-02","2015-12-01","2015-11-30","2015-11-27","2015-11-26","2015-11-25","2015-11-24","2015-11-23","2015-11-20","2015-11-19","2015-11-18","2015-11-17","2015-11-16","2015-11-13","2015-11-12","2015-11-11","2015-11-10","2015-11-09","2015-11-06","2015-11-05","2015-11-04","2015-11-03","2015-11-02","2015-10-30","2015-10-29","2015-10-28","2015-10-27","2015-10-26","2015-10-23","2015-10-22","2015-10-21","2015-10-20","2015-10-19","2015-10-16","2015-10-15","2015-10-14","2015-10-13","2015-10-12","2015-10-10","2015-10-09","2015-10-08","2015-09-30","2015-09-29","2015-09-28","2015-09-25","2015-09-24","2015-09-23","2015-09-22","2015-09-21","2015-09-18","2015-09-17","2015-09-16","2015-09-15","2015-09-14","2015-09-11","2015-09-10","2015-09-09","2015-09-08","2015-09-07","2015-09-06","2015-09-01","2015-08-31","2015-08-28","2015-08-27","2015-08-26","2015-08-25","2015-08-24","2015-08-21","2015-08-20","2015-08-19","2015-08-18","2015-08-17","2015-08-14","2015-08-13","2015-08-12","2015-08-11","2015-08-10","2015-08-07","2015-08-06","2015-08-05","2015-08-03","2015-07-31","2015-07-30","2015-07-29","2015-07-28","2015-07-27","2015-07-24","2015-07-23","2015-07-22","2015-07-21","2015-07-20","2015-07-17","2015-07-16","2015-07-15","2015-07-14","2015-07-13","2015-07-10","2015-07-09","2015-07-08","2015-07-07","2015-07-03","2015-07-02","2015-07-01","2015-06-30","2015-06-29","2015-06-26","2015-06-19","2015-06-18","2015-06-17","2015-06-16","2015-06-15","2015-06-12","2015-06-11","2015-06-10","2015-06-09","2015-06-05","2015-06-04","2015-06-03","2015-06-02","2015-05-29","2015-05-28","2015-05-27","2015-05-26","2015-05-25","2015-05-22","2015-05-21","2015-05-20","2015-05-19","2015-05-18"]
     */

    private boolean error;
    private List<String> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }



   /* @SerializedName("id")
    private int userId;

    @SerializedName("cover_url")
    private String coverUrl;

    @SerializedName("full_name")
    private String fullname;

    @SerializedName("description")
    private String description;

    @SerializedName("followers")
    private int followers;

    @SerializedName("email")
    private String email;

    public UserEntity() {
        //empty
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** User Entity Details *****\n");
        stringBuilder.append("id=" + this.getUserId() + "\n");
        stringBuilder.append("cover url=" + this.getCoverUrl() + "\n");
        stringBuilder.append("fullname=" + this.getFullname() + "\n");
        stringBuilder.append("email=" + this.getEmail() + "\n");
        stringBuilder.append("description=" + this.getDescription() + "\n");
        stringBuilder.append("followers=" + this.getFollowers() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }*/
}
