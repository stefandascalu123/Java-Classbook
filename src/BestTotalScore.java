import java.util.HashMap;
import java.util.Map;

public class BestTotalScore implements Strategy{
    @Override
    public Student getBestStudent(HashMap<Student, Grade> studs) {
        Student best = null;
        Double grade = (double) 0;
        for(Map.Entry<Student, Grade> entry : studs.entrySet()){
            if(entry.getValue().getTotal() > grade){
                best = entry.getKey();
            }
        }
        return best;
    }
}
