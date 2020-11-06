package edu.uga.cs.quizapp;

public class Result {
    private long id;
    private String time;
    private int score;
    private long position;
    private long question1;
    private long question2;
    private long question3;
    private long question4;
    private long question5;
    private long question6;

    public Result() {
        this.id = -1;
        this.time = null;
        this.score = 0;
        this.position = 0;
        this.question1 = 0;
        this.question2 = 0;
        this.question3 = 0;
        this.question4 = 0;
        this.question5 = 0;
        this.question6 = 0;
    }

    public Result(String time, int score, int position, long question1, long question2, long question3, long question4, long question5, long question6) {
        this.id = -1;
        this.time = time;
        this.score = score;
        this.position = position;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.question4 = question4;
        this.question5 = question5;
        this.question6 = question6;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }

    public long getQuestion1() {
        return question1;
    }

    public void setQuestion1(long question1) {
        this.question1 = question1;
    }

    public long getQuestion2() {
        return question2;
    }

    public void setQuestion2(long question2) {
        this.question2 = question2;
    }

    public long getQuestion3() {
        return question3;
    }

    public void setQuestion3(long question3) {
        this.question3 = question3;
    }

    public long getQuestion4() {
        return question4;
    }

    public void setQuestion4(long question4) {
        this.question4 = question4;
    }

    public long getQuestion5() {
        return question5;
    }

    public void setQuestion5(long question5) {
        this.question5 = question5;
    }

    public long getQuestion6() {
        return question6;
    }

    public void setQuestion6(long question6) {
        this.question6 = question6;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", date='" + time + '\'' +
                ", score=" + score +
                ", position=" + position +
                ", question1='" + question1 + '\'' +
                ", question2='" + question2 + '\'' +
                ", question3='" + question3 + '\'' +
                ", question4='" + question4 + '\'' +
                ", question5='" + question5 + '\'' +
                ", question6='" + question6 + '\'' +
                '}';
    }
}
