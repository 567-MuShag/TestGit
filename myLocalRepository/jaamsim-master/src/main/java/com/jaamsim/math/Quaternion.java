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
 * Quaternion class, stored as an array of 4 doubles
 * 四元数类，存储为4个双精度数的数组
 * @author Matt Chudleigh
 *
 */
public class Quaternion {

public double x;
public double y;
public double z;
public double w;

/**
 * Default returns an identity quaternion (real = 1, imaginary = 0)
 * 默认返回一个恒等四元数(实数= 1，虚数= 0)
 */
public Quaternion() {
	x = 0.0d;
	y = 0.0d;
	z = 0.0d;
	w = 1.0d;
}

/**
 * Quaternion constructor with explicit data, may need to be explicitly normalized
 * 具有显式数据的四元数构造函数可能需要显式规范化
 * @param ix
 * @param iy
 * @param iz
 * @param r
 */
public Quaternion(double ix, double iy, double iz, double r) {
	x = ix;
	y = iy;
	z = iz;
	w = r;
}

public Quaternion(Quaternion q) {
	x = q.x;
	y = q.y;
	z = q.z;
	w = q.w;
}

/**
 * Set this Quaternion with the values (q.x, q.y, q.z, q.w);
 * 用值(q)设置这个四元数。x,问。y,问。z, q.w);
 * @param q the Quaternion containing the values
 * @throws NullPointerException if q is null
 */
public void set(Quaternion q) {
	this.x = q.x;
	this.y = q.y;
	this.z = q.z;
	this.w = q.w;
}

/**
 * Set this Quaternion from Euler angles, specifically the kind of euler angles
 * used by Java3d (which seems to be rotation around global x, then y, then z)
 * 从欧拉角设置这个四元数，特别是Java3d使用的欧拉角(似乎是围绕全局x、y和z旋转)
 * @param v the Vec3d containing the x,y,z angles
 * @throws NullPointerException if v is null
 */
public void setEuler3(Vec3d v) {
	// This will almost certainly be a performance bottleneck before too long
	Quaternion tmp = new Quaternion();
	this.setRotXAxis(v.x);

	tmp.setRotYAxis(v.y);
	this.mult(tmp, this);

	tmp.setRotZAxis(v.z);
	this.mult(tmp, this);
}

/**
 * Set this Quaternion to a rotation about the X-axis.
 * 将这个四元数设置为绕x轴旋转。
 * @param angle the angle to rotate through
 */
public void setRotXAxis(double angle) {
	double halfAngle = 0.5d * angle;
	this.x = Math.sin(halfAngle);
	this.y = 0.0d;
	this.z = 0.0d;
	this.w = Math.cos(halfAngle);
}

/**
 * Set this Quaternion to a rotation about the Y-axis.
 * 将这个四元数设置为绕y轴旋转。
 * @param angle the angle to rotate through
 */
public void setRotYAxis(double angle) {
	double halfAngle = 0.5d * angle;
	this.x = 0.0d;
	this.y = Math.sin(halfAngle);
	this.z = 0.0d;
	this.w = Math.cos(halfAngle);
}

/**
 * Set this Quaternion to a rotation about the Z-axis.
 * 将这个四元数设置为绕z轴旋转。
 * @param angle the angle to rotate through
 */
public void setRotZAxis(double angle) {
	double halfAngle = 0.5d * angle;
	this.x = 0.0d;
	this.y = 0.0d;
	this.z = Math.sin(halfAngle);
	this.w = Math.cos(halfAngle);
}

/**
 * Set this Quaternion from a rotation in axis-angle form.
 * 从轴角形式的旋转中设置此四元数。
 * @param axis the about which to rotate  要旋转的轴
 * @param angle the angle to rotate through in radians  以弧度旋转的角度
 * @throws NullPointerException if axis is null
 */
public void setAxisAngle(Vec3d axis, double angle) {
	double halfAngle = 0.5d * angle;
	Vec3d v = new Vec3d(axis);
	v.normalize3();
	v.scale3(Math.sin(halfAngle));

	this.x = v.x; this.y = v.y; this.z = v.z;
	this.w = Math.cos(halfAngle);
}

/**
 * Factory that returns a Quaternion that would rotate one direction into the other.
 * 返回一个四元数的工厂，该四元数将从一个方向旋转到另一个方向。
 * Only valid for vectors of the same length
 * 只对相同长度的向量有效
 * @param from
 * @param to
 * @return
 */
public static Quaternion transformVectors(Vec4d from, Vec4d to) {

	Vec4d f = new Vec4d(from);
	Vec4d t = new Vec4d(to);

	f.normalize3();
	t.normalize3();

	Vec4d cross = new Vec4d(0.0d, 0.0d, 0.0d, 1.0d);
	cross.cross3(f, t);

	double angle = Math.asin(cross.mag3());
	cross.normalize3();

	Quaternion ret = new Quaternion();
	ret.setAxisAngle(cross, angle);
	return ret;
}
/**
 * 传入的两个四元数的点乘
 * @param q1
 * @param q2
 * @return
 */
private final double _dot4(Quaternion q1, Quaternion q2) {
	double ret;
	ret  = q1.x * q2.x;
	ret += q1.y * q2.y;
	ret += q1.z * q2.z;
	ret += q1.w * q2.w;
	return ret;
}
/**
 * 计算这个四元数自身的点乘
 * @return
 */
public double magSquared() {
	return _dot4(this, this);
}
/**
 * 计算这个四元数的模
 * @return
 */
public double mag() {
	return Math.sqrt(_dot4(this, this));
}

private void _norm(Quaternion q) {
	//计算传入的四元数的点乘值
	double mag = _dot4(q, q);
	if (MathUtils.isSmall(mag)) { // The quaternion is of length 0, simply return an identity 四元数长度为0，只需返回一个标识符
		this.x = 0.0d; this.y = 0.0d; this.z = 0.0d; this.w = 1.0d;
		return;
	}
	//当传入的四元数长度不为0的时候，对该四元数进行标准化
	mag = Math.sqrt(mag);
	this.x = q.x / mag;
	this.y = q.y / mag;
	this.z = q.z / mag;
	this.w = q.w / mag;
}

/**
 * Normalize the quaternion in place
 * 规范化这个四元素
 */
public void normalize() {
	_norm(this);
}

/**
 * Set this quaternion to the normalized value of q
 * 将这个四元数设置为q的规格化值
 * @throws NullPointerException if q is null
 */
public void normalize(Quaternion q) {
	_norm(q);
}

/**
 * Set this Quarternion to its complex conjugate
 * 把这个四分之一变成它的共轭复数
 */
public void conjugate() {
	x *= -1.0d;
	y *= -1.0d;
	z *= -1.0d;
}

/**
 * Set this Quarternion to the complex conjugate of q
 * 把这个四分之一变成q的复共轭
 * @throws NullPointerException if q is null
 */
public void conjugate(Quaternion q) {
	this.x = q.x * -1.0d;
	this.y = q.y * -1.0d;
	this.z = q.z * -1.0d;
	this.w = q.w;
}

/**
 * Quaternion multiplication, mathematically equivalent to applying both rotations in order.
 * 四元数乘法，数学上等价于按顺序应用两个旋转。
 * Sets this to a*b
 * @param q
 * @param res
 */
public void mult(Quaternion a, Quaternion b) {
	double _x = a.w*b.x + a.x*b.w + a.y*b.z - a.z*b.y;
	double _y = a.w*b.y + a.y*b.w + a.z*b.x - a.x*b.z;
	double _z = a.w*b.z + a.z*b.w + a.x*b.y - a.y*b.x;
	double _w = a.w*b.w - a.x*b.x - a.y*b.y - a.z*b.z;

	x = _x;
	y = _y;
	z = _z;
	w = _w;
}

public boolean isNormal() {
	//计算四元数的点乘值
	double magSquared = magSquared();
	//判断点乘值是否和1.0重合
	return MathUtils.near(magSquared, 1.0);
}
/**
 * 计算传入的四元数和这个四元数的点乘值
 * @param q
 * @return
 */
public double dot(Quaternion q) {
	return _dot4(this, q);
}

/**
 * Weighted linear interpolation between quaternions when
 * weight = 1 -> res = q
 * weight = 0 -> res = this
 * 四元数之间的加权线性插值，当权=1时候 res=q；当权=0的时候res=this
 * @param q - the other quaternion  传入的四元数
 * @param weight - the weight to blend with  权重
 * @param res - the result  结果四元数
 */
public void lerp(Quaternion q, double weight, Quaternion res) {
	double weight1 = 1.0d - weight;
	res.x = x * weight1 + q.x * weight;
	res.y = y * weight1 + q.y * weight;
	res.z = z * weight1 + q.z * weight;
	res.w = w * weight1 + q.w * weight;
}

/**
 * Spherical linear interpolation between quaternions, look up slerp if you are unsure
 * weight = 1 -> res = q
 * weight = 0 -> res = this
 * 四元数之间的球面线性插值，如果你不确定，请查阅slerp
 * 当权=1的时候res=q，当权=0的时候res=this
 * @param q - the other quaternion
 * @param weight - the weight to blend with
 * @param res - the result
 */
public void slerp(Quaternion q, double weight, Quaternion res) {
	double cosTheta = dot(q);
	if (cosTheta > 0.95) { // close enough, just lerp it
		lerp(q, weight, res);
		res.normalize();
		return;
	}

	double theta = Math.acos(cosTheta);
	double sinTheta = Math.sin(theta);

	if (MathUtils.isSmall(sinTheta)) {
		// TODO: some kind of decent default as the two quaternions are nearly opposite
		throw new IllegalArgumentException("Cannot slerp two opposite quaternions");
	}
	double thisScale = Math.sin((1.0 - weight)*theta) / sinTheta;
	double qScale = Math.sin(weight*theta) / sinTheta;

	res.x = x * thisScale + q.x * qScale;
	res.y = y * thisScale + q.y * qScale;
	res.z = z * thisScale + q.z * qScale;
	res.w = w * thisScale + q.w * qScale;
}

@Override
public boolean equals(Object o) {
	if (!(o instanceof Quaternion)) return false;
	Quaternion q = (Quaternion)o;

	return q.x == x && q.y == y && q.z == z && q.w == w;
}
/**
 * 判断两个四元数是否重合
 * @param q
 * @return
 */
public boolean near(Quaternion q) {
	return MathUtils.near(x, q.x)
	    && MathUtils.near(y, q.y)
	    && MathUtils.near(z, q.z)
	    && MathUtils.near(w, q.w);
}

@Override
public int hashCode() {
	assert false : "hashCode not designed";
	return 42; // any arbitrary constant will do
}

@Override
public String toString()
{
	return "[(" + x + ", "  + y + ", "  + z + ")i, "  + w + "]";
}

} // class Quaternion
