import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PartialCourse extends Course{

    public PartialCourse(CourseBuilder builder) {
        super(builder);
    }

    @Override
    public ArrayList<Student> getGraduatedStudents() {
        ArrayList<Student> graduatedStudents = new ArrayList<Student>();
        HashMap<Student, Grade> grades= getAllStudentGrades();
        for(Map.Entry<Student, Grade> entry : grades.entrySet()){
            Grade g = (Grade) entry.getValue();
            if(g.getTotal() >= 5){
                graduatedStudents.add((Student) entry.getKey());
            }
        }
        return graduatedStudents;
    }

    public static class PartialCourseBuilder extends CourseBuilder{
        public PartialCourseBuilder(String name, Teacher teacher, Integer creditPoints) {
            super(name, teacher, creditPoints);
        }
    }

}
