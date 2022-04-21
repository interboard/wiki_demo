package com.interboard.wikiblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Stack;

@SpringBootApplication
public class DemoApplication {
	static Stack<String> a = new Stack<String>();
	public static void main(String[] args) {
		//StringBuffer a1= new StringBuffer("A");
		//StringBuffer a2 = new StringBuffer("B");
		//aMethod(a1,a2);
		System.out.println("--");
		System.out.println(xSum(3));
		System.out.println(xSum(10));


		a.push(".");
		a.push("g");
		a.push("n");
		a.push("i");
		a.push("k");
		a.push("r");
		a.push("a");
		a.push("b");
		a.push(" ");
		a.push("s");
		a.push("i");
		a.push(" ");
		a.push("g");
		a.push("o");
		a.push("d");
		a.push(" ");
		a.push("A");
test();
		//System.out.println(a1 + ":" + a2);
		//SpringApplication.run(DemoApplication.class, args);
	}

	public static int xSum(int x){
		int sum = 0;
		for (int i=1;i<=x;i++){
			sum=sum+i;
		}
		return sum;
	}

	public static void test(){
		if (!a.isEmpty()){
			String c = a.pop();
			test();
			System.out.print(c);
		}
	}

	public static void aMethod(StringBuffer a1,StringBuffer a2){
		a1.append(a2);
		a2=a1;
	}

}
