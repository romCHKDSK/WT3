public class Student {
    private String firstname;
    private String lastname;
    private int age;
    private String specialty;
    private int group;

    public Student(String firstname, String lastname, int age, String specialty, int group) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.age = age;
        this.specialty = specialty;
        this.group = group;
    }

    public Student() {
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Student: " +
                "name='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", specialty='" + specialty + '\'' +
                ", group='" + group + '\'';
    }
}


