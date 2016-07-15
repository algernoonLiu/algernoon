package com.algernoon.leetcode;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;

public class blog
{
	public static void main(String[] args) {
		for(int i=0; i<10; i++) {
			Desktop desktop=Desktop.getDesktop();
				try {
					desktop.browse(new URI("https://www.baidu.com"));
					
				} 
				catch (IOException e1) {}
				catch (URISyntaxException e1) {}
		}
	}
}