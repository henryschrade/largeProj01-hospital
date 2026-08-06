import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main{
    public static void main(String[] args){
        ArrayList<Doctor> doctors = new ArrayList<>();
        ArrayList<Patient> patients = new ArrayList<>();


        //doctor    
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


        //patient
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

        /* print out all doctors and patients 
        System.out.println("Loaded " + doctors.size() + " doctors and " + patients.size() + " patients.");
        System.out.println(doctors);
        System.out.println(patients);
    */
   
        Scanner inputScanner = new Scanner(System.in);
        // int currentTime = readCurrentTime(inputScanner);
        int currentTime = parseTimeToMinutes("10:30");
        int[][] sortedPatientPriorities = buildPriorityMatrix(patients, currentTime);
        System.out.println("Sorted patient priorities:");
        for (int[] patientPriority : sortedPatientPriorities) {
            System.out.println("Priority=" + patientPriority[0] + ", PatientID=" + patientPriority[1]);
        }
    }

    private static int[][] buildPriorityMatrix(ArrayList<Patient> patients, int currentTime) {
        int[][] priorityMatrix = new int[patients.size()][2];

        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            int waitingTime = calculateWaitingTime(patient.getArrivalTime(), currentTime);
            int[] priorityInfo = patient.getPriorityAndId(waitingTime);
            priorityMatrix[i][0] = priorityInfo[0];
            priorityMatrix[i][1] = priorityInfo[1];
        }

        Arrays.sort(priorityMatrix, (left, right) -> {
            int priorityCompare = Integer.compare(right[0], left[0]);
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            return Integer.compare(left[1], right[1]);
        });

        return priorityMatrix;
    }

    private static int readCurrentTime(Scanner scanner) {
        System.out.print("Enter current time (HH:mm or HHmm): ");
        String input = scanner.nextLine().trim();
        return parseTimeToMinutes(input);
    }

    private static int parseTimeToMinutes(String timeValue) {
        String normalized = timeValue.replace(":", "");
        if (normalized.length() == 3) {
            normalized = "0" + normalized;
        }

        int hours = Integer.parseInt(normalized.substring(0, 2));
        int minutes = Integer.parseInt(normalized.substring(2, 4));
        return (hours * 60) + minutes;
    }

    private static int calculateWaitingTime(String arrivalTime, int currentTime) {
        int arrivalMinutes = parseTimeToMinutes(arrivalTime);
        return Math.max(0, currentTime - arrivalMinutes);
    }
}
     /* 
        need to create algo to determine patient order and doctor assignment
            make arraylist of doctors and patients - done
                methods to sort doctors by doctoralgo
                methods to sort patients by patientalgo - done
            main method to find doctor for the highest priority patient (tie = lower patientID) - done
                    priority =
                        (severity × 100)
                        + waitingTime
                        + (10 if special condition)
                        + (5 if age < 18 or age >= 65)   
                                 
                if no doctor, then wait until next doctor is available
                then, recheck list and assign doctor to next patient
                if multiple doctors then choose based on 
                    priority = # of patients worked with (less is better) > shift start time > doctorID
                once doctor is assigned, remove patient from list and doctor, updating relevant values
            repeat forever
        */
        
    
