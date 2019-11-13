package com.cets;

public class Student {
	private Student() {
		
	}
	private static Student s = null;
	public synchronized static Student getStudent() {
		if(s==null) {
			s = new Student();
		}
		return s;
	}
	public static void main(String[] args) {
		Student s1 = Student.getStudent();
		Student s2 = Student.getStudent();
		System.out.println(s1==s2);
	}
}
