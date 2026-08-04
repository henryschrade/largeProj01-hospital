public class Doctor{
    private int id;
    private String name;
    private String specialty;
    private String shiftStart;
    private String shiftEnd;
    private int patientsTreated;

    public Doctor(int id, String name, String specialty, String shiftStart, String shiftEnd) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.patientsTreated = 0;
    }

   

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", specialty='" + specialty + '\'' +
                ", shiftStart='" + shiftStart + '\'' +
                ", shiftEnd='" + shiftEnd + '\'' +
                '}';
    }
}
