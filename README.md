# My Projects
This repository contains multiple past projects that I have completed.

## Contact Information
**Email:** ben_cavanagh@outlook.com  
**Phone:** 0490483292

## DataAnalysis
**Summary:** Created a program to analyse data taken from images of elderly participants performing daily activities.

**Details:**
- **Language Used:** Java  
- **Classes:** Main class ('DataAnalysis'), 'ImageRecord' class, and 'ActivityRecord' class.
- **Dataset:** Images organised by activity classes and participants that capture daily activities of 26 elderly people.
- **Process:**
  1. Search through all dataset folders to create a collection of image records.
  2. Sort image records chronologically for each participant.
  3. Output sorted records into a file as a long table.
  4. For each participant, define start and end time for all their activities, calculate duration of activities, and determine a representative image for each activity.
  5. Print a separate activity table for each participant containing the above information.

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
**Summary:** Team project to develop a progam to help create a functional and visually-appealing marble machine.

**Details:**
- **Language Used:** C++
- **Goal:** To create a box that guides marbles through the box from the entrance to the exit.
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
