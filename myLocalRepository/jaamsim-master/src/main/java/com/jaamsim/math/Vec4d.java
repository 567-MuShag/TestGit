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


public class Vec4d extends Vec3d {

public double w;

/**
 * Construct a Vec4d initialized to (0,0,0,0);   （0,0,0,0）初始化一个四维向量
 */
public Vec4d() {
	x = 0.0d;
	y = 0.0d;
	z = 0.0d;
	w = 0.0d;
}

/**
 * Construct a Vec4d initialized to (v.x, v.y, v.z, v.w);
 * 构造一个初始化为(v。x, v。y, v.z，v.w)的四维向量; v包含初始值
 * @param v the Vec4d containing the initial values
 * @throws NullPointerException if v is null
 */
public Vec4d(Vec4d v) {
	x = v.x;
	y = v.y;
	z = v.z;
	w = v.w;
}

/**
 * Construct a Vec4d from a Vec3d and a w parameter
 * 根据给定的三维向量和参数w构造一个四维向量
 * @param v the Vec3d containing the initial values for x,y,z
 * @param w the new 'w' value
 * @throws NullPointerException if v is null
 */
public Vec4d(Vec3d v, double w) {
	x = v.x;
	y = v.y;
	z = v.z;
	this.w = w;
}

/**
 * Construct a Vec4d initialized to (x, y, z, w);
 * 根据传入的xyzw参数构造一个四维向量
 * @param x the initial x value
 * @param y the initial y value
 * @param z the initial z value
 * @param w the initial w value
 */
public Vec4d(double x, double y, double z, double w) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.w = w;
}

/**
 * Returns a string representation of this vec.
 * 将向量转换成字符串形式 
 */
@Override
public String toString() {
	StringBuilder tmp = new StringBuilder("(");
	tmp.append(x);
	tmp.append(", ").append(y);
	tmp.append(", ").append(z);
	tmp.append(", ").append(w);
	tmp.append(")");
	return tmp.toString();
}

/**
 * Tests the first four components are exactly equal.
 *	判断两个向量是否相同
 * This returns true if the x,y,z,w components compare as equal using the ==
 * operator.  Note that NaN will always return false, and -0.0 and 0.0
 * will compare as equal.
 * 如果x、y、z、w分量使用==操作符进行相等比较，则返回true。请注意，NaN总是返回false，并且-0和0.0比较起来是相等的。
 * @throws NullPointerException if v is null
 */
public boolean equals4(Vec4d v) {
	return x == v.x && y == v.y && z == v.z && w == v.w;
}
//判断两个四维向量是否相同
public boolean near4(Vec4d v) {
	return MathUtils.near(x, v.x) &&
	       MathUtils.near(y, v.y) &&
	       MathUtils.near(z, v.z) &&
	       MathUtils.near(w, v.w);
}

/**
 * Set this Vec4d with the values (v.x, v.y, v.z, v.w);
 * 四维向量的set方法（传入一个四维向量）
 * @param v the Vec4d containing the values
 * @throws NullPointerException if v is null
 */
public void set4(Vec4d v) {
	this.x = v.x;
	this.y = v.y;
	this.z = v.z;
	this.w = v.w;
}

/**
 * Set this Vec4d with the values (x, y, z, w);
 * 四维向量的set方法（传入xyzw参数）
 */
public void set4(double x, double y, double z, double w) {
	this.x = x;
	this.y = y;
	this.z = z;
	this.w = w;
}

/**
 * Add v to this Vec4d: this = this + v
 * 这个向量加上传入的向量，结果赋给这个向量
 * @throws NullPointerException if v is null
 */
public void add4(Vec4d v) {
	this.x = this.x + v.x;
	this.y = this.y + v.y;
	this.z = this.z + v.z;
	this.w = this.w + v.w;
}

/**
 * Add v1 to v2 into this Vec4d: this = v1 + v2
 * 传入的四维向量v1和v2，v1加上v2，将结果赋给这个向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void add4(Vec4d v1, Vec4d v2) {
	this.x = v1.x + v2.x;
	this.y = v1.y + v2.y;
	this.z = v1.z + v2.z;
	this.w = v1.w + v2.w;
}

/**
 * Subtract v from this Vec4d: this = this - v
 * 这个向量减去传入的向量，结果赋给这个向量
 * @throws NullPointerException if v is null
 */
