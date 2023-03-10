import java.util.HashMap;

public interface Strategy {
    Student getBestStudent(HashMap<Student, Grade> studs);
}
