package edu.uga.cs.quizapp;

public class Result {
    private long id;
    private String time;
    private int score;
    private int position;
    private String question1;
    private String question2;
    private String question3;
    private String question4;
    private String question5;
    private String question6;

    public Result() {
        this.id = -1;
        this.time = null;
        this.score = 0;
        this.position = 0;
        this.question1 = null;
        this.question2 = null;
        this.question3 = null;
        this.question4 = null;
        this.question5 = null;
        this.question6 = null;
    }

    public Result(String time, int score, int position, String question1, String question2, String question3, String question4, String question5, String question6) {
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    public String getQuestion6() {
        return question6;
    }

    public void setQuestion6(String question6) {
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
