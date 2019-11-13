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
 * ��Ԫ���࣬�洢Ϊ4��˫������������
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
 * Ĭ�Ϸ���һ�������Ԫ��(ʵ��= 1������= 0)
 */
public Quaternion() {
	x = 0.0d;
	y = 0.0d;
	z = 0.0d;
	w = 1.0d;
}

/**
 * Quaternion constructor with explicit data, may need to be explicitly normalized
 * ������ʽ���ݵ���Ԫ�����캯��������Ҫ��ʽ�淶��
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
 * ��ֵ(q)���������Ԫ����x,�ʡ�y,�ʡ�z, q.w);
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
 * ��ŷ�������������Ԫ�����ر���Java3dʹ�õ�ŷ����(�ƺ���Χ��ȫ��x��y��z��ת)
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
 * �������Ԫ������Ϊ��x����ת��
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
 * �������Ԫ������Ϊ��y����ת��
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
 * �������Ԫ������Ϊ��z����ת��
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
 * �������ʽ����ת�����ô���Ԫ����
 * @param axis the about which to rotate  Ҫ��ת����
 * @param angle the angle to rotate through in radians  �Ի�����ת�ĽǶ�
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
 * ����һ����Ԫ���Ĺ���������Ԫ������һ��������ת����һ������
 * Only valid for vectors of the same length
 * ֻ����ͬ���ȵ�������Ч
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
 * �����������Ԫ���ĵ��
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
 * ���������Ԫ������ĵ��
 * @return
 */
public double magSquared() {
	return _dot4(this, this);
}
/**
 * ���������Ԫ����ģ
 * @return
 */
public double mag() {
	return Math.sqrt(_dot4(this, this));
}

private void _norm(Quaternion q) {
	//���㴫�����Ԫ���ĵ��ֵ
	double mag = _dot4(q, q);
	if (MathUtils.isSmall(mag)) { // The quaternion is of length 0, simply return an identity ��Ԫ������Ϊ0��ֻ�践��һ����ʶ��
		this.x = 0.0d; this.y = 0.0d; this.z = 0.0d; this.w = 1.0d;
		return;
	}
	//���������Ԫ�����Ȳ�Ϊ0��ʱ�򣬶Ը���Ԫ�����б�׼��
	mag = Math.sqrt(mag);
	this.x = q.x / mag;
	this.y = q.y / mag;
	this.z = q.z / mag;
	this.w = q.w / mag;
}

/**
 * Normalize the quaternion in place
 * �淶�������Ԫ��
 */
public void normalize() {
	_norm(this);
}

/**
 * Set this quaternion to the normalized value of q
 * �������Ԫ������Ϊq�Ĺ��ֵ
 * @throws NullPointerException if q is null
 */
public void normalize(Quaternion q) {
	_norm(q);
}

/**
 * Set this Quarternion to its complex conjugate
 * ������ķ�֮һ������Ĺ����
 */
public void conjugate() {
	x *= -1.0d;
	y *= -1.0d;
	z *= -1.0d;
}

/**
 * Set this Quarternion to the complex conjugate of q
 * ������ķ�֮һ���q�ĸ�����
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
 * ��Ԫ���˷�����ѧ�ϵȼ��ڰ�˳��Ӧ��������ת��
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
	//������Ԫ���ĵ��ֵ
	double magSquared = magSquared();
	//�жϵ��ֵ�Ƿ��1.0�غ�
	return MathUtils.near(magSquared, 1.0);
}
/**
 * ���㴫�����Ԫ���������Ԫ���ĵ��ֵ
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
 * ��Ԫ��֮��ļ�Ȩ���Բ�ֵ����Ȩ=1ʱ�� res=q����Ȩ=0��ʱ��res=this
 * @param q - the other quaternion  �������Ԫ��
 * @param weight - the weight to blend with  Ȩ��
 * @param res - the result  �����Ԫ��
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
 * ��Ԫ��֮����������Բ�ֵ������㲻ȷ���������slerp
 * ��Ȩ=1��ʱ��res=q����Ȩ=0��ʱ��res=this
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
 * �ж�������Ԫ���Ƿ��غ�
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
