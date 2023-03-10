import java.util.*;

public abstract class Course{
    private final String name;
    private final Teacher teacher;
    private final ArrayList<Assistant> assistants;
    private final ArrayList<Grade> grades;
    private final HashMap<String, Group> groups;
    private final Integer creditPoints;

    private final Strategy strategy;

    public Course(CourseBuilder builder){
        this.name = builder.name;
        this.teacher = builder.teacher;
        this.assistants = builder.assistants;
        this.grades = builder.grades;
        this.groups = builder.groups;
        this.creditPoints = builder.creditPoints;
        this.strategy = builder.strategy;
    }


    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }

    public ArrayList<Grade> getGrades() {
        return grades;
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }

    public Integer getCreditPoints() {
        return creditPoints;
    }


    public void addAssistant(String ID, Assistant assistant){
        if(!assistants.contains(assistant)) {
            assistants.add(assistant);
        }
        Group group = groups.get(ID);
        group.setAssistant(assistant);
    }

    public  void addStudent(String ID, Student student){
        Group group = groups.get(ID);
        group.addStudent(student);
    }

    public void addGroup(Group group){
        groups.put(group.getID(), group);
    }

    public void addGroup(String ID, Assistant assistant){
        groups.put(ID, new Group(ID, assistant));
    }

    public void addGroup(String ID, Assistant assistant, Comparator<Student> comp){
        groups.put(ID, new Group(ID, assistant, comp));
    }

    public Grade getGrade(Student student){
        for (Grade g: grades ) {
            if (g.getStudent() == student){
                return g;
            }
        }
        return null;
    }

    public void addGrade(Grade grade){
        grades.add(grade);
    }

    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> allStudents = new ArrayList<Student>();
        for (Map.Entry<String, Group> entry : groups.entrySet()){
            Group g = entry.getValue();
            allStudents.addAll(g);
        }
        return allStudents;
    }

    public HashMap<Student, Grade> getAllStudentGrades(){
        HashMap<Student, Grade> studentGradeHashMap = new HashMap<>();
        for(Grade g: grades){
            studentGradeHashMap.put(g.getStudent(), g);
        }
        return studentGradeHashMap;

    }

    public abstract ArrayList<Student> getGraduatedStudents();

    public Student getBestStudent(){
        return strategy.getBestStudent(getAllStudentGrades());
    }

    public static class CourseBuilder{
        private final String name;
        private final Teacher teacher;
        private  ArrayList<Assistant> assistants;
        private  ArrayList<Grade> grades;
        private  HashMap<String, Group> groups;
        private  Integer creditPoints;
        private Strategy strategy;

        public CourseBuilder(String name, Teacher teacher, Integer creditPoints){
            this.name = name;
            this.teacher = teacher;
            this.creditPoints = creditPoints;
        }

        public CourseBuilder assistants(ArrayList<Assistant> assistants){
            this.assistants = assistants;
            return  this;
        }

        public CourseBuilder grades(ArrayList<Grade> grades){
            this.grades = grades;
            return this;
        }

        public CourseBuilder groups(HashMap<String, Group> groups){
            this.groups = groups;
            return  this;
        }

        public CourseBuilder strategy(Strategy strategy){
            this.strategy = strategy;
            return this;
        }

        public Course build(){
            return new Course(this) {

                @Override
                public ArrayList<Student> getGraduatedStudents() {
                    return null;
                }
            };
        }
    }

}


