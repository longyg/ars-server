package com.longyg.frontend.model.ars.us;

import com.longyg.frontend.model.ne.NeRelease;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "us")
public class UserStorySpec {
    @Id
    private String id;
    private NeRelease ne;
    private Title title;
    private List<AdapInfo> adapInfos = new ArrayList<>();
    private List<UserStory> userStories = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NeRelease getNe() {
        return ne;
    }

    public void setNe(NeRelease ne) {
        this.ne = ne;
    }

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public List<AdapInfo> getAdapInfos() {
        return adapInfos;
    }

    public void setAdapInfos(List<AdapInfo> adapInfos) {
        this.adapInfos = adapInfos;
    }

    public List<UserStory> getUserStories() {
        return userStories;
    }

    public void setUserStories(List<UserStory> userStories) {
        this.userStories = userStories;
    }
}