public void sub4(Vec4d v) {
	this.x = this.x - v.x;
	this.y = this.y - v.y;
	this.z = this.z - v.z;
	this.w = this.w - v.w;
}

/**
 * Subtract v2 from v1 into this Vec4d: this = v1 - v2
 * 传入的四维向量v1和v2，v1减去v2,将结果赋给这个向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void sub4(Vec4d v1, Vec4d v2) {
	this.x = v1.x - v2.x;
	this.y = v1.y - v2.y;
	this.z = v1.z - v2.z;
	this.w = v1.w - v2.w;
}

/**
 * Multiply the elements of this Vec4d by v: this = this * v
 * 将这个四维向量乘上传入的四维向量，结果赋给这个四维向量
 * @throws NullPointerException if v is null
 */
public void mul4(Vec4d v) {
	this.x = this.x * v.x;
	this.y = this.y * v.y;
	this.z = this.z * v.z;
	this.w = this.w * v.w;
}

/**
 * Multiply the elements of v1 and v2 into this Vec4d: this = v1 * v2
 * 传入的四维向量v1乘上传入的四维向量v2，并将结果赋给这个向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void mul4(Vec4d v1, Vec4d v2) {
	this.x = v1.x * v2.x;
	this.y = v1.y * v2.y;
	this.z = v1.z * v2.z;
	this.w = v1.w * v2.w;
}

/**
 * Set this Vec4d to the minimum of this and v: this = min(this, v)
 * 比较这个向量和传入四维向量的xyzw,取最小值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void min4(Vec4d v) {
	this.x = Math.min(this.x, v.x);
	this.y = Math.min(this.y, v.y);
	this.z = Math.min(this.z, v.z);
	this.w = Math.min(this.w, v.w);
}

/**
 * Set this Vec4d to the minimum of v1 and v2: this = min(v1, v2)
 * 比较传入的四维向量v1和v2的xyzw，取最小值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void min4(Vec4d v1, Vec4d v2) {
	this.x = Math.min(v1.x, v2.x);
	this.y = Math.min(v1.y, v2.y);
	this.z = Math.min(v1.z, v2.z);
	this.w = Math.min(v1.w, v2.w);
}

/**
 * Set this Vec4d to the maximum of this and v: this = max(this, v)
 * 比较这个向量和传入四维向量的xyzw,取最大值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void max4(Vec4d v) {
	this.x = Math.max(this.x, v.x);
	this.y = Math.max(this.y, v.y);
	this.z = Math.max(this.z, v.z);
	this.w = Math.max(this.w, v.w);
}

/**
 * Set this Vec4d to the maximum of v1 and v2: this = max(v1, v2)
 * 比较传入的四维向量v1和v2的xyzw，取最大值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void max4(Vec4d v1, Vec4d v2) {
	this.x = Math.max(v1.x, v2.x);
	this.y = Math.max(v1.y, v2.y);
	this.z = Math.max(v1.z, v2.z);
	this.w = Math.max(v1.w, v2.w);
}

/**
 * Return the 4-component dot product of v1 and v2
 * Internal helper to help with dot, mag and magSquared
 * 计算传入的三维向量v1和v2的点乘
 */
private final double _dot4(Vec4d v1, Vec4d v2) {
	double ret;
	ret  = v1.x * v2.x;
	ret += v1.y * v2.y;
	ret += v1.z * v2.z;
	ret += v1.w * v2.w;
	return ret;
}

/**
 * Return the 4-component dot product of this Vec4d with v
 * 计算这个向量和传入向量v的点乘
 * @throws NullPointerException if v is null
 */
public double dot4(Vec4d v) {
	return _dot4(this, v);
}

/**
 * Return the 4-component magnitude of this Vec4d
 * 计算这个向量的模
 */
public double mag4() {
	return Math.sqrt(_dot4(this, this));
}

/**
 * Return the 4-component magnitude squared of this Vec4d
 * 返回这个向量自己的点乘
 */
public double magSquare4() {
	return _dot4(this, this);
}
/**
 * 对传入的向量进行标准化的方法
 * @param v
 */
