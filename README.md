# largeProj01-hospital

Project was generated using ChatGPT on GPT-5.5

# Project: Hospital Emergency Room Simulation

## Overview

You are creating the backend software for a hospital emergency department. Patients arrive throughout the day with different medical conditions, and doctors become available at different times. Your system must determine who receives treatment, when they are treated, and how patient priorities change as time passes.

The emphasis of this project is **designing a realistic scheduling algorithm**, not creating a graphical interface.

---

# Learning Objectives

Your solution should demonstrate:

* Object-Oriented Programming
* Encapsulation
* Searching
* Sorting
* Collections (ArrayLists or similar)
* File I/O
* Algorithm design
* Data analysis
* Method decomposition

---

# Input Files

Create several text files to load into your program.

### Patients

Each patient should contain information similar to:

```
Patient ID
Name
Age
Arrival Time
Severity Level
Estimated Treatment Time
Special Condition
```

Example

```
1024,John Smith,42,08:15,4,35,None
```

---

### Doctors

Each doctor should have information such as

```
Doctor ID
Name
Specialty
Shift Start
Shift End
```

Example

```
5,Dr. Lee,Emergency,08:00,16:00
```

---

### Hospital Settings

Optional configuration file

```
Maximum Waiting Room Size
Priority Aging Rate
Maximum Doctors
```

---

# Core Classes

You should determine exactly how responsibilities are divided, but a successful design will likely require several interacting classes rather than one large controller class.

Your classes should each manage their own state and behavior instead of exposing data directly.

---

# Simulation

The simulation advances minute by minute.

During each minute, multiple events may occur.

Examples include:

* New patients arrive.
* Doctors finish treating patients.
* Doctors become available.
* Waiting patients may move up in priority.
* New treatment assignments are made.

The simulation continues until every patient has either been treated or otherwise removed from the system.

---

# Patient Severity

Each patient begins with a severity value.

Example scale:

```
1 = Minor
2 = Moderate
3 = Serious
4 = Critical
5 = Life-threatening
```

Higher severity patients generally receive treatment first.

However, severity should **not** be the only deciding factor.

---

# Dynamic Priority Algorithm

This is the heart of the project.

Design a scoring system that determines treatment order.

Your algorithm should consider multiple factors rather than relying on a single field.

Possible influences include:

* Severity
* Waiting time
* Age
* Special medical conditions
* Arrival order
* Estimated treatment time

You are responsible for deciding how these factors interact.

Your algorithm should be documented and consistently applied throughout the simulation.

---

# Aging Mechanic

Patients should not remain in the waiting room indefinitely.

As waiting time increases, their effective priority should also increase.

Design a rule that models this behavior.

For example:

* Increase priority after a fixed amount of waiting time.
* Apply a mathematical function based on wait duration.
* Use another strategy of your choosing.

---

# Doctor Assignment

When multiple doctors are available, determine which doctor should receive the next patient.

Your assignment algorithm should account for available information such as:

* Availability
* Specialty
* Workload
* Other relevant considerations

---

# Searching Requirements

Your system should support searching for:

* Patient ID
* Patient name
* Doctor ID
* Doctor name
* Patients currently waiting
* Patients currently being treated

Choose appropriate searching methods based on your data organization.

---

# Sorting Requirements

At various points, your program should produce sorted views of data.

Examples include:

* Highest priority patients
* Longest waiting patients
* Shortest treatment times
* Doctors by workload
* Patients by arrival time
* Completed treatments

Different reports may require different sorting criteria.

---

# Reports

Generate reports after the simulation completes.

Examples include:

### Waiting Statistics

* Average wait time
* Longest wait
* Shortest wait

---

### Doctor Statistics

* Number of patients treated
* Total treatment time
* Average treatment duration

---

### Hospital Statistics

* Average severity
* Total patients treated
* Peak waiting room size
* Simulation length

---

### Patient Outcomes

For every patient, record

* Arrival time
* Treatment start time
* Treatment completion time
* Final waiting time

---

# Additional Events

To create a more realistic simulation, introduce occasional events such as:

* Emergency arrivals that interrupt the normal queue.
* Doctors ending or beginning shifts.
* Patients leaving before treatment after waiting too long.
* Temporary doctor unavailability.
* Multiple patients arriving simultaneously.

These events should integrate naturally into your scheduling algorithm.

---

# Design Expectations

Your project should make thoughtful use of object-oriented design. Responsibilities should be divided logically among classes, avoiding large "god classes" that handle every task.

Methods should be focused on single responsibilities, and complex operations should be broken into smaller helper methods where appropriate.

---

# Deliverables

Your finished program should:

* Read all input data from files.
* Simulate the emergency room from start to finish.
* Apply your priority algorithm consistently.
* Produce clear reports and statistics.
* Allow searching and sorted reports.
* Handle invalid or unexpected input gracefully.

---

# Stretch Goals (Optional)

If you finish early, consider adding one or more advanced features:

* Multiple hospital departments (Emergency, Pediatrics, Trauma)
* Patients requiring specialists
* Multiple waiting rooms
* Patients whose condition worsens over time
* Limited treatment rooms
* Ambulance arrivals with preemption
* Nurse triage before doctor assignment
* Random patient generation
* Comparison of different triage algorithms (e.g., pure severity vs. dynamic priority vs. first-come-first-served) using the same dataset


