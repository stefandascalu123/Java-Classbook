import java.util.ArrayList;

public class Catalog implements Subject{

    private static Catalog catalog = null;
    public ArrayList<Course> courses = new ArrayList<Course>();

    private final ArrayList<Observer> observers = new ArrayList<>();

    public ScoreVisitor scoreVisitor = new ScoreVisitor();

    public static Catalog getInstance(){
        if(catalog == null){
            catalog = new Catalog();
        }
        return catalog;
    }

    public void addCourse(Course course){
        courses.add(course);
    }

    public void removeCourse(Course course){
        courses.remove(course);
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add((Parent) observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove((Parent) observer);
    }

    @Override
    public void notifyObservers(Grade grade) {
        for(Observer observer: observers) {
            if (grade.getStudent().getMother() != null && grade.getStudent().getMother().equals(observer)) {
                observer.update(new Notification(grade));
            }

            if (grade.getStudent().getFather() != null && grade.getStudent().getFather().equals(observer)) {
                observer.update(new Notification(grade));
            }
        }
    }
}