private void _norm4(Vec4d v) {
	double mag = _dot4(v, v);
	//这里调用了Vec2d类中的nonNormalMag方法，该方法用于判断模mag能否用于标准化操作，返回true表示不能用该值进行标准化
	if (nonNormalMag(mag)) {
		//mag不能用于标准化则自定义一个标准标准化向量
		this.x = 0.0d;
		this.y = 0.0d;
		this.z = 0.0d;
		this.w = 1.0d;
		return;
	}
	//向量标准化操作
	mag = Math.sqrt(mag);
	this.x = v.x / mag;
	this.y = v.y / mag;
	this.z = v.z / mag;
	this.w = v.w / mag;
}

/**
 * Normalize the first four components in-place
 *将前四个组件标准化
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * 如果Vec的大小为零，或者包含NaN或Inf，这将把所有的分量都设为零，最后一个分量设为1。就是自定义给出一个标准化向量
 */
public void normalize4() {
	_norm4(this);
}

/**
 * Set the first four components to the normalized values of v
 *将前四个组件设置为v的规格化值
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * 如果Vec的大小为零，或者包含NaN或Inf，这将把所有的分量都设为零，最后一个分量设为1。就是自定义给出一个标准化向量
 * @throws NullPointerException if v is null
 */
public void normalize4(Vec4d v) {
	_norm4(v);
}

/**
 * Scale the first four components of this Vec: this = scale * this
 * 对这个向量进行缩放，比例是scale
 */
public void scale4(double scale) {
	this.x = this.x * scale;
	this.y = this.y * scale;
	this.z = this.z * scale;
	this.w = this.w * scale;
}

/**
 * Scale the first four components of v into this Vec: this = scale * v
 * 对传入的四维向量进行缩放，比例是scale
 * @throws NullPointerException if v is null
 */
public void scale4(double scale, Vec4d v) {
	this.x = v.x * scale;
	this.y = v.y * scale;
	this.z = v.z * scale;
	this.w = v.w * scale;
}

/**
 * Linearly interpolate between a, b into this Vec: this = (1 - ratio) * a + ratio * b
 * 对传入的向量a和向量b   进行(1 - ratio) * a + ratio * b线性变化
 * @throws NullPointerException if a or b are null
 */
public void interpolate4(Vec4d a, Vec4d b, double ratio) {
	double temp = 1.0d - ratio;
	this.x = temp * a.x + ratio * b.x;
	this.y = temp * a.y + ratio * b.y;
	this.z = temp * a.z + ratio * b.z;
	this.w = temp * a.w + ratio * b.w;
}

/**
 * Multiply v by m and store into this Vec: this = m x v  矩阵m乘向量v  向量v为列向量
 * @throws NullPointerException if m or v are null
 */
public void mult4(Mat4d m, Vec4d v) {
	double _x = m.d00 * v.x + m.d01 * v.y + m.d02 * v.z + m.d03 * v.w;
	double _y = m.d10 * v.x + m.d11 * v.y + m.d12 * v.z + m.d13 * v.w;
	double _z = m.d20 * v.x + m.d21 * v.y + m.d22 * v.z + m.d23 * v.w;
	double _w = m.d30 * v.x + m.d31 * v.y + m.d32 * v.z + m.d33 * v.w;

	this.x = _x;
	this.y = _y;
	this.z = _z;
	this.w = _w;
}

/**
 * Multiply m by v and store into this Vec: this = v x m    矩阵m乘向量v  向量v为列向量
 * @throws NullPointerException if m or v are null
 */
public void mult4(Vec4d v, Mat4d m) {
	double _x = v.x * m.d00 + v.y * m.d10 + v.z * m.d20 + v.w * m.d30;
	double _y = v.x * m.d01 + v.y * m.d11 + v.z * m.d21 + v.w * m.d31;
	double _z = v.x * m.d02 + v.y * m.d12 + v.z * m.d22 + v.w * m.d32;
	double _w = v.x * m.d03 + v.y * m.d13 + v.z * m.d23 + v.w * m.d33;

	this.x = _x;
	this.y = _y;
	this.z = _z;
	this.w = _w;
}

public void setByInd(int index, double val) {
	switch (index) {
	case 0:
		x = val;
		return;
	case 1:
		y = val;
		return;
	case 2:
		z = val;
		return;
	case 3:
		w = val;
		return;
	}
	assert(false);
}

public double getByInd(int index) {
	switch (index) {
	case 0:
		return x;
	case 1:
		return y;
	case 2:
		return z;
	case 3:
		return w;
	}
	assert(false);
	return 0;
}

}
