package com.algernoon.io.intelcon.tcpbio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 基于Java自身技术实现消息间系统通信-客户端
 */
public class ClientEnd {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket ss = new Socket("192.168.2.145", 12345);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(ss.getInputStream()));
	
		PrintWriter writer = new PrintWriter(ss.getOutputStream(), true);
	
		writer.println("hello");
		
		in.readLine();
	}
	
}
