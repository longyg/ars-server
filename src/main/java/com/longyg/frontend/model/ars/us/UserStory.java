package com.longyg.frontend.model.ars.us;

import java.util.ArrayList;
import java.util.List;

public class UserStory extends Base{
    private List<SubTask> subTasks = new ArrayList<>();

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(List<SubTask> subTasks) {
        this.subTasks = subTasks;
    }
}
