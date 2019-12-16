package ru.panfio.telescreen.model.timesheet;

import java.util.Objects;

public class Tag {
    private String tagId;
    private String name;
    private boolean deleted;

    public Tag() {
    }

    public Tag(String tagId, String name) {
        this.tagId = tagId;
        this.name = name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId='" + tagId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return isDeleted() == tag.isDeleted() &&
                getTagId().equals(tag.getTagId()) &&
                getName().equals(tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTagId(), getName(), isDeleted());
    }
}
