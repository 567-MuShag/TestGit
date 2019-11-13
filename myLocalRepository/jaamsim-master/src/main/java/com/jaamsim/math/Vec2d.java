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

public class Vec2d {

public double x;
public double y;

/**
 * Construct a Vec2d initialized to (0,0);初始化构造一个二维向量(0,0)，即构造一个零向量
 */
public Vec2d() {
	x = 0.0d;
	y = 0.0d;
}

/**
 * Construct a Vec2d initialized to (v.x, v.y);构造初始化为(v)的二维向量
 * @param v the Vec2d containing the initial values   向量v是包含初始值的，初始值为传入的值
 * @throws NullPointerException if v is null
 */
public Vec2d(Vec2d v) {
	x = v.x;
	y = v.y;
}

/**
 * Construct a Vec2d initialized to (x, y);传入double类型的x,y构造二维向量v
 * @param x the initial x value
 * @param y the initial y value
 */
public Vec2d(double x, double y) {
	this.x = x;
	this.y = y;
}

/**
 * Returns a string representation of this vec.  返回向量的字符串表示形式
 */
@Override
public String toString() {
	StringBuilder tmp = new StringBuilder("(");
	tmp.append(x);
	tmp.append(", ").append(y);
	tmp.append(")");
	return tmp.toString();
}

/**
 * Tests the first two components are exactly equal.测试这两个向量相同
 *
 * This returns true if the x,y components compare as equal using the ==
 * operator.  Note that NaN will always return false, and -0.0 and 0.0
 * will compare as equal.
 * 如果x、y分量使用==操作符进行相等比较，则返回true。注意NaN总是返回false， -0和0。0比较起来是相等的。
 * @throws NullPointerException if v is null
 */
public boolean equals2(Vec2d v) {
	return x == v.x && y == v.y;
}
//测试两个二维向量重合
public boolean near2(Vec2d v) {
	return MathUtils.near(x, v.x) &&
	       MathUtils.near(y, v.y);
}

/**
 * Set this Vec2d with the values (v.x, v.y); 二维向量的set方法（传入向量）
 * @param v the Vec2d containing the values
 * @throws NullPointerException if v is null
 */
public void set2(Vec3d v) {
	this.x = v.x;
	this.y = v.y;
}

/**
 * Set this Vec3d with the values (x, y); 二维向量的set方法(传入参数）
 */
public void set2(double x, double y) {
	this.x = x;
	this.y = y;
}

/**
 * Add v to this Vec2d: this = this + v 对这个向量加上传入的向量
 * @throws NullPointerException if v is null
 */
public void add2(Vec2d v) {
	this.x = this.x + v.x;
	this.y = this.y + v.y;
}

/**
 * Add v1 to v2 into this Vec2d: this = v1 + v2  v1向量加上v2向量生成新的向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void add2(Vec2d v1, Vec2d v2) {
	this.x = v1.x + v2.x;
	this.y = v1.y + v2.y;
}

/**
 * Subtract v from this Vec2d: this = this - v  这个向量减去传入的向量
 * @throws NullPointerException if v is null
 */
public void sub2(Vec2d v) {
	this.x = this.x - v.x;
	this.y = this.y - v.y;
}

/**
 * Subtract v2 from v1 into this Vec2d: this = v1 - v2  向量v1减去向量v2生成新的向量
 * @throws NullPointerException if v1 or v2 are null
 */
public void sub2(Vec2d v1, Vec2d v2) {
	this.x = v1.x - v2.x;
	this.y = v1.y - v2.y;
}

/**
 * Multiply the elements of this Vec2d by v: this = this * v  将这个二维向量乘上传入的二维向量v，两个向量的叉乘
 * @throws NullPointerException if v is null
 */
public void mul2(Vec2d v) {
	this.x = this.x * v.x;
	this.y = this.y * v.y;
}

/**
 * Multiply the elements of v1 and v2 into this Vec2d: this = v1 * v2 将两个传入的二维向量v1和v2相乘得到一个新的向量，两个向量的叉乘
 * @throws NullPointerException if v1 or v2 are null
 */
public void mul2(Vec2d v1, Vec2d v2) {
	this.x = v1.x * v2.x;
	this.y = v1.y * v2.y;
}

/**
 * Set this Vec2d to the minimum of this and v: this = min(this, v)  取这个向量和 传入的v向量的最小值赋值给这个向量
 * @throws NullPointerException if v is null
 */
public void min2(Vec2d v) {
	this.x = Math.min(this.x, v.x);
	this.y = Math.min(this.y, v.y);
}

/**
 * Set this Vec2d to the minimum of v1 and v2: this = min(v1, v2)  取传入的v1和v2向量的最小值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void min2(Vec2d v1, Vec2d v2) {
	this.x = Math.min(v1.x, v2.x);
	this.y = Math.min(v1.y, v2.y);
}

/**
 * Set this Vec2d to the maximum of this and v: this = max(this, v)  取这个向量和传入向量的最大值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void max2(Vec2d v) {
	this.x = Math.max(this.x, v.x);
	this.y = Math.max(this.y, v.y);
}

/**
 * Set this Vec2d to the maximum of v1 and v2: this = max(v1, v2) 	取传入向量v1和v2的最大值赋给这个向量
 * @throws NullPointerException if v is null
 */
public void max2(Vec2d v1, Vec2d v2) {
	this.x = Math.max(v1.x, v2.x);
	this.y = Math.max(v1.y, v2.y);
}

/**
 * Return the 2-component dot product of v1 and v2  返回v1向量和v2向量的点乘
 * Internal helper to help with dot, mag and magSquared
 */
private final double _dot2(Vec2d v1, Vec2d v2) {
	double ret;
	ret  = v1.x * v2.x;
	ret += v1.y * v2.y;
	return ret;
}

/**
 * Return the 2-component dot product of this Vec2d with v  返回这个向量和传入向量v的点乘
 * @throws NullPointerException if v is null
 */
public double dot2(Vec2d v) {
	//调用了private final double _dot2()方法
	return _dot2(this, v);
}

/**
 * Return the 2-component magnitude of this Vec2d  返回这个向量的模
 */
public double mag2() {
	return Math.sqrt(_dot2(this, this));
}

/**
 * Return the 2-component magnitude squared of this Vec2d  返回这个向量模的平方
 */
public double magSquare2() {
	return _dot2(this, this);
}

/**
 * Returns whether the given magnitude can be used to normalize a Vec 判断给定的向量模能否用于标准化
 * @param mag
 * @return
 */
static final boolean nonNormalMag(double mag) {
	//逻辑或有一个为真就为真
	return mag == 0.0d || Double.isNaN(mag) || Double.isInfinite(mag);
}
/**
 * 对传入的向量进行标准化的方法
 * @param v
 */
private void _norm2(Vec2d v) {
	double mag = _dot2(v, v); //计算向量v和向量v的点乘
	//如果nonNormalMag方法返回值为true即表示mag不能用作标准化
	if (nonNormalMag(mag)) {
		//给定一个标准化向量
		this.x = 0.0d;
		this.y = 1.0d;
		return;
	}
	//mag可以用于标准化，则求模，对向量v进行标准化
	mag = Math.sqrt(mag);
	this.x = v.x / mag;
	this.y = v.y / mag;
}

/**
 * Normalize the first two components in-place   将前两个组件标准化
 *
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * 如果Vec的大小为零，或者包含NaN或Inf，这将把所有的分量都设为零，最后一个分量设为1。就是自定义给出一个标准化向量
 */
public void normalize2() {
	_norm2(this);
}

/**
 * Set the first two components to the normalized values of v将前两个组件设置为v的规格化值
 *
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * 如果Vec的大小为零，或者包含NaN或Inf，这将把所有的分量都设为零，最后一个分量设为1。就是自定义给出一个标准化向量
 * @throws NullPointerException if v is null
 */
public void normalize2(Vec2d v) {
	_norm2(v);
}

/**
 * Scale the first two components of this Vec: this = scale * this
 * 按照比例scale对这个向量进行缩放
 */
public void scale2(double scale) {
	this.x = this.x * scale;
	this.y = this.y * scale;
}

/**
 * Scale the first two components of v into this Vec: this = scale * v
 * 按照比例scale对传入的向量进行缩放
 * @throws NullPointerException if v is null
 */
public void scale2(double scale, Vec2d v) {
	this.x = v.x * scale;
	this.y = v.y * scale;
}

/**
 * Linearly interpolate between a, b into this Vec: this = (1 - ratio) * a + ratio * b
 * 对传入的向量a和向量b以及参数ratio，按照(1 - ratio) * a + ratio * b进行线性处理
 * @throws NullPointerException if a or b are null
 */
public void interpolate2(Vec2d a, Vec2d b, double ratio) {
	double temp = 1.0d - ratio;
	this.x = temp * a.x + ratio * b.x;
	this.y = temp * a.y + ratio * b.y;
}

/**
 * Multiply v by m and store into this Vec: this = m x v  矩阵m乘向量v  向量v为列向量
 * @throws NullPointerException if m or v are null
 */
public void mult2(Mat4d m, Vec2d v) {
	double _x = m.d00 * v.x + m.d01 * v.y;
	double _y = m.d10 * v.x + m.d11 * v.y;

	this.x = _x;
	this.y = _y;
}

/**
 * Multiply m by v and store into this Vec: this = v x m  向量v乘矩阵m  向量v为行向量
 * @throws NullPointerException if m or v are null
 */
public void mult2(Vec2d v, Mat4d m) {
	double _x = v.x * m.d00 + v.y * m.d10;
	double _y = v.x * m.d01 + v.y * m.d11;

	this.x = _x;
	this.y = _y;
}
}
