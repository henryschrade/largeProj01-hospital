import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        ArrayList<Doctor> doctors = new ArrayList<>();
        ArrayList<Patient> patients = new ArrayList<>();

        try {
            Scanner doctorScanner = new Scanner(new File("doctors.txt"));
            while (doctorScanner.hasNextLine()) {
                String line = doctorScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String specialty = parts[2].trim();
                    String shiftStart = parts[3].trim();
                    String shiftEnd = parts[4].trim();
                    doctors.add(new Doctor(id, name, specialty, shiftStart, shiftEnd));
                }
            }
            doctorScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("doctors.txt not found: " + e.getMessage());
        }

        try {
            Scanner patientScanner = new Scanner(new File("patients.txt"));
            while (patientScanner.hasNextLine()) {
                String line = patientScanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    int age = Integer.parseInt(parts[2].trim());
                    String arrivalTime = parts[3].trim();
                    int severityLevel = Integer.parseInt(parts[4].trim());
                    int estimatedTreatmentTime = Integer.parseInt(parts[5].trim());
                    String specialCondition = parts[6].trim();
                    patients.add(new Patient(id, name, age, arrivalTime, severityLevel, estimatedTreatmentTime, specialCondition));
                }
            }
            patientScanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("patients.txt not found: " + e.getMessage());
        }

        System.out.println("Loaded " + doctors.size() + " doctors and " + patients.size() + " patients.");
        System.out.println(doctors);
        System.out.println(patients);
    }
}
     /* 
        need to create algo to determine patient order and doctor assignment
            make arraylist of doctors and patients - done
                methods to sort doctors by specialty + shift avaliable
                methods to sort patients by severity level + arrival time
            main method to find doctor for the highest priority patient
                    priority =
                        (severity × 100)
                        + waitingTime
                        + (10 if special condition)
                        + (5 if age < 18 or age >= 65)                
                if no doctor, then wait until next doctor is available
                then, recheck list and assign doctor to next patient
                if multiple doctors then choose based on 
                    specaility > # of patients worked with (less is better) > shift start time > alphabetical
                once doctor is assigned, remove patient from list and doctor, updating relevant values
            repeat forever
        */
        
    
