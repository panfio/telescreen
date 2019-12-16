package ru.panfio.telescreen.model.timesheet;

public class TaskTag {
     private String id;
     private String tagId;
     private String taskId;

     public TaskTag() {
     }

     public TaskTag(String id, String tagId, String taskId) {
          this.id = id;
          this.tagId = tagId;
          this.taskId = taskId;
     }

     public String getId() {
          return id;
     }

     public void setId(String id) {
          this.id = id;
     }

     public String getTagId() {
          return tagId;
     }

     public void setTagId(String tagId) {
          this.tagId = tagId;
     }

     public String getTaskId() {
          return taskId;
     }

     public void setTaskId(String taskId) {
          this.taskId = taskId;
     }
}
