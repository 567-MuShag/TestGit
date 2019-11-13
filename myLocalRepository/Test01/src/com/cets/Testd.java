package com.cets;

import java.util.Arrays;
import java.util.Scanner;

public class Testd {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int val = sc.nextInt();
		int[] arr = new int[n];
		for(int i=0;i<n;i++) {
			arr[i]=sc.nextInt();
		}
		Arrays.sort(arr);
		System.out.println(search(arr,val));
	}
	public static int search(int[] arr,int val) {
		for(int i=0;i<arr.length;i++) {
			if(val<=arr[i]) {
				return i;
			}
		}
		return arr.length;
	}
}
