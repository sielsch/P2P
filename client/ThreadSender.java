package client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

import com.sun.istack.internal.ByteArrayDataSource;

import comClientServer.AdressServerTCP;
import comClientServer.P2PFile;

public class ThreadSender extends Thread{

	
	
	private DatagramSocket socketUDP=null;
	private P2PFile fileToSend=null; 
	private AdressServerTCP adress=null;
	private DatagramPacket packetSended;
	private byte[] buff;

	private int buffSize;

	

	private long premierMorceau;
	private long dernierMorceau;
	private byte [] longInByte;

	
	private FileInputStream fis;
	private BufferedInputStream bis;
	
	public ThreadSender(P2PFile fileToSend, AdressServerTCP adress, int port,long premierMorceau, long dernierMorceau){
		this.fileToSend=fileToSend;
		this.adress=adress;
		this.premierMorceau=premierMorceau;
		this.dernierMorceau=dernierMorceau;
		buff= new byte[buffSize];

	}
	
	public void run(){
		try {
			fis=new FileInputStream(fileToSend.getName());
			bis=new BufferedInputStream(fis);
			
		
			for (long i = premierMorceau; i < dernierMorceau; i++) {
				bis.read(buff);
				for (int j = buffSize-Long.BYTES; j < buff.length; j++) {
					
				}
				
				
			}

			
			socketUDP=new DatagramSocket();
//			socketUDP.send(new DatagramPacket(buf, length));
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public byte[] longToBytes(long x) {
	    ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
	    buffer.putLong(x);
	    return buffer.array();
	}
	
}
