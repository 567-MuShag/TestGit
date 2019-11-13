package com.cets;

import java.util.Scanner;

public class Teste {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int[] arr = new int[n];
		for(int i=0;i<n;i++) {
			arr[i]=sc.nextInt();
		}
		System.out.println(search(arr));
	}
	public static int search(int[] arr) {
		int tmp=arr[0];
		int sum=0;
		for(int i=0;i<arr.length;i++) {
			if(sum>0) {
				sum = sum+arr[i];
			}else {
				sum = arr[i];
			}
			tmp = Math.max(tmp, sum);
		}
		return tmp;
	}
}
