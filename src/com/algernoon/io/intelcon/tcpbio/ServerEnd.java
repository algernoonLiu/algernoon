package com.algernoon.io.intelcon.tcpbio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 基于Java自身技术实现消息间系统通信-服务端
 */
public class ServerEnd {
	
	public static void main(String[] args) throws IOException {
		
		ServerSocket ss = new ServerSocket(12345);
		Socket socket = ss.accept();
	}
	
}
