package com.github.reomor.photoapp.api.users.ui.model;

import java.io.Serializable;

public class AlbumResponseModel implements Serializable {
    private static final long serialVersionUID = -7974250393022441177L;

    private String albumId;
    private String userId;
    private String name;
    private String description;

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
