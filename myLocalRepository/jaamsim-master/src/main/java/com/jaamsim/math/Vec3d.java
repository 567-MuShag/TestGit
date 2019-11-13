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

import com.jaamsim.input.Input;

public class Vec3d extends Vec2d {

public double z;

/**
 * Construct a Vec3d initialized to (0,0,0);构造初始化为(0,0,0)的Vec3d;
 */
public Vec3d() {
	x = 0.0d;
	y = 0.0d;
	z = 0.0d;
}

/**
 * Construct a Vec3d initialized to (v.x, v.y, v.z);
 * 构造一个初始化为(v。x, v。y, v.z); v包含初始值
 * @param v the Vec3d containing the initial values
 * @throws NullPointerException if v is null
 */
public Vec3d(Vec3d v) {
	x = v.x;
	y = v.y;
	z = v.z;
}

/**
 * Construct a Vec3d initialized to (x, y, z);
 * 构造一个初始化的Vec3d,初始值为传入的值
 * @param x the initial x value
 * @param y the initial y value
 * @param z the initial z value
 */
public Vec3d(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
}

/**
 * Returns a string representation of this vec.返回此vec的字符串表示形式。
 */
@Override
public String toString() {
	StringBuilder tmp = new StringBuilder();
	tmp.append(x);
	//  ？？？Input.SEPARATOR的作用
	tmp.append(Input.SEPARATOR).append(y);
	tmp.append(Input.SEPARATOR).append(z);
	return tmp.toString();
}

/**
 * Tests the first three components are exactly equal.测试前三个组件完全相同。
 *
 * This returns true if the x,y,z components compare as equal using the ==
 * operator.  Note that NaN will always return false, and -0.0 and 0.0
 * will compare as equal.
 * 如果x、y、z分量使用==操作符进行相等比较，则返回true。注意NaN总是返回false， -0和0。0比较起来是相等的。
 * @throws NullPointerException if v is null
 */
public boolean equals3(Vec3d v) {
	return x == v.x && y == v.y && z == v.z;
}
//判断两个三维向量重合
public boolean near3(Vec3d v) {
	return MathUtils.near(x, v.x) &&
	       MathUtils.near(y, v.y) &&
	       MathUtils.near(z, v.z);
}

/**
 * Set this Vec3d with the values (v.x, v.y, v.z); 三维向量的set方法（传入向量）
 * @param v the Vec3d containing the values
 * @throws NullPointerException if v is null
 */
public void set3(Vec3d v) {
	this.x = v.x;
	this.y = v.y;
	this.z = v.z;
}

/**
 * Set this Vec3d with the values (x, y, z); 三维向量的set方法（传入xyz参数）
 */
public void set3(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
}

/**
 * Add v to this Vec3d: this = this + v  对这个向量加上传入的向量
 * @throws NullPointerException if v is null
 */
public void add3(Vec3d v) {
	this.x = this.x + v.x;
	this.y = this.y + v.y;
	this.z = this.z + v.z;
}

/**
 * Add v1 to v2 into this Vec3d: this = v1 + v2  将传入的两个三维向量相加赋给这个向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void add3(Vec3d v1, Vec3d v2) {
	this.x = v1.x + v2.x;
	this.y = v1.y + v2.y;
	this.z = v1.z + v2.z;
}

/**
 * Subtract v from this Vec3d: this = this - v  对这个向量减去传入的向量
 * @throws NullPointerException if v is null
 */
public void sub3(Vec3d v) {
	this.x = this.x - v.x;
	this.y = this.y - v.y;
	this.z = this.z - v.z;
}

/**
 * Subtract v2 from v1 into this Vec3d: this = v1 - v2  两个传入的向量相减并赋给这个向量
 * 从v1减去v2到这个向量3d:这个等于v1 - v2
 * @throws NullPointerException if v1 or v2 are null
 */
public void sub3(Vec3d v1, Vec3d v2) {
	this.x = v1.x - v2.x;
	this.y = v1.y - v2.y;
	this.z = v1.z - v2.z;
}

/**
 * Multiply the elements of this Vec3d by v: this = this * v  将这个向量的xyz分别乘上传入的向量的xyz，并赋给这个向量
 * @throws NullPointerException if v is null
 */
public void mul3(Vec3d v) {
	this.x = this.x * v.x;
	this.y = this.y * v.y;
	this.z = this.z * v.z;
}

/**
 * Multiply the elements of v1 and v2 into this Vec3d: this = v1 * v2  将两个传入向量的xyz分别相乘，并赋给这个向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void mul3(Vec3d v1, Vec3d v2) {
	this.x = v1.x * v2.x;
	this.y = v1.y * v2.y;
	this.z = v1.z * v2.z;
}

/**
 * Set this Vec3d to the minimum of this and v: this = min(this, v)  比较这个向量和传入向量的xyz，分别取xyz的最小值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void min3(Vec3d v) {
	this.x = Math.min(this.x, v.x);
	this.y = Math.min(this.y, v.y);
	this.z = Math.min(this.z, v.z);
}

/**
 * Set this Vec3d to the minimum of v1 and v2: this = min(v1, v2)  比较传入的两个向量的xyz，取xyz的最小值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void min3(Vec3d v1, Vec3d v2) {
	this.x = Math.min(v1.x, v2.x);
	this.y = Math.min(v1.y, v2.y);
	this.z = Math.min(v1.z, v2.z);
}

/**
 * Set this Vec3d to the maximum of this and v: this = max(this, v)     比较这个向量和传入向量的xyz，分别取xyz的最大值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void max3(Vec3d v) {
	this.x = Math.max(this.x, v.x);
	this.y = Math.max(this.y, v.y);
	this.z = Math.max(this.z, v.z);
}

/**
 * Set this Vec3d to the maximum of v1 and v2: this = max(v1, v2)  比较传入的两个向量的xyz，取xyz的最大值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void max3(Vec3d v1, Vec3d v2) {
	this.x = Math.max(v1.x, v2.x);
	this.y = Math.max(v1.y, v2.y);
	this.z = Math.max(v1.z, v2.z);
}

/**
 * Return the 3-component dot product of v1 and v2  计算传入的三维向量v1和v2的点乘
 * Internal helper to help with dot, mag and magSquared
 */
private final double _dot3(Vec3d v1, Vec3d v2) {
	double ret;
	ret  = v1.x * v2.x;
	ret += v1.y * v2.y;
	ret += v1.z * v2.z;
	return ret;
}

/**
 * Return the 3-component dot product of this Vec3d with v  计算这个向量和传入向量v的点乘
 * @throws NullPointerException if v is null
 */
public double dot3(Vec3d v) {
	return _dot3(this, v);
}

/**
 * Return the 3-component magnitude of this Vec3d   计算这个向量的模
 */
public double mag3() {
	return Math.sqrt(_dot3(this, this));
}

/**
 * Return the 3-component magnitude squared of this Vec3d  返回这个向量和这个向量的点乘
 */
public double magSquare3() {
	return _dot3(this, this);
}
/**
 * 对传入的向量进行标准化的方法
 * @param v
 */
private void _norm3(Vec3d v) {
	double mag = _dot3(v, v);
	//这里调用了Vec2d类中的nonNormalMag方法，该方法用于判断模mag能否用于标准化操作，返回true表示不能用该值进行标准化
	if (nonNormalMag(mag)) {
		//mag不能用于标准化则自定义一个标准标准化向量
		this.x = 0.0d;
		this.y = 0.0d;
		this.z = 1.0d;
		return;
	}
	//向量标准化操作
	mag = Math.sqrt(mag);
	this.x = v.x / mag;
	this.y = v.y / mag;
	this.z = v.z / mag;
}

/**
 * Normalize the first three components in-place 将前三个组件标准化
 *
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * 如果Vec的大小为零，或者包含NaN或Inf，这将把所有的分量都设为零，最后一个分量设为1。就是自定义给出一个标准化向量
 */
public void normalize3() {
	_norm3(this);
}

/**
 * Set the first three components to the normalized values of v   将前三个组件设置为v的规格化值
 *
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * 如果Vec的大小为零，或者包含NaN或Inf，这将把所有的分量都设为零，最后一个分量设为1。就是自定义给出一个标准化向量
 * @throws NullPointerException if v is null
 */
public void normalize3(Vec3d v) {
	_norm3(v);
}

/**
 * Scale the first three components of this Vec: this = scale * this
 * 对这个向量进行缩放，比例是scale
 */
public void scale3(double scale) {
	this.x = this.x * scale;
	this.y = this.y * scale;
	this.z = this.z * scale;
}

/**
 * Scale the first three components of v into this Vec: this = scale * v
 * 对传入的向量进行缩放，比例是scale
 * @throws NullPointerException if v is null
 */
public void scale3(double scale, Vec3d v) {
	this.x = v.x * scale;
	this.y = v.y * scale;
	this.z = v.z * scale;
}

/**
 * Linearly interpolate between a, b into this Vec: this = (1 - ratio) * a + ratio * b
 * 对传入的向量a和向量b   进行(1 - ratio) * a + ratio * b线性变化
 * @throws NullPointerException if a or b are null
 */
public void interpolate3(Vec3d a, Vec3d b, double ratio) {
	double temp = 1.0d - ratio;
	this.x = temp * a.x + ratio * b.x;
	this.y = temp * a.y + ratio * b.y;
	this.z = temp * a.z + ratio * b.z;
}

/**
 * Multiply v by m and store into this Vec: this = m x v  矩阵m乘向量v  向量v为列向量
 * @throws NullPointerException if m or v are null
 */
public void mult3(Mat4d m, Vec3d v) {
	double _x = m.d00 * v.x + m.d01 * v.y + m.d02 * v.z;
	double _y = m.d10 * v.x + m.d11 * v.y + m.d12 * v.z;
	double _z = m.d20 * v.x + m.d21 * v.y + m.d22 * v.z;

	this.x = _x;
	this.y = _y;
	this.z = _z;
}

/**
 * Like mult3 but includes an implicit w = 1 term to include the translation part of the matrix  矩阵m乘向量v  向量v为列向量
 * 与mult3类似，但是包含了一个隐含的w = 1项来包含矩阵的平移部分
 * @param m
 * @param v
 */
public void multAndTrans3(Mat4d m, Vec3d v) {
	double _x = m.d00 * v.x + m.d01 * v.y + m.d02 * v.z + m.d03;
	double _y = m.d10 * v.x + m.d11 * v.y + m.d12 * v.z + m.d13;
	double _z = m.d20 * v.x + m.d21 * v.y + m.d22 * v.z + m.d23;

	this.x = _x;
	this.y = _y;
	this.z = _z;
}

/**
 * Multiply m by v and store into this Vec: this = v x m     向量v乘矩阵m  向量v为行向量
 * @throws NullPointerException if m or v are null
 */
public void mult3(Vec3d v, Mat4d m) {
	double _x = v.x * m.d00 + v.y * m.d10 + v.z * m.d20;
	double _y = v.x * m.d01 + v.y * m.d11 + v.z * m.d21;
	double _z = v.x * m.d02 + v.y * m.d12 + v.z * m.d22;

	this.x = _x;
	this.y = _y;
	this.z = _z;
}

/**
 * Set this Vec3d to the cross product of this and v: this = this X v
 * 这个向量和传入的向量进行叉乘，将结果赋给这个向量
 * @throws NullPointerException if v is null
 */
public void cross3(Vec3d v) {
	// Use temp vars to deal with this passed in as the argument
	double _x = this.y * v.z - this.z * v.y;
	double _y = this.z * v.x - this.x * v.z;
	double _z = this.x * v.y - this.y * v.x;

	this.x = _x;
	this.y = _y;
	this.z = _z;
}

/**
 * Set this Vec3d to the cross product of v1 and v2: this = v1 X v2
 * 传入的向量v1和v2进行叉乘，将结果赋给这个向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void cross3(Vec3d v1, Vec3d v2) {
	// Use temp vars to deal with this passed in as the argument
	double _x = v1.y * v2.z - v1.z * v2.y;
	double _y = v1.z * v2.x - v1.x * v2.z;
	double _z = v1.x * v2.y - v1.y * v2.x;

	this.x = _x;
	this.y = _y;
	this.z = _z;
}
}
