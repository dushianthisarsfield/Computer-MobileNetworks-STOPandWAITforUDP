

import java.io.*;

import java.net.*;
import java.util.Scanner;

public class Client {
		
	public static void main(String [] args){
	 
	try { //entire code inside a try-catch block because many of the methods for both the connectionless and Stream-mode API could raise exceptions.
	  	
	  //create a socket
	  DatagramSocket clientSocket = new DatagramSocket(2390);
	  
	  //time out timer initialised to 5 seconds.
	  clientSocket.setSoTimeout( 5000 );

	  //next sequence number of data packet initialised (smallest unused sequence number, sequence number of the next packet to be sent).
	  Integer SN = -1; 
	  
	  //oldest unacknowledged packet
	  int sendBase = 0;

	  //number of unacknowledged packets
	  int nak = 0;
	  
	  //next sequence number
	  int nextSeqNum = SN +1;
	  
	  //lost ack probability
	  final double lostAckProbability = 0.3;
	  
	  //random number generated
	  double randomNumber = Math.random();
	  
	  //getting ip address of server
	  InetAddress receiverHost = InetAddress.getByName("localhost");
	  
	  //port of server
	  int receiverPort = 9876;
	  
	  int firstNak = 0;
	  
	  boolean nakOneStart = false;
	  
	  
	  
	  
	  
	  //Intro message to user.
	  System.out.println("Hello, welcome to my Summative Assessment for Computer & Mobile Networks.");
		
		   //Code to decide how many times the string umbrella will be sent (1 word per frame).
	       {
	       //for user to input number. 	
	       Scanner scanner = new Scanner(System.in);
	       
	       System.out.print("Enter number of frames : ");
           //initialising variable - user input.
	       int number = scanner.nextInt();
	       
	       System.out.print("Enter window size : ");
	       int windowSize = scanner.nextInt();
	       
	       //int AvailableSN = (int)((Math.pow(2,windowSize))-1);
	       
	       
           //logic loop
	       if(number==0)
	             {//if number entered is 0 display the following.
	             System.out.println("No frame is sent");
	             }	    
	       else //otherwise if number entered is over 0 increase counter if counter is less than the number entered.	
	       for(int counter = 0; counter<number; counter++)
	          {

	             boolean timedOut = true;
		         while( timedOut )
			     {
		       
		         SN++;

		         //read from file
		         File file = new File("C:/Users/Dushi/OneDrive/Desktop/Summative/test.txt");
		         BufferedReader inFromFile = new BufferedReader(new FileReader(file));
		         
		         //create space inbetween writing
		   	     System.out.println();
		   	     System.out.println();
		   	     
		   	     //print out following text.
		         System.out.println("Client is reading from the filepath " +file);

                 //create a buffer to send the data
                 byte[] sendData = new byte[1024];
         
                 //set the message to send
                 String sentence = inFromFile.readLine();
                 
                 //wait 2 seconds
                 Thread.sleep(2000);
                 
                 //create space inbetween writing
           	     System.out.println();
           	     System.out.println();
           	     
                 System.out.println("Data string to be sent is: " +sentence);
                 System.out.println("SEQUENCE NUMBER of packet = " +SN);
        
                 //convert message to bytes
                 byte [] payload = sentence.getBytes();
        
                 //dealing with sequence and stuff
                 ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 DataOutputStream dos = new DataOutputStream(baos);
                 dos.writeInt(SN);
                 dos.writeInt(windowSize);
                 dos.write(payload);
        
                 //message and sequence number to bytes buffer
                 byte [] concatData = baos.toByteArray();
        
                 sendData = concatData;
                 
                 //wait 2 seconds
                 Thread.sleep(2000);
                 
                 //make space between writing.
                 System.out.println();
                 System.out.println();
   
                 //preparing the packet and send the packet to server
                 DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, receiverHost, 9876);
                 clientSocket.send(sendPacket);
                 System.out.println("Preparing to send data to the server " +receiverHost + " Port:9876");

                 //wait 4 seconds
                 Thread.sleep(4000);

                 try {
                 //to receive ack from server
                 int MESSAGE_LEN = 60;
                 byte[] recvBuffer =new byte[MESSAGE_LEN];
        
                 DatagramPacket datagram = new DatagramPacket(recvBuffer, MESSAGE_LEN);
                 
                 clientSocket.setSoTimeout(5000);
        
                 //wait to receive datagram packet
                 clientSocket.receive(datagram);
                 
                 //wait 4 seconds
                 Thread.sleep(4000);
        
                 //sequence extraction etc
      		     ByteArrayInputStream bais = new ByteArrayInputStream(recvBuffer);
      		     DataInputStream dis = new DataInputStream(bais);
      		     Integer No = dis.readInt();
        
                 //extract the message from the receive packet
                 String recvdString = new String(datagram.getData(),4, datagram.getLength());
                 
                 //make space between words.
                 System.out.println();
                 System.out.println();
                 
                 //to create lost acks.
                 
                 if(Math.random() > lostAckProbability){
                	
                	 //acknowledgement successful.
                	 if(No == SN){
                     //Acknowledgement for sequence number received by client
      	             System.out.println("Client says: "+recvdString.trim() + No + " has been successfully received from Server.  :)");
      	             sendBase++;
      	             
      	               
      	                System.out.println("sendBase = " +sendBase);
      	                }
      	             
      	     
      	          
      	        	 // If we receive an ack, stop the while loop
      	        	 timedOut = false;}
      	         
                        // Unsuccessful acknowledgement transmission
      	                else { 
			    
                             System.out.println("ACK " +No+ " lost in transmission from server. :(");

                             nak++;
                             System.out.println("nak = " +nak);
                             System.out.println("send base = " +sendBase);
      	                     
                 
                 
                        if((nak == 1) && (nak <2)) {
                	       
                    	   //nakOneStart = true;
                 	       firstNak = SN;
                 	     System.out.println("first nak = " + firstNak);
                 	     sendBase =  0;
                  }
                        
      	                }   
                        
                 int sum = nak +  sendBase;
                 System.out.println("sum of sendBase + naks = " +sum);
                        
                 //int subtract = sum - firstNak;
                 //System.out.println("sum - firstNak = " +subtract);
                        
                        
                      
                 if((sum >= windowSize) && (nak > 0)) {
                     System.out.println("GO BACK N");
                        	SN = SN - (windowSize);                       	
                        	nak = 0;
                        	firstNak = 0;
                        	sendBase = 0;
                 }
                        
                 
               
                 }       				 
			     catch( SocketTimeoutException exception )
			     {
			    
				 
			     }
	    		
			     }}}
	        
		clientSocket.close();
	      
	   }
	catch(Exception e) 
 {
	e.printStackTrace();
 }}}
	

