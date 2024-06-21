// Import items.
import java.io.*;
import java.net.*;

public class JobScheduler1 {

	// Declares the same common variables for all methods.
	static Socket socket;
	static int numberOfRecords;
	static String receivedMessage;
	static String[][] serverRecords;
	
	// Declares the same message variables for all methods.
	static String HELO = "HELO";
	static String AUTH = "AUTH " + System.getProperty("user.name");
	static String QUIT = "QUIT";
	static String REDY = "REDY";
	static String DATA = "DATA";
	static String JOBN = "JOBN";
	static String JOBP = "JOBP";
	static String JCPL = "JCPL";
	static String RESF = "RESF";
	static String RESR = "RESR";
	static String CHKQ = "CHKQ";
	static String NONE = "NONE";
	static String GETS = "GETS All";
	static String SCHD = "SCHD ";
	static String ENQJ = "ENQJ";
	static String DEQJ = "DEQJ";
	static String LSTQ = "LSTQ";
	static String CNTJ = "CNTJ";
	static String EJWT = "EJWT";
	static String LSTJ = "LSTJ";
	static String MIGJ = "MIGJ";
	static String KILJ = "KILJ";
	static String TERM = "TERM";
	static String ERR = "ERR";
	static String OK = "OK";

	/*
	* This is a method designed for the client to send any type of message to the server.
	*/
	public static void sendMessage(String sendingMessage){
		try{
			String printedMessage = sendingMessage;
			sendingMessage = sendingMessage + '\n'; // append a new line character to each message being sent.
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			output.write(sendingMessage.getBytes());
			output.flush();
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}
	}
	
	/*
	* This is a method designed for the client to receive a standard (single-line) message from the server.
	*/
	public static void receiveMessage(){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			receivedMessage = (String)input.readLine();
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}
	}
	
	/*
	* This is a method designed for the client to receive a large number of server records (multiple lines) and get their information.
	* Each record has 7 components: type, limit, bootupTime, hourlyRate, cores, memory, and disk
	* The method reads in each record one line at a time, getting its server type component [0] and core component [4]
	* and then storing both in a two-dimensional array called "serverRecords".
	*/
	public static void receiveRecords(){
		try{
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			serverRecords = new String[2][numberOfRecords];
			for(int i = 0; i < numberOfRecords; i++){ // loops through all the records and stores each one in its own array element.
				String[] serverRecord = ((String)input.readLine()).split(" ");
				serverRecords[0][i] = serverRecord[0];
				serverRecords[1][i] = serverRecord[4];
			}
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}
	}
	
	/*
	* This is a method designed to implement the largest-round-robin job-scheduling algorithm.
	*/
	public static void largestRoundRobin(){
	
		// Declare and initialise variables for finding the biggest server.
		boolean firstIteration = true;
		String biggestServerType = "";
		int biggestServerCores = 0;
		
		// Declare and initialise variables for scheduling the jobs.
		int numberOfServers = 0;
		int serverID = 0;
		
		// Loop through every job that needs to be completed.
		while(true){
			
			// If there are no more jobs, then stop scheduling jobs.
			if(receivedMessage.startsWith(NONE)){
				break;
			}
			else{
				/*
				* Send REDY message and then receive a new message (most likely JOBN).
				* Store the type of message to later check if it is actually a JOBN message.
				*/
				sendMessage(REDY);
				receiveMessage();
				String messageType = receivedMessage;
					
				/*
				* Each JOBN message contains six parameters: submitTime, jobID, estRuntime, core, memory, and disk.
				* The "jobID" parameter is required to be used for the scheduling process, so we will record the job message for later.
				*/ 
				String jobMessageString = receivedMessage;

				/*
				* We want to perform the following code exactly one time only. Therefore, after this code is executed,
				* we prevent the while loop from entering it again.
				* Then, we need to send a GETS All message.
				* After this, the server sends back a DATA message with two parameters: numberOfRecords, and recordSize.
				* The client then responds with an OK message.
				*/
				if(firstIteration == true){
					sendMessage(GETS);
					receiveMessage(); 
					sendMessage(OK); 
					
					/*
					* This DATA message tells us how many records of servers are in the array. We need to split up the message
					* into its components to acquire this number (from the first parameter) and store it in a variable.
					*/
					String dataMessageString = receivedMessage;
					String[] dataMessageComponents = dataMessageString.split(" ");
					numberOfRecords = Integer.parseInt(dataMessageComponents[1]);
					
					/*
					* Next, we need to read in all the server records received from the ds-server.
					* This shall be done using the "receive records" method, which reads in many lines of input 
					* and stores all of it into a two-dimensional array.
					*/
					receiveRecords();
					
					/*
					* Now, we need to find the biggest server type by finding the one with the most amount of cores.
					* To do this, we loop through each record, read its "core" component, and compare it to the current maximum
					* core. If it is larger, then this becomes the biggest server type. If it has the same amount of cores as
					* the current biggest server type, it also checks its server type component. If it is a different type, it ignores
					* the record, but if it is of the same type, the number of biggest server type records increases. The final value
					* is then decreased by one, as serverIDs should begin with zero and not one.
					* We only want to perform this task one time. Therefore, after the code is executed, we prevent the while loop
					* from accessing it again.
					*
					*/
					String serverType;
					int serverCores;
					
					for(int i = 0; i < numberOfRecords; i++){
						serverType = serverRecords[0][i];
						serverCores = Integer.parseInt(serverRecords[1][i]);
						// Check if there is a new biggest server type
						if(serverCores > biggestServerCores){
							biggestServerType = serverType;
							biggestServerCores = serverCores;
							numberOfServers = 1; 
						}
						// Check if there is another server of the same type
						else if((serverCores == biggestServerCores) && (serverType.equals(biggestServerType))){
								numberOfServers++;
						}
					}
					numberOfServers--;
					
					// Send an OK message and receive the '.' message
					sendMessage(OK);
					receiveMessage(); 
					firstIteration = false;
				}
				
				/*
				* Now at this point (after checking for a jobN message), we can schedule a job.
				* A SCHD message has three parameters: jobID, serverType, and serverID.
				* We can get the jobID from the job message we received from earlier.
				* We can get the serverType from the 'biggest server' information.
				* The serverID component is more difficult. To make this work, we need to start at zero,
				* and increase the value with each iteration until it reaches the numberOfServers. Then,
				* the value shall reset back to zero.
				* Using this information, we can successfully schedule a job to all servers of the
				* biggest type.
				*/	
				if(messageType.startsWith(JOBN)){
					String[] jobMessageComponents = jobMessageString.split(" ");
					int jobID = Integer.parseInt(jobMessageComponents[2]);
					sendMessage(SCHD + jobID + " " + biggestServerType + " " + serverID); // schedule the job.
					
					// Change the value of the serverID
					if(serverID == numberOfServers){
						serverID = 0;
					}
					else{
						serverID++;
					}
					
					// Read the OK message from the ds-server and continue.
					receiveMessage();
				}
			}
		}
	}

	/*
	* This is the method used to run the main code execution.
	*/
	public static void main(String[] args){
		
		try{	
			// Initialise the socket with given IP address and port number.
			socket = new Socket("127.0.0.1", 50000);
				
			// Perform the initial handshake process.
			sendMessage(HELO);
			receiveMessage();
			sendMessage(AUTH);
			receiveMessage();
			
			// Implement the desired job-scheduling algorithm. In this case, it is largest-round-robin only.
			largestRoundRobin();
			
			// Terminate the process and close the socket.
			sendMessage(QUIT);
			receiveMessage();
			socket.close();	
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}
	}	
}
