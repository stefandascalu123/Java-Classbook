public class Notification {
    Grade grade;

    public Notification(Grade grade){
        this.grade = grade;
    }

    @Override
    public String toString(){

        if(grade.getPartialScore() == null){
            return grade.getStudent() + " a primit nota (examen) " + grade.getExamScore() + " la " + grade.getCourse();
        }

        if(grade.getExamScore() == null){
            return grade.getStudent() + " a primit nota (partial) " + grade.getPartialScore() + " la " + grade.getCourse();
        }

        return grade.getStudent() + " are nota totala " + grade.getTotal() + " la " + grade.getCourse();
    }
}
