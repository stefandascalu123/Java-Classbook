public class Teacher extends User implements Element{
    public Teacher(String firstName, String lastName) {
        super(firstName, lastName);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
        for (Course c: Catalog.getInstance().courses){
            for(Grade g: c.getGrades()){
                Catalog.getInstance().notifyObservers(g);
            }
        }
    }
}
