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
 * 球面的简单几何表示。内部是一个点和半径
 * @author Matt.Chudleigh
 *
 */
public class Sphere {
//center表示中心点，是一个三维向量坐标
public final Vec3d center;
//radius表示球的半径
public double radius;

public Sphere(Vec3d center, double radius) {
	this.center = new Vec3d(center);
	this.radius = radius;
}
/**
 * 计算给定点距离球面的距离
 * @param point
 * @return
 */
public double getDistance(Vec3d point) {
	Vec3d diff = new Vec3d();
	//计算中心点和这个点的差
	diff.sub3(center, point);
	//计算差向量的模
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
	//计算中心点和球的中心点的差
	diff.sub3(center, s.center);
	//计算差向量的模
	double dist = diff.mag3();

	return dist - radius - s.radius;
}
/**
 * 计算给定平面到球面的距离
 * @param p
 * @return
 */
public double getDistance(Plane p) {
	//计算平面到中心点的最短距离
	double dist = p.getNormalDist(center);
	return dist - radius;
}
}
