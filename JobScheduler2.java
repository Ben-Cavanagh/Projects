// Import Java libraries.
import java.io.*;
import java.net.*;

public class JobScheduler2 {
	
	// Declare a set of common variables for all methods to use.
	static Socket socket;
	static BufferedReader input;
	static DataOutputStream output;
	static int numberOfServers;
	static String[][] serverRecords;
	static int numberOfInitialServers;
	static String[][] initialServerRecords;
	static String receivedMessage;
	static String schedulingServerType = "";
	static int schedulingServerID = 0;
	static boolean serverFound = false;
	static boolean firstServerFound = false;
	static int lowestWaitingTime = 0;
	
	// Declare a set of message variables for all methods to use.
	static String HELO = "HELO";
	static String AUTH = "AUTH " + System.getProperty("user.name");
	static String QUIT = "QUIT";
	static String REDY = "REDY";
	static String DATA = "DATA";
	static String JOBN = "JOBN";
	static String NONE = "NONE";
	static String GETS = "GETS Capable ";
	static String SCHD = "SCHD ";
	static String CNTJ = "CNTJ ";
	static String EJWT = "EJWT ";
	static String OK = "OK";
	
	/*
	* The following method is intended to be used to send a message from the client to the ds-server.
	*/
	public static void sendMessage(String sendingMessage){
		try{
			String messageOutput = sendingMessage;
			sendingMessage = sendingMessage + '\n';
			output = new DataOutputStream(socket.getOutputStream());
			output.write(sendingMessage.getBytes());
			output.flush();
			// System.out.println("Sent: " + messageOutput);
		}
		catch (IOException e){
			System.out.println("IO Exception " + e);
		}
	}
	
	/*
	* The following method is intended to be used to receive an incoming message from the ds-server.
	*/
	public static void receiveMessage(){
		try{
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			receivedMessage = (String)input.readLine();
			// System.out.println("Received: " + receivedMessage);
		}
		catch (IOException e){
			System.out.println("IO Exception " + e);
		}
	}
	
	/*
	* The following method is intended to be used to receive all server records and their state at the requested time.
	*/
	public static void receiveServerRecords(){
		try{
			// Initialise method variables.
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			serverRecords = new String[numberOfServers][8];
			String previousServerType = "";
			int serverID = 0;
			
			// Receive each incoming server record one at a time.
			for(int i = 0; i < numberOfServers; i++){
				String[] currentServerRecord = ((String)input.readLine()).split(" ");
				
				// Detect when there is a new server type and set the serverID accordingly.
				if((currentServerRecord[0].equals(previousServerType)) == false){
					serverID = 0;
				}
				
				// Store the value of each server record in its own array element
				serverRecords[i][0] = currentServerRecord[0]; // Server type
				serverRecords[i][1] = currentServerRecord[1]; // Server limit
				serverRecords[i][2] = currentServerRecord[2]; // Server bootuptime
				serverRecords[i][3] = currentServerRecord[3]; // Server hourlyrate
				serverRecords[i][4] = currentServerRecord[4]; // Server cores
				serverRecords[i][5] = currentServerRecord[5]; // Server memory
				serverRecords[i][6] = currentServerRecord[6]; // Server disk
				serverRecords[i][7] = String.valueOf(serverID); // Server ID
				
				previousServerType = currentServerRecord[0];
				serverID++;
			}
		}
		catch (IOException e){
			System.out.println("IO Exception " + e);
		}
	}
	
