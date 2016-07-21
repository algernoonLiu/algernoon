package com.algernoon.test;

import java.util.Random;

public class XCTest implements Runnable{

	@Override
	public void run() {
		while(true){
			System.out.println("执行任务。。。。。。。。。。。。。。。。。。");
			try {
				int sleep  = new Random().nextInt(800) * 1000;
				System.out.println("休息"+sleep/1000/60+"分钟");
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Thread(new XCTest()).start();
	}
}
