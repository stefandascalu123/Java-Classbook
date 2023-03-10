import java.util.ArrayList;

public class Parent extends User implements Observer{

    ArrayList<String> notifications = new ArrayList<>();
    public Parent(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public void update(Notification notification) {
        if(!notifications.contains(notification.toString()))
            notifications.add(notification.toString());
        System.out.println(notification) ;
    }
}