	/*
	* The following method is used as the first job-scheduling server type priority. The steps involved are:
	* 1. Check the server's current resource capacity
	* 2. Check if the server has waiting and running jobs simultaneously. 
	* 3. Check the server's total waiting time
	* 4. If the server has the lowest waiting time, set the scheduling server to the current server.
	
	* At the end, if at least one server is found, skip the other "priority" sections and schedule the job.
	* If no server was found in the first "priority" section, move on to "serverPriorityTwo".
	*/
	public static void serverPriorityOne(int jobCores, int jobMemory, int jobDisk){
		for(int i = 0; i < numberOfServers; i++){
			String serverType = serverRecords[i][0];
			int serverCores = Integer.parseInt(serverRecords[i][4]);
			int serverMemory = Integer.parseInt(serverRecords[i][5]);
			int serverDisk = Integer.parseInt(serverRecords[i][6]);
			int serverID = Integer.parseInt(serverRecords[i][7]);
			
			// Step 1
			if((serverCores >= jobCores) && (serverMemory >= jobMemory) && (serverDisk >= jobDisk)){
			
				// Step 2
				boolean waitingJobs = false;
				boolean runningJobs = false;
				
				sendMessage(CNTJ + serverType + " " + "0" + " " + "1");
				receiveMessage();
				if(Integer.parseInt(receivedMessage) > 0){
					waitingJobs = true;
				}
				sendMessage(CNTJ + serverType + " " + "0" + " " + "2");
				receiveMessage();
				if(Integer.parseInt(receivedMessage) > 0){
					runningJobs = true;
				}
				
				if((waitingJobs && runningJobs) == false){
					// Steps 3-4
					sendMessage(EJWT + serverType + " " + serverID);
					receiveMessage();
					if(firstServerFound == false){
						lowestWaitingTime = Integer.parseInt(receivedMessage);
						schedulingServerType = serverType;
						schedulingServerID = serverID;
						serverFound = true;
						firstServerFound = true;
					}
					else{
						if(Integer.parseInt(receivedMessage) < lowestWaitingTime){
							lowestWaitingTime = Integer.parseInt(receivedMessage);
							schedulingServerType = serverType;
							schedulingServerID = serverID;
							serverFound = true;
						}
					}
				}
			}
		}
	}
	/*
	* The following method is used as the second job-scheduling server type priority. The steps involved are:
	* 1. Check the server's current resource capacity
	* 2. Check the server's total waiting time
	* 3. If the server has the lowest waiting time, set the scheduling server to the current server.
	
	* At the end, if at least one server is found, skip the third "priority" section and schedule the job.
	* If no server was found in the second "priority" section, move on to "serverPriorityThree".
	*/
	public static void serverPriorityTwo(int jobCores, int jobMemory, int jobDisk){
		for(int i = 0; i < numberOfServers; i++){
			String serverType = serverRecords[i][0];
			int serverCores = Integer.parseInt(serverRecords[i][4]);
			int serverMemory = Integer.parseInt(serverRecords[i][5]);
			int serverDisk = Integer.parseInt(serverRecords[i][6]);
			int serverID = Integer.parseInt(serverRecords[i][7]);

			// Step 1
			if((serverCores >= jobCores) && (serverMemory >= jobMemory) && (serverDisk >= jobDisk)){
				// Steps 2-3
				sendMessage(EJWT + serverType + " " + serverID);
				receiveMessage();
				if(firstServerFound == false){
					lowestWaitingTime = Integer.parseInt(receivedMessage);
					schedulingServerType = serverType;
					schedulingServerID = serverID;
					serverFound = true;
					firstServerFound = true;
				}
				else{
					if(Integer.parseInt(receivedMessage) < lowestWaitingTime){
						lowestWaitingTime = Integer.parseInt(receivedMessage);
						schedulingServerType = serverType;
						schedulingServerID = serverID;
						serverFound = true;
					}
				}
			}
		}
	}

	/*
	* The following method is used as the third job-scheduling server type priority. The steps involved are:
	* 1. Check the server's INITIAL resource capacity
	* 2. Check the server's total waiting time
	* 3. If the server has the lowest waiting time, set the scheduling server to the current server.
	*/
	public static void serverPriorityThree(int jobCores, int jobMemory, int jobDisk){
		for(int i = 0; i < numberOfInitialServers; i++){
			String serverType = initialServerRecords[i][0];
			int serverCores = Integer.parseInt(initialServerRecords[i][4]);
			int serverMemory = Integer.parseInt(initialServerRecords[i][5]);
			int serverDisk = Integer.parseInt(initialServerRecords[i][6]);
			int serverID = Integer.parseInt(initialServerRecords[i][7]);
			
			// Step 1
			if((serverCores >= jobCores) && (serverMemory >= jobMemory) && (serverDisk >= jobDisk)){
				// Steps 2-3
				sendMessage(EJWT + serverType + " " + serverID);
				receiveMessage();
				if(firstServerFound == false){
					lowestWaitingTime = Integer.parseInt(receivedMessage);
					schedulingServerType = serverType;
					schedulingServerID = serverID;
					serverFound = true;
					firstServerFound = true;
				}
				else{
					if(Integer.parseInt(receivedMessage) < lowestWaitingTime){
						lowestWaitingTime = Integer.parseInt(receivedMessage);
						schedulingServerType = serverType;
						schedulingServerID = serverID;
						serverFound = true;
					}
				}
			}
		}
	}
	
