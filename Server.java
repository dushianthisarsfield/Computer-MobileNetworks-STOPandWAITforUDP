import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.Random;


public class Server {
	
	public static void main(String[] args) {
	
	    try{
		
	        int MAX_LEN = 60;//can receive a packet of maximum 40 bytes.
		    //create a server socket
		    DatagramSocket serverSocket = new DatagramSocket(9876);
		
		    //create a buffer to read the receive data.
		    byte[] recvBuffer = new byte[MAX_LEN];
				
		    //create a buffer to send the data
		    byte[] sendServerData = new byte[1024];

		         //Server (receiver) always tuned in, listening for information sent, therefore infinite loop.   
		       while(true) {
		                   //byte[] ACK = new byte[9]; //Byte array for send ACK  
		
		                   //prints 'Server is running' when it is running.
		                   System.out.println("Server is running");
		                   //space between writing
		                   System.out.println();
		                   System.out.println();
		                   
		                   //wait 2 seconds (1000/second)
		                   Thread.sleep(2000);
		
		                   //Create a receiver packet for incoming data.
		                   DatagramPacket receivePacket = new DatagramPacket(recvBuffer, MAX_LEN);
		
		                   //Wait to receive a datagram packet.
		                   serverSocket.receive(receivePacket);

		                   //sequence extraction etc
		                   ByteArrayInputStream bais = new ByteArrayInputStream(recvBuffer);
		                   DataInputStream dis = new DataInputStream(bais);
		                   Integer SN = dis.readInt();
		                   int windowSize = dis.readInt();

		                   //extract the message from the receive packet
		                   String sentence = new String(receivePacket.getData(),4,receivePacket.getLength());
		                   
		                   //extract the ip address from the receive packet.
			               InetAddress clientIPAddress = receivePacket.getAddress();

		                   //extract client port number from the receive packet.
		                   int clientPort = receivePacket.getPort();
		                   
		                   int expectedSN = SN;
		                 
		                   System.out.println(" the expected sequence number = "+expectedSN);
    
		                   //wait 2 seconds (1000/second)
		                   Thread.sleep(2000);
		                   
		                   //Display the received message and the sequence number.
	                       System.out.println("Server says: Message = "+sentence.trim()+ " has been successfully received");
	                       System.out.println("Server says: Sequence number = " +SN+ " has been successfully received");
	                       
	                       //in reply to sender when packet was received.
	               		
	                       //creating ack	
	                       
	                       //if(SN == windowSize -1)
	                    	
	                       String messageToSend = "ACK";
                           //convert message to bytes
                           byte[] sendBuffer = messageToSend.getBytes();

                           //dealing with sequence and stuff
                           ByteArrayOutputStream baos = new ByteArrayOutputStream();
                           DataOutputStream dos = new DataOutputStream(baos);
                           dos.writeInt(SN);
                           dos.write(sendBuffer);

                           //message and sequence number to bytes buffer
                           byte [] concatData = baos.toByteArray();

                           sendServerData = concatData;

                           DatagramPacket datagram = new DatagramPacket(sendServerData, sendServerData.length, clientIPAddress, 2390);

                           serverSocket.send(datagram);
                           
                           //space between writing.
                           System.out.println();
                           System.out.println();
                           
                           //wait 2 seconds (1000/second)
		                    Thread.sleep(2000);
		                    
                           //print following text.
                           System.out.println("Acknowledgement will be sent to client" +clientIPAddress + " & Port: " +clientPort);
                           
                           //space between writing.
                           System.out.println();
                           System.out.println();
                           
                           //wait 2 seconds (1000/second)
		                    Thread.sleep(6000);
	                       
		                   }
		       }  
 	                       
          catch(Exception e){e.printStackTrace();}
		
	  }
      	
}
	


