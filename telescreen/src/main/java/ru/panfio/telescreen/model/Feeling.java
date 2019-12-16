package ru.panfio.telescreen.model;

public enum Feeling {
    NO(0),
    WELL (1),
    NORMAL (2),
    GRUMPY (3),
    BAD (4);

    private int feel;

    Feeling(int feel) {
        this.feel = feel;
    }

    public int get() {
        return feel;
    }

    @Override
    public String toString() {
        return "DayOfWeek{" +
                "title='" + feel + '\'' +
                '}';
    }
}
