import java.util.HashMap;
import java.util.Map;

public class ScoreVisitor implements Visitor{

    HashMap<Teacher, Tuple> examScores = new HashMap<>();
    HashMap<Assistant, Tuple> partialScores = new HashMap<>();

    @Override
    public void visit(Assistant assistant) {
        for (Map.Entry<Assistant, Tuple> entry: partialScores.entrySet()){
            for(Course c: Catalog.getInstance().courses){
                if(c.getName().equals(entry.getValue().courseName)){
                    String[] studentName = entry.getValue().student.split(" ");
                    for(Student s: c.getAllStudents() ){
                        if(s.toString().equals(entry.getValue().student) && entry.getKey().toString().equals(assistant.toString())){
                            int cont = 0;
                            for(Grade g: c.getGrades()){
                                if(g.getStudent().equals(s)){
                                    g.setPartialScore(entry.getValue().grade);
                                    cont = 1;
                                }
                            }
                            if(cont==0)
                                c.addGrade(new Grade(entry.getValue().courseName, entry.getValue().grade, s));
                        }
                    }
                }
            }
        }
    }

    @Override
    public void visit(Teacher teacher) {
        for (Map.Entry<Teacher, Tuple> entry: examScores.entrySet()){
            for(Course c: Catalog.getInstance().courses){
                if(c.getName().equals(entry.getValue().courseName)){
                    String[] studentName = entry.getValue().student.split(" ");
                    for(Student s: c.getAllStudents() ){
                        if(s.toString().equals(entry.getValue().student) && entry.getKey().toString().equals(teacher.toString())){
                            int cont = 0;
                            for(Grade g: c.getGrades()){
                                if(g.getStudent().equals(s)){
                                    g.setExamScore(entry.getValue().grade);
                                    cont = 1;
                                }
                            }
                            if(cont==0)
                                c.addGrade(new Grade(entry.getValue().courseName, s, entry.getValue().grade));
                        }
                    }
                }
            }
        }
    }
}

class Tuple {
    public String student;
    public String courseName;
    public Double grade;

    public Tuple(String student, String courseName, Double grade){
        this.student = student;
        this.courseName = courseName;
        this.grade = grade;
    }
}
