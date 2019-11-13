package com.cets;

import java.util.Scanner;

public class Testc {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int a = sc.nextInt();
		int val = sc.nextInt();
		int[] arr = new int[a];
		for(int i=0;i<a;i++) {
			arr[i] = sc.nextInt();
		}
		System.out.println(search(arr,val));
	}
	public static int search(int[] arr,int val) {
		int count = 0;
		for(int i=0;i<arr.length;i++) {
			if(arr[i]!=val) {
				count++;
			}
		}
		return count;
	}
}
