/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2012 Ausenco Engineering Canada Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jaamsim.math;

/**
 * A simple geometric representation of a sphere. Internally is just a point and radius
 * ����ļ򵥼��α�ʾ���ڲ���һ����Ͱ뾶
 * @author Matt.Chudleigh
 *
 */
public class Sphere {
//center��ʾ���ĵ㣬��һ����ά��������
public final Vec3d center;
//radius��ʾ��İ뾶
public double radius;

public Sphere(Vec3d center, double radius) {
	this.center = new Vec3d(center);
	this.radius = radius;
}
/**
 * ����������������ľ���
 * @param point
 * @return
 */
public double getDistance(Vec3d point) {
	Vec3d diff = new Vec3d();
	//�������ĵ�������Ĳ�
	diff.sub3(center, point);
	//�����������ģ
	double dist = diff.mag3();
	return dist - radius;
}
/**
 * 
 * @param s
 * @return
 */
public double getDistance(Sphere s) {
	Vec3d diff = new Vec3d();
	//�������ĵ��������ĵ�Ĳ�
	diff.sub3(center, s.center);
	//�����������ģ
	double dist = diff.mag3();

	return dist - radius - s.radius;
}
/**
 * �������ƽ�浽����ľ���
 * @param p
 * @return
 */
public double getDistance(Plane p) {
	//����ƽ�浽���ĵ����̾���
	double dist = p.getNormalDist(center);
	return dist - radius;
}
}
