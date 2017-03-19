package coloring;

/**
 * Created by yeedle on 3/13/17.
 */
public class Course {
    private String title;
    private int TimeSlot;


    public Course(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(int timeSlot) {
        TimeSlot = timeSlot;
    }

    @Override
    public String toString() {
        return title;
    }
}
