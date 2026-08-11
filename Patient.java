public class Patient{
    private int id;
    private String name;
    private int age;
    private String arrivalTime;
    private int severityLevel;
    private int estimatedTreatmentTime;
    private String specialCondition;

    public Patient(int id, String name, int age, String arrivalTime, int severityLevel,
                   int estimatedTreatmentTime, String specialCondition) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.arrivalTime = arrivalTime;
        this.severityLevel = severityLevel;
        this.estimatedTreatmentTime = estimatedTreatmentTime;
        this.specialCondition = specialCondition;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean hasArrived(int currentTime) {
        int arrivalMinutes = parseTimeToMinutes(arrivalTime);
        return arrivalMinutes <= currentTime;
    }

    private int parseTimeToMinutes(String timeValue) {
        String normalized = timeValue.replace(":", "");
        if (normalized.length() == 3) {
            normalized = "0" + normalized;
        }

        int hours = Integer.parseInt(normalized.substring(0, 2));
        int minutes = Integer.parseInt(normalized.substring(2, 4));
        return (hours * 60) + minutes;
    }

    public int getEstimatedTreatmentTime() {
        return estimatedTreatmentTime;
    }

    public Patient copy() {
        return new Patient(id, name, age, arrivalTime, severityLevel, estimatedTreatmentTime, specialCondition);
    }

    public int[] getPriorityAndId(int waitingTime) {
        int priority = (severityLevel * 100) + waitingTime;

        boolean hasSpecialCondition = specialCondition != null
                && !specialCondition.trim().isEmpty()
                && !specialCondition.equalsIgnoreCase("none")
                && !specialCondition.equalsIgnoreCase("no");

        if (hasSpecialCondition) {
            priority += 10;
        }

        if (age < 18 || age >= 65) {
            priority += 5;
        }

        return new int[]{priority, id};
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", severityLevel=" + severityLevel +
                ", estimatedTreatmentTime=" + estimatedTreatmentTime +
                ", specialCondition='" + specialCondition + '\'' +
                '}';
    }
}
