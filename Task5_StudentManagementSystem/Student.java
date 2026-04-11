public class Student {
    private String name;
    private int rollNumber;
    private String grade;
    private String email;
    private String phone;

    public Student(String name, int rollNumber, String grade, String email, String phone) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.grade = grade;
        this.email = email;
        this.phone = phone;
    }

    public String getName()       { return name; }
    public int getRollNumber()    { return rollNumber; }
    public String getGrade()      { return grade; }
    public String getEmail()      { return email; }
    public String getPhone()      { return phone; }

    public void setName(String name)    { this.name = name; }
    public void setGrade(String grade)  { this.grade = grade; }
    public void setEmail(String email)  { this.email = email; }
    public void setPhone(String phone)  { this.phone = phone; }

    public String toFileString() {
        return rollNumber + "," + name + "," + grade + "," + email + "," + phone;
    }

    public static Student fromFileString(String line) {
        String[] parts = line.split(",", 5);
        if (parts.length != 5) return null;
        return new Student(parts[1], Integer.parseInt(parts[0]), parts[2], parts[3], parts[4]);
    }

    @Override
    public String toString() {
        return String.format("| %-5d | %-20s | %-6s | %-25s | %-12s |",
                rollNumber, name, grade, email, phone);
    }
}
