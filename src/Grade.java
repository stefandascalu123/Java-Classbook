import java.util.Objects;

public class Grade implements Comparable<Grade>, Cloneable{

    private Double partialScore, examScore;
    private Student student;
    private String course;

    public Grade(String course, Double partialScore, Student student, Double examScore){
        this.course = course;
        this.partialScore = partialScore;
        this.student = student;
        this.examScore = examScore;
    }

    public Grade(String course, Student student, Double examScore){
        this.course = course;
        this.student = student;
        this.examScore = examScore;
    }

    public Grade(String course, Double partialScore, Student student){
        this.course = course;
        this.partialScore = partialScore;
        this.student = student;
    }

    public void setPartialScore(Double partialScore) {
        this.partialScore = partialScore;
    }

    public void setExamScore(Double examScore) {
        this.examScore = examScore;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public Double getPartialScore() {
        return partialScore;
    }

    public Double getExamScore() {
        return examScore;
    }

    public Student getStudent() {
        return student;
    }

    public String getCourse() {
        return course;
    }

    public Double getTotal(){
        return partialScore + examScore;
    }

    @Override
    public int compareTo(Grade grade) {
        if(this.getTotal() > grade.getTotal()){
            return 1;
        }
        if(Objects.equals(this.getTotal(), grade.getTotal())){
            return 0;
        }
        return -1;
    }
}
