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
 * A simple representation of a Ray in 3 space. Like all rays, it's a position and direction.
 * 三维空间中光线的简单表示。像所有光线一样，它是一个位置和方向。
 * @author Matt Chudleigh
 *
 */
public class Ray {
	//定义射线起点，是一个四维向量
	private Vec4d _start;
	//定义射线的增量，是一个四维向量
	private Vec4d _direction;
	//无参构造
	public Ray() {
		_start = new Vec4d(0, 0, 0, 1.0d);
		_direction = new Vec4d(1, 0 ,0, 0);
	}
	//传入两个四维向量，构造起点和增量
	public Ray(Vec4d start, Vec4d dir) {
		_start = new Vec4d(start);
		_direction = new Vec4d(dir);
		//将增量向量进行标准化
		_direction.normalize3();
		_direction.w = 0; // Direction is a direction...
	}

	public Vec4d getStartRef() {
		return _start;
	}

	public Vec4d getDirRef() {
		return _direction;
	}

	/**
	 * Returns a new Ray as though this was passed through the Transform trans
	 * 返回一个新射线，就好像它通过了Transform trans一样
	 * @param trans
	 * @return
	 */
	public Ray transform(Transform trans) {
		return transform(trans.getMat4dRef());
	}

	/**
	 * Returns a new Ray as though this was passed through the Matrix mat
	 * 返回一个新的射线，就像它通过矩阵席一样
	 * @param mat
	 * @return
	 */
	public Ray transform(Mat4d mat) {
		Vec4d startTransed = new Vec4d(0.0d, 0.0d, 0.0d, 1.0d);
		startTransed.mult4(mat, _start);

		Vec4d dirTransed = new Vec4d(0.0d, 0.0d, 0.0d, 1.0d);
		dirTransed.mult4(mat, _direction);
		dirTransed.normalize3();

		return new Ray(startTransed, dirTransed);
	}

	/**
	 * Returns a new vector4d representing the point 'dist' distance along this ray
	 * 返回一个新的vector4d，表示沿此射线的点“dist”距离
	 * @param dist
	 * @return
	 */
	public Vec3d getPointAtDist(double dist) {
		Vec3d ret = new Vec3d(_direction);
		ret.scale3(dist);
		ret.add3(_start);
		return ret;
	}

	/**
	 * Returns the distance along the ray to the point on the ray closest to given point, this
	 * can be negative if the point given is effectively behind the ray
	 * 返回沿射线到离给定点最近的射线上的点的距离，如果给定的点有效地在射线后面，这可以是负的
	 * @param point
	 * @return
	 */
	public double getDistAlongRay(Vec3d point) {
		Vec3d diff = new Vec3d(point);
		diff.sub3(_start);
		return diff.dot3(_direction);
	}

	@Override
	public String toString() {
		return "Orig: " + _start.toString() + " Dir: " + _direction.toString();
	}
}


