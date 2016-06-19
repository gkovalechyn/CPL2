
/*
 * File:   Server.java
 * Author: gkovalechyn
 *
 * Created on Jun 19, 2016, 1:00:40 AM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.testpluginserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author gkovalechyn
 */
public class Server implements Runnable{
    private DatagramSocket serverSocket;
    private boolean toRun = true;
    private InetAddress address;
    private int port;
    private boolean is64 = false;
    
    private final byte[] cplHash32 = {(byte) 0x54, (byte) 0x93, (byte) 0x6D, (byte) 0x4F, (byte) 0x10, (byte) 0x92, (byte) 0x6B, (byte) 0xE1, (byte) 0xB7, (byte) 0xB9, (byte) 0x53, (byte) 0x18, (byte) 0x0C, (byte) 0x67, (byte) 0x21, (byte) 0x84, (byte) 0x1C, (byte) 0x21, (byte) 0x12, (byte) 0x99, (byte) 0x5E, (byte) 0xF2, (byte) 0xA1, (byte) 0x20, (byte) 0xA3, (byte) 0x4F, (byte) 0xD4, (byte) 0xC0, (byte) 0xD0, (byte) 0x4E, (byte) 0xEC, (byte) 0x9F};
    private final byte[] cplHash64 = {(byte) 0xD6, (byte) 0x6D, (byte) 0xE2, (byte) 0xFE, (byte) 0x32, (byte) 0x0A, (byte) 0x25, (byte) 0xCF, (byte) 0x25, (byte) 0x65, (byte) 0x1F, (byte) 0xC0, (byte) 0xF1, (byte) 0x3B, (byte) 0x10, (byte) 0xAB, (byte) 0xC4, (byte) 0xCB, (byte) 0x5E, (byte) 0x8E, (byte) 0xB0, (byte) 0xDE, (byte) 0xFA, (byte) 0x7A, (byte) 0xFA, (byte) 0x6E, (byte) 0x43, (byte) 0x23, (byte) 0xB4, (byte) 0x15, (byte) 0xB6, (byte) 0x53};
    
    private final byte[] loaderHash = {(byte) 0x2A, (byte) 0x22, (byte) 0x64, (byte) 0x2A, (byte) 0x5E, (byte) 0xFB, (byte) 0x66, (byte) 0x03, (byte) 0x39, (byte) 0xE7, (byte) 0x45, (byte) 0x4E, (byte) 0x6D, (byte) 0x0D, (byte) 0x8F, (byte) 0xCC, (byte) 0x01, (byte) 0x7F, (byte) 0x79, (byte) 0x62, (byte) 0x45, (byte) 0x1A, (byte) 0x7F, (byte) 0x36, (byte) 0x89, (byte) 0x7C, (byte) 0x3C, (byte) 0x61, (byte) 0x34, (byte) 0x8E, (byte) 0xF1, (byte) 0x4D};
    
    private final byte[] pluginHash = {(byte) 0x33, (byte) 0x17, (byte) 0x81, (byte) 0xEB, (byte) 0x6A, (byte) 0x32, (byte) 0x0D, (byte) 0xBB, (byte) 0x92, (byte) 0x39, (byte) 0x13, (byte) 0xC6, (byte) 0xB3, (byte) 0xD8, (byte) 0x36, (byte) 0xA2, (byte) 0xE7, (byte) 0x87, (byte) 0xDA, (byte) 0x2D, (byte) 0xAC, (byte) 0x60, (byte) 0x8C, (byte) 0x39, (byte) 0xC3, (byte) 0xAE, (byte) 0x72, (byte) 0x34, (byte) 0x1D, (byte) 0x7D, (byte) 0xA4, (byte) 0xDA};
    
