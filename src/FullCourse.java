import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullCourse extends Course{
    public FullCourse(CourseBuilder builder) {
        super(builder);
    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<Student>();
        HashMap<Student, Grade> grades= getAllStudentGrades();
        for(Map.Entry<Student, Grade> entry : grades.entrySet()){
            Grade g = (Grade) entry.getValue();
            if(g.getPartialScore()>=3 && g.getExamScore()>= 2){
                graduatedStudents.add((Student) entry.getKey());
            }
        }
        return graduatedStudents;
    }

    public static class FullCourseBuilder extends CourseBuilder{

        public FullCourseBuilder(String name, Teacher teacher, Integer creditPoints) {
            super(name, teacher, creditPoints);
        }
    }
}
