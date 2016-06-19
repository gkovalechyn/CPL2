/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gkovalechyn.testpluginserver;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author gkovalechyn
 */
public class TestPluginServer {
    
    
    public static void main(String[] args) {
        String input;
        Scanner sc = new Scanner(System.in);
        Server server;
        try {
            server = new Server(6969);
            new Thread(server).start();
            
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        while(!"exit".equals(input = sc.next())){
            System.out.println(input);
        }
        
        server.stop();
    }
}