	/*
	* The following method is the "main method", intended to be used to run the algorithm and implement the new
	* fast-scheduling algorithm.
	*/
	
	public static void main(String[] args){
		try{
			// Initialise method variables.
			boolean firstIteration = true;
			
			// Initialise the socket with the given IP address and port number.
			socket = new Socket("127.0.0.1", 50000);
			
			// Perform the initial handshake.
			sendMessage(HELO);
			receiveMessage();
			sendMessage(AUTH);
			receiveMessage();
			
			// Loop through every job that must be scheduled.
			while(true){
				// If there are no more jobs to be scheduled, exit the loop and stop.
				if(receivedMessage.equals(NONE)){
					break;
				}
				else{
					// Send REDY message and receive JOBN message.
					sendMessage(REDY);
					receiveMessage();
					String jobMessageString = receivedMessage;
					
					// Ignore all messages that are not JOBN.
					if((jobMessageString.startsWith(JOBN)) == false){
					}
					else{
						// Break up the individual parts of the JOBN message to use job requirements.
						String[] jobMessageComponents = receivedMessage.split(" ");
						int jobSubmitTime = Integer.parseInt(jobMessageComponents[1]);
						int jobID = Integer.parseInt(jobMessageComponents[2]);
						int jobEstRuntime = Integer.parseInt(jobMessageComponents[3]);
						int jobCores = Integer.parseInt(jobMessageComponents[4]);
						int jobMemory = Integer.parseInt(jobMessageComponents[5]);
						int jobDisk = Integer.parseInt(jobMessageComponents[6]);
						
						// Send GETS Capable message (with job requirements) and receive DATA message.
						sendMessage(GETS + jobCores + " " + jobMemory + " " + jobDisk);
						receiveMessage();
						String dataMessageString = receivedMessage;
						sendMessage(OK);
						
						// Get the number of the server records about to be read.
						String[] dataMessageComponents = dataMessageString.split(" ");
						numberOfServers = Integer.parseInt(dataMessageComponents[1]);
						
						// Receive a two-dimensional array of all server records.
						receiveServerRecords();
						
						// On the first iteration only, keep a record of the maximum capacity of all servers.
						if(firstIteration == true){
							numberOfInitialServers = numberOfServers;
							initialServerRecords = serverRecords.clone();
							firstIteration = false;
						}
						
						// Send OK message and receive the '.' message.
						sendMessage(OK);
						receiveMessage();
						
						/*
						* In this algorithm, the scheduling decision is based on priority. It is broken up into three
						* levels of priority. The first section prioritises servers with acceptable current resources
						* and no jobs waiting/running simultaneously. The second section prioritises servers with
						* acceptable current resources only. The third section prioritises servers with acceptable
						* initial (capable) resources. If at least one server is found in any section, the job is
						* scheduled to the server with the lowest waiting time in that section.
						*/
						
						// Set up variables for the fast-scheduling algorithm.
						serverFound = false;
						firstServerFound = false;
						lowestWaitingTime = 0;
						
						// Make the scheduling decision based on priority of the server state as discussed above.
						serverPriorityOne(jobCores, jobMemory, jobDisk);
						if(serverFound == false){
							serverPriorityTwo(jobCores, jobMemory, jobDisk);
							if(serverFound == false){
								serverPriorityThree(jobCores, jobMemory, jobDisk);
							}
						}
						
						// Schedule the job to the server that can handle it the soonest.
						sendMessage(SCHD + jobID + " " + schedulingServerType + " " + schedulingServerID);
						
						// Receive the OK message and continue.
						receiveMessage();
					}
				}
			}
			
			// Terminate the process and close the socket and input/output streams.
			sendMessage(QUIT);
			receiveMessage();
			socket.close();
			input.close();
			output.close();
		}
		catch (IOException e){
			System.out.println("IO Exception " + e);
		}
	}
}