package Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

public class Test 
{
	public static void main(String[] args) 
	{
		Test test = new Test();
		
		int n=12;
		int k=7;
			
		int x=n;
		int y=1;
		int b=1;
		
		System.out.println(" n : " + Integer.toString(n) + " k: " + Integer.toString(k));
		System.out.println("x   |   y   |   b");
		
		while(x!=k)
		{
			System.out.println(Integer.toString(x) + "   |   " + Integer.toString(y) + "   |   " + Integer.toString(b));
			b=(b*x/y);
			x=x-1;
			y=y+1;
		}
		
		System.out.println(Integer.toString(x) + "   |   " + Integer.toString(y) + "   |   " + Integer.toString(b));
		
		//System.out.println(Integer.toString(test.bin(x+y-1,y-1)));
		System.out.println("bin(n,k) : " + Integer.toString(test.bin(n, k)));
		System.out.println("b = " + Integer.toString(b));

		
		/*
		System.out.println("Base : " + Integer.toString(test.bin(n,k)));
		//int check = (((n-k+1)*(k+1))/((k-1)*(n-k))) * test.bin((n-1), (n-k));
		int check = ((k-1)/(n-k+1)) * test.bin((n-1), (n-k-1));
		int bb = ((k-1)/(n-k+1))
		System.out.println("Check : " + Integer.toString(check));
		 */
	}
	
	public int bin(int n, int k)
	{
		if(k==n)
		{
			return 1;
		}
		else
		{
			return bin(n,k+1)*(k+1)/(n-k);
		}
	}
}
