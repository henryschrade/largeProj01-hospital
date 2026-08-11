public class Doctor{
    private int id;
    private String name;
    private String specialty;
    private String shiftStart;
    private String shiftEnd;
    private int patientsTreated;
    private int busyUntil;
    private int treatmentStartTime;
    private int currentPatientId;
    private String currentPatientName;

    public Doctor(int id, String name, String specialty, String shiftStart, String shiftEnd) {
        this.id = id;
        this.name = name;
        this.specialty = specialty;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.patientsTreated = 0;
        this.busyUntil = -1;
        this.treatmentStartTime = -1;
        this.currentPatientId = -1;
        this.currentPatientName = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShiftStart() {
        return shiftStart;
    }

    public String getShiftEnd() {
        return shiftEnd;
    }

    public String getSpecialty() {
        return specialty;
    }

    public boolean isAvailable(int currentTime) {
        if (busyUntil > currentTime) {
            return false;
        }

        int shiftStartMinutes = parseTimeToMinutes(shiftStart);
        int shiftEndMinutes = parseTimeToMinutes(shiftEnd);
        return currentTime >= shiftStartMinutes && currentTime < shiftEndMinutes;
    }

    public boolean canTreat(int treatmentTime, int currentTime) {
        if (!isAvailable(currentTime)) {
            return false;
        }

        int shiftEndMinutes = parseTimeToMinutes(shiftEnd);
        return currentTime + treatmentTime <= shiftEndMinutes;
    }

    public void startTreatment(int patientId, String patientName, int treatmentTime, int currentTime) {
        this.currentPatientId = patientId;
        this.currentPatientName = patientName;
        this.treatmentStartTime = currentTime;
        this.busyUntil = currentTime + treatmentTime;
        this.patientsTreated++;
    }

    public int[] getSortInfo(int currentTime) {
        int shiftStartMinutes = parseTimeToMinutes(shiftStart);
        int minutesSinceShiftStart = Math.max(0, currentTime - shiftStartMinutes);
        return new int[]{patientsTreated, minutesSinceShiftStart, id};
    }

    public int getBusyUntil() {
        return busyUntil;
    }

    public int getCurrentPatientId() {
        return currentPatientId;
    }

    public String getCurrentPatientName() {
        return currentPatientName;
    }

    public boolean isBusy(int currentTime) {
        return busyUntil > currentTime;
    }

    public Doctor copy() {
        return new Doctor(id, name, specialty, shiftStart, shiftEnd);
    }

    public int getPatientsTreated() {
        return patientsTreated;
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
