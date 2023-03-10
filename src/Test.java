import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws  IOException, ParseException {

        Catalog.getInstance();


        FileReader f = new FileReader("src/catalog.json");
        JSONObject parser = (JSONObject) new JSONParser().parse(f);

        Test test = new Test();
        test.parseCourses(parser);
        test.parseExamScores(parser, Catalog.getInstance().scoreVisitor);
        test.parsePartialScores(parser, Catalog.getInstance().scoreVisitor);


        for(Course c: Catalog.getInstance().courses){
            c.getTeacher().accept(Catalog.getInstance().scoreVisitor);
            for(Assistant a: c.getAssistants()){
                a.accept(Catalog.getInstance().scoreVisitor);
            }
        }


        StudentPage studentPage = new StudentPage("Student Page", Catalog.getInstance().courses.get(0).getAllStudents().get(0));
        TeacherPage teacherPage = new TeacherPage("Teacher Page", Catalog.getInstance().courses.get(3).getTeacher());
        AssistantPage assistantPagePage = new AssistantPage("Assistant Page", Catalog.getInstance().courses.get(0).getAssistants().get(0));
        ParentPage parentPage = new ParentPage("Parent Page",Catalog.getInstance().courses.get(0).getAllStudents().get(1).getFather());
    }
    public void printCourse(){
        for(Course c: Catalog.getInstance().courses){
            System.out.println(c.getName());
            System.out.println("Assistants:");
            for (Assistant a: c.getAssistants()){
                System.out.println(a);
            }
            System.out.println("Students:");
            for(Student s: c.getAllStudents()){
                System.out.println(s);
            }
            System.out.println("\n");
        }
    }

    public void parseCourses(JSONObject parser){
        JSONArray courses = (JSONArray) parser.get("courses");

        for(Object obj : courses){
            String courseType = (String) ((JSONObject) obj).get("type");
            String courseStrategy = (String) ((JSONObject) obj).get("strategy");
            String courseName = (String) ((JSONObject) obj).get("name");

            String teacherFirst = (String) ((JSONObject) ((JSONObject) obj).get("teacher")).get("firstName");
            String teacherLast = (String) ((JSONObject) ((JSONObject) obj).get("teacher")).get("lastName");

            JSONArray courseAssistants = (JSONArray) ((JSONObject) obj).get("assistants");
            ArrayList<Assistant> assistants = new ArrayList<>();
            for(Object assistant: courseAssistants){
                String assistantFirst = (String) ((JSONObject) assistant).get("firstName");
                String assistantLast = (String) ((JSONObject) assistant).get("lastName");

                assistants.add((Assistant) UserFactory.getUser("Assistant", assistantFirst, assistantLast));
            }

            JSONArray courseGroups = (JSONArray) ((JSONObject) obj).get("groups");
            ArrayList<Group> groups = new ArrayList<>();
            for (Object group: courseGroups){
                String ID = (String) ((JSONObject) group).get("ID");

                String assistantFirst = (String) ((JSONObject) ((JSONObject) group).get("assistant")).get("firstName");
                String assistantLast = (String) ((JSONObject) ((JSONObject) group).get("assistant")).get("lastName");

                JSONArray groupStudents = (JSONArray) ((JSONObject) group).get("students");
                ArrayList<Student> students = new ArrayList<>();
                for(Object student: groupStudents){
                    String studentFirst = (String) ((JSONObject) student).get("firstName");
                    String studentLast = (String) ((JSONObject) student).get("lastName");

                    String motherFirst = null, motherLast = null;
                    if(((JSONObject) student).containsKey("mother")) {
                        motherFirst = (String) ((JSONObject) ((JSONObject) student).get("mother")).get("firstName");
                        motherLast = (String) ((JSONObject) ((JSONObject) student).get("mother")).get("lastName");
                    }

                    String fatherFirst = null, fatherLast = null;
                    if(((JSONObject) student).containsKey("father")) {
                        fatherFirst = (String) ((JSONObject) ((JSONObject) student).get("father")).get("firstName");
                        fatherLast = (String) ((JSONObject) ((JSONObject) student).get("father")).get("lastName");
                    }

                    Student tempStud = (Student) UserFactory.getUser("Student", studentFirst, studentLast);

                    if(((JSONObject) student).containsKey("mother")) {
                        Parent mother = (Parent) UserFactory.getUser("Parent", motherFirst, motherLast);
                        Catalog.getInstance().addObserver(mother);
                        tempStud.setMother(mother);
                    }

                    if(((JSONObject) student).containsKey("father")) {
                        Parent father = (Parent) UserFactory.getUser("Parent", fatherFirst, fatherLast);
                        Catalog.getInstance().addObserver(father);
                        tempStud.setFather(father);
                    }

                    students.add(tempStud);
                }

                Group tempGroup = new Group(ID, (Assistant) UserFactory.getUser("Assistant", assistantFirst, assistantLast));
                tempGroup.addAll(students);

                groups.add(tempGroup);
            }

            HashMap<String, Group> courseGroups1 = new HashMap<>();
            for(Group g: groups){
                courseGroups1.put(g.getID(), g);
            }


            if(courseType.equals("FullCourse")){
                Catalog.getInstance().addCourse(new FullCourse.FullCourseBuilder(courseName, (Teacher) UserFactory.getUser("Teacher", teacherFirst, teacherLast), 5)
                        .assistants(assistants)
                        .grades(new ArrayList<Grade>())
                        .groups(courseGroups1)
                        .build());
            }

            if(courseType.equals("PartialCourse")){
                Catalog.getInstance().addCourse(new PartialCourse.PartialCourseBuilder(courseName, (Teacher) UserFactory.getUser("Teacher", teacherFirst, teacherLast), 5)
                        .assistants(assistants)
                        .grades(new ArrayList<Grade>())
                        .groups(courseGroups1)
                        .build());
            }
        }
    }

    public void parseExamScores(JSONObject parser, ScoreVisitor scoreVisitor){
        JSONArray examScores = (JSONArray) parser.get("examScores");

        for(Object obj: examScores){
            String teacherFirst = (String) ((JSONObject) ((JSONObject) obj).get("teacher")).get("firstName");
            String teacherLast = (String) ((JSONObject) ((JSONObject) obj).get("teacher")).get("lastName");

            String studentFirst = (String) ((JSONObject) ((JSONObject) obj).get("student")).get("firstName");
            String studentLast = (String) ((JSONObject) ((JSONObject) obj).get("student")).get("lastName");

            String course = (String) ((JSONObject) obj).get("course");

            Double grade = (Double) ((JSONObject) obj).get("grade");

            scoreVisitor.examScores.put((Teacher) UserFactory.getUser("Teacher", teacherFirst, teacherLast), new Tuple(studentFirst + " " + studentLast, course, grade));
            }
        }
    public void parsePartialScores(JSONObject parser, ScoreVisitor scoreVisitor){
        JSONArray partialScores = (JSONArray) parser.get("partialScores");

        for(Object obj: partialScores){
            String assistantFirst = (String) ((JSONObject) ((JSONObject) obj).get("assistant")).get("firstName");
            String assistantLast = (String) ((JSONObject) ((JSONObject) obj).get("assistant")).get("lastName");

            String studentFirst = (String) ((JSONObject) ((JSONObject) obj).get("student")).get("firstName");
            String studentLast = (String) ((JSONObject) ((JSONObject) obj).get("student")).get("lastName");

            String course = (String) ((JSONObject) obj).get("course");

            Double grade = (Double) ((JSONObject) obj).get("grade");

            scoreVisitor.partialScores.put((Assistant) UserFactory.getUser("Assistant", assistantFirst, assistantLast), new Tuple(studentFirst + " " + studentLast, course, grade));

        }
    }
    }

    class StudentPage extends JFrame{
        JList<Object> courses;
        DefaultListModel<Object> listModel ;
        JTextField t1;
        JTextField t2;
        JLabel a1;
        JLabel a2;
        JLabel a3;

        JTextField t4;
        JTextField t5;

        Student student;

        public StudentPage(String name, Student student){
            super(name);
            this.student = student;
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 500);
            setMinimumSize(new Dimension(300, 300));
            JPanel p=new JPanel();
            p.setLayout(new GridLayout(3,1));

            listModel = new DefaultListModel<>();
            for(Course c: Catalog.getInstance().courses){
                for(Student stud: c.getAllStudents()){
                    if(stud.toString().equals(student.toString())){
                        listModel.addElement(c.getName());
                    }
                }
            }


            courses = new JList<>(listModel);
            courses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            courses.setLayoutOrientation(JList.VERTICAL);
            courses.setVisibleRowCount(-1);


            JScrollPane listScroller = new JScrollPane(courses);
            listScroller.setPreferredSize(new Dimension(250, 80));

            JPanel p2= new JPanel();
            p.setLayout((new GridLayout(6,1)));
            JPanel p3 = new JPanel();
            p.setLayout(new GridLayout(3,1));

            JLabel courseName = new JLabel("Course");
            JLabel teacherName = new JLabel("Teacher");
            JLabel assistantss = new JLabel("Assistants");

            JLabel partialLabel = new JLabel("Partial Grade:");
            JLabel examLabel = new JLabel("Exam grade:");

            t1 = new JTextField(30);
            t2 = new JTextField(30);

            t4 = new JTextField(4);
            t5 = new JTextField(4);

            a1 = new JLabel();
            a2 = new JLabel();
            a3 = new JLabel();


            p2.add(courseName);
            p2.add(t1);
            p2.add(teacherName);
            p2.add(t2);

            JPanel assistants = new JPanel();
            assistants.setLayout(new GridLayout(4, 1));
            assistants.add(assistantss);
            assistants.add(a1);
            assistants.add(a2);
            assistants.add(a3);

            p2.add(assistants);

            p3.add(partialLabel);
            p3.add(t4);
            p3.add(examLabel);
            p3.add(t5);


            p.add(courses);
            p.add(p2);
            p.add(p3);
            add(p, BorderLayout.CENTER);

            courses.addListSelectionListener(this::valueChange);

            pack();
            setVisible(true);
        }

        public void valueChange(ListSelectionEvent e){
            if(e.getValueIsAdjusting()){
                if(courses.getSelectedIndex() != -1){
                    String courseName = (String) courses.getSelectedValue();
                    String teacherName = null;
                    t1.setText(courseName);
                    for(Course c: Catalog.getInstance().courses){
                        if(c.getName().equals(courseName)) {
                            teacherName = c.getTeacher().toString();

                            t2.setText(teacherName);

                            if(c.getAssistants().size() >= 1){
                                a1.setText(c.getAssistants().get(0).toString());
                            }else {
                                a1.setText("");
                            }

                            if(c.getAssistants().size() >= 2){
                                a2.setText(c.getAssistants().get(1).toString());
                            }else {
                                a2.setText("");
                            }

                            if(c.getAssistants().size() >= 3){
                                a3.setText(c.getAssistants().get(2).toString());
                            }else {
                                a3.setText("");
                            }

                            Student ss = null;
                            for (Student stud : c.getAllStudents()) {
                                if (stud.toString().equals(student.toString())) {
                                    ss = stud;
                                }
                            }
                            Grade grade = c.getGrade(ss);

                            System.out.println(grade.getPartialScore() + " " + grade.getExamScore());

                            if (grade.getPartialScore() != null) {
                                t4.setText(grade.getPartialScore().toString());
                            }
                            if (grade.getExamScore() != null) {
                                t5.setText(grade.getExamScore().toString());
                            }
                        }
                    }

                    
                }
            }
        }
    }

    class TeacherPage extends JFrame{

        Teacher teacher;
        JList<Object> courses;

        DefaultListModel<Object> listModel ;
        JList<Object> grades = new JList<>();
        JButton button;


        public TeacherPage(String name, Teacher teacher){
            super(name);
            this.teacher = teacher;

            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 500);
            setMinimumSize(new Dimension(300, 300));

            listModel = new DefaultListModel<>();
            for(Course c: Catalog.getInstance().courses){
                if(c.getTeacher().toString().equals(teacher.toString())){
                    listModel.addElement(c.getName());
                }
            }

            JPanel p = new JPanel();
            p.setLayout(new GridLayout(3,1));

            courses = new JList<>(listModel);
            courses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            courses.setLayoutOrientation(JList.VERTICAL);
            courses.setVisibleRowCount(-1);


            JScrollPane listScroller = new JScrollPane(courses);
            listScroller.setPreferredSize(new Dimension(250, 80));

            button = new JButton();
            button.setText("Accept Grades");
            button.setSize(100, 50);
            button.addActionListener( new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    for(Course c: Catalog.getInstance().courses){
                        if(c.getTeacher().toString().equals(teacher.toString())){
                            c.getTeacher().accept(Catalog.getInstance().scoreVisitor);
                        }
                    }
                }
            });

            p.add(courses);
            p.add(grades);
            p.add(button);

            add(p, BorderLayout.CENTER);


            courses.addListSelectionListener(this::valueChange);

            pack();
            getContentPane().revalidate();
            repaint();
            setVisible(true);
        }

        public void valueChange(ListSelectionEvent e){
            if(e.getValueIsAdjusting()){
                if(courses.getSelectedIndex() != -1){
                        grades.removeAll();
                        DefaultListModel<Object> listModel1 = new DefaultListModel<>();
                        for(Map.Entry<Teacher, Tuple> entry : Catalog.getInstance().scoreVisitor.examScores.entrySet()){
                            if(entry.getKey().toString().equals(teacher.toString())){
                                listModel1.addElement(entry.getValue().student + " " + entry.getValue().grade.toString());
                            }
                        }
                        grades.setModel(listModel1);
                }
            }
        }
    }

    class AssistantPage extends JFrame{

    Assistant assistant;
    JList<Object> courses;

    DefaultListModel<Object> listModel ;
    JList<Object> grades = new JList<>();
    JButton button;


    public AssistantPage(String name, Assistant assistant){
        super(name);
        this.assistant = assistant;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 500);
        setMinimumSize(new Dimension(300, 300));

        listModel = new DefaultListModel<>();
        for(Course c: Catalog.getInstance().courses){
            if(c.getAssistants().contains(assistant)){
                listModel.addElement(c.getName());
            }
        }

        JPanel p = new JPanel();
        p.setLayout(new GridLayout(3,1));

        courses = new JList<>(listModel);
        courses.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courses.setLayoutOrientation(JList.VERTICAL);
        courses.setVisibleRowCount(-1);


        JScrollPane listScroller = new JScrollPane(courses);
        listScroller.setPreferredSize(new Dimension(250, 80));

        button = new JButton();
        button.setText("Accept Grades");
        button.setSize(100, 50);
        button.addActionListener( new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                for(Course c: Catalog.getInstance().courses){
                    for (Assistant a: c.getAssistants()){
                        if(a.toString().equals(assistant.toString())){
                            assistant.accept(Catalog.getInstance().scoreVisitor);
                        }
                    }
                }
            }
        });

        p.add(courses);
        p.add(grades);
        p.add(button);

        add(p, BorderLayout.CENTER);


        courses.addListSelectionListener(this::valueChange);

        pack();
        getContentPane().revalidate();
        repaint();
        setVisible(true);
    }

    public void valueChange(ListSelectionEvent e){
        if(e.getValueIsAdjusting()){
            if(courses.getSelectedIndex() != -1){
                grades.removeAll();
                DefaultListModel<Object> listModel1 = new DefaultListModel<>();
                for(Map.Entry<Assistant, Tuple> entry : Catalog.getInstance().scoreVisitor.partialScores.entrySet()){
                    if(entry.getKey().toString().equals(assistant.toString()) && entry.getValue().courseName.equals(courses.getSelectedValue().toString())){
                        listModel1.addElement(entry.getValue().student + " " + entry.getValue().grade.toString());
                    }
                }
                grades.setModel(listModel1);
            }
        }
    }
}

    class ParentPage extends JFrame{

        Parent parent;
        DefaultListModel<Object> listModel = new DefaultListModel<>();

        public ParentPage(String name, Parent parent){
            super(name);
            this.parent = parent;
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(1000, 500);
            setMinimumSize(new Dimension(300, 300));

            for(String notification: parent.notifications){
                System.out.println(notification);
                listModel.addElement(notification);
            }

            JPanel p = new JPanel();
            p.setLayout(new GridLayout(1,1));
            p.add(new JList<>(listModel));

            add(p, BorderLayout.CENTER);

            pack();
            getContentPane().revalidate();
            repaint();
            setVisible(true);
        }
    }