    private final byte[] keyBytes = {(byte) 0x13, (byte) 0x0C, (byte) 0xA7, (byte) 0x36, (byte) 0xC5, (byte) 0xA8, (byte) 0x5D, (byte) 0xAD, (byte) 0xDB, (byte) 0x07, (byte) 0x8E, (byte) 0x2C, (byte) 0xCB, (byte) 0x5F, (byte) 0xE4, (byte) 0xAC};
    private final byte[] ivBytes = {(byte) 0xDA, (byte) 0xF4, (byte) 0x16, (byte) 0xF6, (byte) 0xB7, (byte) 0x22, (byte) 0x82, (byte) 0x7E, (byte) 0x45, (byte) 0x17, (byte) 0x26, (byte) 0xAF, (byte) 0x18, (byte) 0xEA, (byte) 0x36, (byte) 0xC9};
    
    public Server(int port) throws IOException{
        this.serverSocket = new DatagramSocket(port);
    }
    
    public boolean isConnected(){
        return this.serverSocket != null && !this.serverSocket.isClosed();
    }
    
    @Override
    public void run() {
        byte[] buff = new byte[256];
        DatagramPacket packet = new DatagramPacket(buff, buff.length);
        
        while(this.toRun){
            try{
                this.serverSocket.receive(packet);
                
                System.out.println("Handling: " + packet.getAddress() + ":" + packet.getPort());
                this.address = packet.getAddress();
                this.port = packet.getPort();
                
                if (packet.getLength() != 4){
                    System.out.println("Received packet with different size than 4. (" + packet.getLength() + ")");
                }else {
                    this.is64 = !(packet.getData()[2] == ((byte) '3') && packet.getData()[2] == ((byte) '2'));
                    System.out.println("Is using 64 bits?: " + is64);
                }
                
                buff = new byte[packet.getLength()];
                System.arraycopy(packet.getData(), 0, buff, 0, packet.getLength());
                this.serverSocket.send(new DatagramPacket(buff, buff.length, address, this.port));
                
                this.serverSocket.receive(packet);
                if (!arraysEqual(packet.getData(), (this.is64) ? this.cplHash64 : this.cplHash32, packet.getLength())){
                    System.out.println("Invalid CPL hash.");
                    buff = new byte[4];
                    buff[3] = 0;
                    this.serverSocket.send(new DatagramPacket(buff, buff.length, address, this.port));
                    continue;
                
                }
                this.serverSocket.receive(packet);
                if (!arraysEqual(packet.getData(), this.loaderHash, packet.getLength())){
                    System.out.println("Invalid loader hash.");
                    buff = new byte[4];
                    buff[3] = 0;
                    this.serverSocket.send(new DatagramPacket(buff, buff.length, address, this.port));
                    continue;
                }
                
                this.serverSocket.receive(packet);
                if (!arraysEqual(packet.getData(), this.pluginHash, packet.getLength())){
                    System.out.println("Invalid plugin hash.");
                    buff = new byte[4];
                    buff[3] = 0;
                    this.serverSocket.send(new DatagramPacket(buff, buff.length, address, this.port));
                    continue;
                }
                
                buff = new byte[4];
                buff[3] = 1;
                
                this.serverSocket.send(new DatagramPacket(buff, buff.length, address, this.port));
                this.serverSocket.send(new DatagramPacket(this.keyBytes, 16, address, this.port));
                this.serverSocket.send(new DatagramPacket(this.ivBytes, 16, address, this.port));
                
                System.out.println("User passed all tests, key and IV sent.");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
    
    private boolean arraysEqual(byte[] arr1, byte[] arr2, int length){
        if (arr2.length != length){
            System.out.println("Different length. " + arr2.length + " != " + length);
            return false;
        }
        
        for(int i = 0; i < length; i++){
            if (arr1[i] != arr2[i]){
                System.out.println("Byte " + i + " differed. " + arr1[i] + " != " + arr2[i]);
                return false;
            }
        }
        
        return true;
    }
    
    public void stop(){
        this.toRun = false;
        this.serverSocket.close();
    }

}
