package com.cets;

import java.util.Arrays;
import java.util.Scanner;

public class Testb {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] arr = new int[n];
		for(int i=0;i<n;i++) {
			arr[i]=sc.nextInt();
		}
		Arrays.sort(arr);
		System.out.println(search(arr));
	}
	public static int search(int[] arr) {
		if(arr.length==0) {
			return 0;
		}
		int i=0;
		for(int j=1;j<arr.length;j++) {
			if(arr[i]!=arr[j]) {
				i++;
				arr[i]=arr[j];
			}
		}
		return i+1;
	}
}
