import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Group extends ArrayList<Student> {
    private Assistant assistant;
    private String ID;

    public void setAssistant(Assistant assistant) {
        this.assistant = assistant;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Assistant getAssistant() {
        return assistant;
    }

    public String getID() {
        return ID;
    }



    public void addStudent(Student student){
        add(student);
    }

    public void removeStudent(Student student){
        remove(student);
    }

    public Group(String ID, Assistant assistant, Comparator<Student> comp){
        this.ID = ID;
        this.assistant = assistant;
        Collections.sort(this, comp);
    }

    public Group(String ID, Assistant assistant){
        this.ID = ID;
        this.assistant = assistant;
        Collections.sort(this, new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });
    }
}
