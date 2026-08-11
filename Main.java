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
        while (true) {
            System.out.print("Enter a time (HH:mm) or 'end day': ");
            String input = inputScanner.nextLine().trim();
            if (input.equalsIgnoreCase("end day")) {
                ArrayList<Doctor> doctorSnapshot = cloneDoctors(doctors);
                ArrayList<Patient> patientSnapshot = clonePatients(patients);
                simulateToTime(patientSnapshot, doctorSnapshot, parseTimeToMinutes("19:00"));
                printEndOfDayStats(patientSnapshot, doctorSnapshot, parseTimeToMinutes("19:00"));
                break;
            }

            int queryTime;
            try {
                queryTime = parseTimeToMinutes(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid time format. Use HH:mm or enter 'end day'.");
                continue;
            }

            if (queryTime < parseTimeToMinutes("07:30") || queryTime > parseTimeToMinutes("19:00")) {
                System.out.println("Time must be between 07:30 and 19:00.");
                continue;
            }

                ArrayList<Doctor> doctorSnapshot = cloneDoctors(doctors);
            ArrayList<Patient> patientSnapshot = clonePatients(patients);
            simulateToTime(patientSnapshot, doctorSnapshot, queryTime);
            printSnapshot(patientSnapshot, doctorSnapshot, queryTime);
        }
    }

    private static ArrayList<Doctor> cloneDoctors(ArrayList<Doctor> doctors) {
        ArrayList<Doctor> copy = new ArrayList<>();
        for (Doctor doctor : doctors) {
            copy.add(doctor.copy());
        }
        return copy;
    }

    private static ArrayList<Patient> clonePatients(ArrayList<Patient> patients) {
        ArrayList<Patient> copy = new ArrayList<>();
        for (Patient patient : patients) {
            copy.add(patient.copy());
        }
        return copy;
    }

    private static void simulateToTime(ArrayList<Patient> patients, ArrayList<Doctor> doctors, int queryTime) {
        for (int currentTime = parseTimeToMinutes("07:30"); currentTime <= queryTime; currentTime += 30) {
            runPriorityMixesSilent(patients, doctors, currentTime);
        }
    }

    private static void runPriorityMixesSilent(ArrayList<Patient> patients, ArrayList<Doctor> doctors, int currentTime) {
        while (!patients.isEmpty()) {
            int[][] sortedPatientPriorities = buildPriorityMatrix(patients, currentTime);
            if (sortedPatientPriorities.length == 0) {
                break;
            }

            int[][] sortedDoctorPreferences = buildDoctorMatrix(doctors, currentTime);
            if (sortedDoctorPreferences.length == 0) {
                break;
            }

            Doctor assignedDoctor = null;
            Patient assignedPatient = null;

            for (int[] patientPriority : sortedPatientPriorities) {
                Patient candidatePatient = findPatientById(patients, patientPriority[1]);
                if (candidatePatient == null) {
                    continue;
                }

                for (int[] doctorPreference : sortedDoctorPreferences) {
                    Doctor candidateDoctor = findDoctorById(doctors, doctorPreference[2]);
                    if (candidateDoctor == null) {
                        continue;
                    }

                    if (candidateDoctor.canTreat(candidatePatient.getEstimatedTreatmentTime(), currentTime)) {
                        assignedDoctor = candidateDoctor;
                        assignedPatient = candidatePatient;
                        break;
                    }
                }

                if (assignedDoctor != null) {
                    break;
                }
            }

            if (assignedDoctor == null || assignedPatient == null) {
                break;
            }

            assignedDoctor.startTreatment(assignedPatient.getId(), assignedPatient.getName(), assignedPatient.getEstimatedTreatmentTime(), currentTime);
            patients.remove(assignedPatient);
        }
    }

    private static void printSnapshot(ArrayList<Patient> patients, ArrayList<Doctor> doctors, int queryTime) {
        System.out.println("\n===== Snapshot at " + formatTime(queryTime) + " =====");

        System.out.println("Doctors currently treating patients:");
        boolean anyTreating = false;
        for (Doctor doctor : doctors) {
            if (doctor.isBusy(queryTime)) {
                anyTreating = true;
                String patientName = doctor.getCurrentPatientName();
                if (patientName == null) {
                    patientName = "unknown";
                }
                System.out.println("- " + doctor.getName() + " treating " + patientName + " until " + formatTime(doctor.getBusyUntil()));
            }
        }
        if (!anyTreating) {
            System.out.println("- None");
        }

        System.out.println("\nDoctors available and waiting:");
        anyTreating = false;
        for (Doctor doctor : doctors) {
            if (doctor.isAvailable(queryTime) && !doctor.isBusy(queryTime)) {
                anyTreating = true;
                System.out.println("- " + doctor.getName() + " (treated " + doctor.getPatientsTreated() + " patients so far)");
            }
        }
        if (!anyTreating) {
            System.out.println("- None");
        }

        System.out.println("\nWaiting patients and priorities:");
        int[][] waiting = buildPriorityMatrix(patients, queryTime);
        if (waiting.length == 0) {
            System.out.println("- None");
        } else {
            for (int[] row : waiting) {
                Patient patient = findPatientById(patients, row[1]);
                if (patient != null) {
                    System.out.println("- " + patient.getName() + ": priority=" + row[0]);
                }
            }
        }
    }

    private static int[][] buildPriorityMatrix(ArrayList<Patient> patients, int currentTime) {
        ArrayList<int[]> priorityRows = new ArrayList<>();

        for (Patient patient : patients) {
            if (!patient.hasArrived(currentTime)) {
                continue;
            }
            int waitingTime = calculateWaitingTime(patient.getArrivalTime(), currentTime);
            int[] priorityInfo = patient.getPriorityAndId(waitingTime);
            priorityRows.add(new int[]{priorityInfo[0], priorityInfo[1]});
        }

        priorityRows.sort((left, right) -> {
            int priorityCompare = Integer.compare(right[0], left[0]);
            if (priorityCompare != 0) {
                return priorityCompare;
            }
            return Integer.compare(left[1], right[1]);
        });

        int[][] priorityMatrix = new int[priorityRows.size()][2];
        for (int i = 0; i < priorityRows.size(); i++) {
            priorityMatrix[i] = priorityRows.get(i);
        }

        return priorityMatrix;
    }

    private static int[][] buildDoctorMatrix(ArrayList<Doctor> doctors, int currentTime) {
        ArrayList<int[]> doctorRows = new ArrayList<>();

        for (Doctor doctor : doctors) {
            if (!doctor.isAvailable(currentTime)) {
                continue;
            }

            int[] sortInfo = doctor.getSortInfo(currentTime);
            doctorRows.add(new int[]{sortInfo[0], sortInfo[1], sortInfo[2]});
        }

        doctorRows.sort((left, right) -> {
            int patientsTreatedCompare = Integer.compare(left[0], right[0]);
            if (patientsTreatedCompare != 0) {
                return patientsTreatedCompare;
            }
            int shiftCompare = Integer.compare(left[1], right[1]);
            if (shiftCompare != 0) {
                return shiftCompare;
            }
            return Integer.compare(left[2], right[2]);
        });

        int[][] doctorMatrix = new int[doctorRows.size()][3];
        for (int i = 0; i < doctorRows.size(); i++) {
            doctorMatrix[i] = doctorRows.get(i);
        }

        return doctorMatrix;
    }

    private static void runPriorityMixes(ArrayList<Patient> patients, ArrayList<Doctor> doctors, int currentTime) {
        while (!patients.isEmpty()) {
            int[][] sortedPatientPriorities = buildPriorityMatrix(patients, currentTime);
            if (sortedPatientPriorities.length == 0) {
                System.out.println("No patients have arrived by " + formatTime(currentTime) + ".");
                break;
            }

            int[][] sortedDoctorPreferences = buildDoctorMatrix(doctors, currentTime);
            if (sortedDoctorPreferences.length == 0) {
                System.out.println("No doctors available at " + formatTime(currentTime) + ". ");
                break;
            }

            Doctor assignedDoctor = null;
            Patient assignedPatient = null;

            for (int[] patientPriority : sortedPatientPriorities) {
                Patient candidatePatient = findPatientById(patients, patientPriority[1]);
                if (candidatePatient == null) {
                    continue;
                }

                for (int[] doctorPreference : sortedDoctorPreferences) {
                    Doctor candidateDoctor = findDoctorById(doctors, doctorPreference[2]);
                    if (candidateDoctor == null) {
                        continue;
                    }

                    if (candidateDoctor.canTreat(candidatePatient.getEstimatedTreatmentTime(), currentTime)) {
                        assignedDoctor = candidateDoctor;
                        assignedPatient = candidatePatient;
                        break;
                    }
                }

                if (assignedDoctor != null) {
                    break;
                }
            }

            if (assignedDoctor == null || assignedPatient == null) {
                System.out.println("No available doctor can finish treatment for the arrived patients at " + formatTime(currentTime) + ".");
                break;
            }

            assignedDoctor.startTreatment(assignedPatient.getId(), assignedPatient.getName(), assignedPatient.getEstimatedTreatmentTime(), currentTime);
            patients.remove(assignedPatient);

            System.out.println("Assigned Doctor " + assignedDoctor.getName() + " to Patient " + assignedPatient.getName() + " at " + formatTime(currentTime) + " for " + assignedPatient.getEstimatedTreatmentTime() + " minutes.");
        }
    }

    private static Doctor findDoctorById(ArrayList<Doctor> doctors, int doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getId() == doctorId) {
                return doctor;
            }
        }
        return null;
    }

    private static Patient findPatientById(ArrayList<Patient> patients, int patientId) {
        for (Patient patient : patients) {
            if (patient.getId() == patientId) {
                return patient;
            }
        }
        return null;
    }

    private static String formatTime(int currentTime) {
        int hours = currentTime / 60;
        int minutes = currentTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    private static void printEndOfDayStats(ArrayList<Patient> patients, ArrayList<Doctor> doctors, int currentTime) {
        System.out.println("\n===== End of day summary =====");

        if (patients.isEmpty()) {
            System.out.println("All patients have been assigned.");
        } else {
            System.out.println("Remaining patients and priorities at " + formatTime(currentTime) + ":");
            int[][] remaining = buildPriorityMatrix(patients, currentTime);
            if (remaining.length == 0) {
                for (Patient patient : patients) {
                    System.out.println("- " + patient.getName() + " (arrives at " + patient.getArrivalTime() + ")");
                }
            } else {
                for (int[] row : remaining) {
                    Patient patient = findPatientByPriority(patients, row[1]);
                    if (patient != null) {
                        System.out.println("- " + patient.getName() + ": priority=" + row[0]);
                    }
                }
            }
        }

        int maxTreated = 0;
        for (Doctor doctor : doctors) {
            maxTreated = Math.max(maxTreated, doctor.getPatientsTreated());
        }

        System.out.println("Top treating doctor(s) with " + maxTreated + " patient(s):");
        for (Doctor doctor : doctors) {
            if (doctor.getPatientsTreated() == maxTreated) {
                System.out.println("- " + doctor.getName());
            }
        }
    }

    private static Patient findPatientByPriority(ArrayList<Patient> patients, int patientId) {
        for (Patient patient : patients) {
            if (patient.getId() == patientId) {
                return patient;
            }
        }
        return null;
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
        need to create algo to determine patient order and doctor assignment - done
            make arraylist of doctors and patients - done
                methods to sort doctors by doctoralgo - done
                methods to sort patients by patientalgo - done
            main method to find doctor for the highest priority patient (tie = lower patientID) - done
                    priority = - done
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

       /*  - done
            when doctor available
            treat highest priority patient
            update matrices, patient status = being treated
            record time when treatment started
            when matrix next called on, check time
            if doctor should be done, status = available & patients treated +1
       */
        
    
