package ro.pub.cs.systems.eim.practical_test02;

public class Alarm {
    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    private String hour;
    private String minute;

    public Alarm(String hour, String minute) {
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                '}';
    }
}
