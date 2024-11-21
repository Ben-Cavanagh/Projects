# Ben Cavanagh - Undergraduate Projects
This repository contains multiple past projects completed during undergraduate years.

## Contact Information
**Email:** ben_cavanagh@outlook.com  
**Phone:** 0490483292

## Software Engineering Research Thesis
**Summary:** Created an algorithm to visualise spatio-temporal ADL data taken from images of elderly participants performing daily activities.

**Details:**
- **Language Used:** Java  
- **Classes:** 'Main' class, 'ImageRecord' class, and 'ActivityRecord' class.
- **Dataset:** Images categorised by activity classes and participants that capture daily activities of 26 elderly people.
- **Process:**
  1. Iterate through dataset and parse image data from multiple sources
  2. Sort image data records chronologically for each participant
  3. Identify and delete duplicate/outlier image records
  4. Define the "activities" for each participant using the processed image records
  5. Calculate the total combined time duration for all instances of each of a participant's activity classes
  6. Output the tabular results into an Excel file
  7. Create an "activity-by-activity view" visualisation file for every participant
  8. Create a "summary view" visualisation file for every participant
  9. Create a single "summary view" visualisation file containing the data for all participants
- **Note:** Cannot show code due to university intellectual property, however, the processes are detailed in the Thesis Report

## VivaMQ
**Summary:** Team-based web application project to process a student's written submission and generate oral examination questions powered by AI

**Details:**
- **Language Used:** JavaScript
- **Backend Tools Used:** Docker, Swagger, Prisma
- **Approach:** Establish a database and create numerous backend API endpoints to be called from the frontend to store and retrieve records
- **Final Deployed Application:** https://vivamq.app/
- **Note:** Cannot show full solution due to university intellectual property, however, example API endpoints/validators are provided

## Unity Game
**Summary:** Developed a 2D video game in Unity

**Details:**
- **Language Used:** C#
- **Game:** You, a wizard, must defend enchanted mushrooms from roaming ghouls by shooting spellbolts at them
- **Approach:** Attach scripts to Unity game objects to give them their intended behaviour

## JobScheduler1
**Summary:** Developed a client-side job-scheduling algorithm based on a Largest Round-Robin (LRR) approach.

**Details:**
- **Language Used:** Java
- **Process:**
  1. Connect to the server (simulator) and receive a list of jobs.
  2. Schedule jobs to the largest server type (the server with the largest number of cores).
  3. Close the server connection when completed.

## JobScheduler2
**Summary:** Developed an improved client-side job-scheduling algorithm that optimises overall turnaround time.

**Details:**
- **Language Used:** Java
- **Goal:** To reduce job waiting time to reduce turnaround time.
- **Process:**
  1. Connect to the server (simulator) and receive a list of jobs.
  2. Prioritise scheduling of jobs based on server availability and resources:
      - Highest priority: Servers that have sufficient resources and no waiting and running jobs at the same time.
      - Second priority: Servers that have sufficient resources but may have waiting and running jobs at the same time.
      - Third priority: Servers that are eventually capable of having sufficient resources.
  3. Close the server connection when completed.

## LoadBalancer
**Summary:** Implemented an efficient load distribution algorithm for a matrix of processors using a "greedy algorithm" approach.

**Details:**
- **Language Used:** Java
- **Goal:** To balance load among a matrix of processors.
- **Process**
  1. Identify the average load and sum of each row/column in the matrix.
  2. Compare splitting the matrix horizontally and vertically using two different approaches for each.
  3. Choose the best result for the most optimal load distribution.
  4. Output the maximum load and sub-array coordinates for each processor.
  
## Marble Machine
**Summary:** Engineering project software to enable the functionality and control of physical electronic components of a box called the Marble Machine.

**Details:**
- **Language Used:** C++
- **Goal:** To plan, design, and implement a program for the motor and LED components of the box to guide marbles through the box from the entrance to the exit.
- **Components:** LEDs and 2 types of motors.
- **Subsystems:**
  - Rotating ferris wheel.
  - Oscillating staircase.
  - LED strips.

## OptimalMovesAndWinnings
**Summary:** Developed an efficient solution to determine optimal strategies and guaranteed winnings in a coin-pile game based on dynamic programming.

**Details:**
- **Language Used:** Java
- **Game:** One line of piles of coins of varying sizes. Two players take turns in choosing a pile of coins from either end and attempts to maximise their own winnings.
- **Approach:** Use dynamic programming to determine optimal moves and guaranteed winnings for both players.
