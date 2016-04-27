package com.github.hilo.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Class that represents a User in the domain layer.
 */
public class User implements Serializable {

	public User() {}

	public User(boolean error) {this.error = error;}

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

  /*  private final int userId;

    public User(int userId) {
        this.userId = userId;
    }

    private String coverUrl;
    private String fullName;
    private String email;
    private String description;
    private int followers;

    public int getUserId() {
        return userId;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("***** User Details *****\n");
        stringBuilder.append("id=" + this.getUserId() + "\n");
        stringBuilder.append("cover url=" + this.getCoverUrl() + "\n");
        stringBuilder.append("fullname=" + this.getFullName() + "\n");
        stringBuilder.append("email=" + this.getEmail() + "\n");
        stringBuilder.append("description=" + this.getDescription() + "\n");
        stringBuilder.append("followers=" + this.getFollowers() + "\n");
        stringBuilder.append("*******************************");

        return stringBuilder.toString();
    }*/
}
