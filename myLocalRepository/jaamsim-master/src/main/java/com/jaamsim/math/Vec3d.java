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
 * Construct a Vec3d initialized to (0,0,0);�����ʼ��Ϊ(0,0,0)��Vec3d;
 */
public Vec3d() {
	x = 0.0d;
	y = 0.0d;
	z = 0.0d;
}

/**
 * Construct a Vec3d initialized to (v.x, v.y, v.z);
 * ����һ����ʼ��Ϊ(v��x, v��y, v.z); v������ʼֵ
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
 * ����һ����ʼ����Vec3d,��ʼֵΪ�����ֵ
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
 * Returns a string representation of this vec.���ش�vec���ַ�����ʾ��ʽ��
 */
@Override
public String toString() {
	StringBuilder tmp = new StringBuilder();
	tmp.append(x);
	//  ������Input.SEPARATOR������
	tmp.append(Input.SEPARATOR).append(y);
	tmp.append(Input.SEPARATOR).append(z);
	return tmp.toString();
}

/**
 * Tests the first three components are exactly equal.����ǰ���������ȫ��ͬ��
 *
 * This returns true if the x,y,z components compare as equal using the ==
 * operator.  Note that NaN will always return false, and -0.0 and 0.0
 * will compare as equal.
 * ���x��y��z����ʹ��==������������ȱȽϣ��򷵻�true��ע��NaN���Ƿ���false�� -0��0��0�Ƚ���������ȵġ�
 * @throws NullPointerException if v is null
 */
public boolean equals3(Vec3d v) {
	return x == v.x && y == v.y && z == v.z;
}
//�ж�������ά�����غ�
public boolean near3(Vec3d v) {
	return MathUtils.near(x, v.x) &&
	       MathUtils.near(y, v.y) &&
	       MathUtils.near(z, v.z);
}

/**
 * Set this Vec3d with the values (v.x, v.y, v.z); ��ά������set����������������
 * @param v the Vec3d containing the values
 * @throws NullPointerException if v is null
 */
public void set3(Vec3d v) {
	this.x = v.x;
	this.y = v.y;
	this.z = v.z;
}

/**
 * Set this Vec3d with the values (x, y, z); ��ά������set����������xyz������
 */
public void set3(double x, double y, double z) {
	this.x = x;
	this.y = y;
	this.z = z;
}

/**
 * Add v to this Vec3d: this = this + v  ������������ϴ��������
 * @throws NullPointerException if v is null
 */
public void add3(Vec3d v) {
	this.x = this.x + v.x;
	this.y = this.y + v.y;
	this.z = this.z + v.z;
}

/**
 * Add v1 to v2 into this Vec3d: this = v1 + v2  �������������ά������Ӹ����������
 * @throws NullPointerException if v1 or v2 are null
 */
public void add3(Vec3d v1, Vec3d v2) {
	this.x = v1.x + v2.x;
	this.y = v1.y + v2.y;
	this.z = v1.z + v2.z;
}

/**
 * Subtract v from this Vec3d: this = this - v  �����������ȥ���������
 * @throws NullPointerException if v is null
 */
public void sub3(Vec3d v) {
	this.x = this.x - v.x;
	this.y = this.y - v.y;
	this.z = this.z - v.z;
}

/**
 * Subtract v2 from v1 into this Vec3d: this = v1 - v2  �����������������������������
 * ��v1��ȥv2���������3d:�������v1 - v2
 * @throws NullPointerException if v1 or v2 are null
 */
public void sub3(Vec3d v1, Vec3d v2) {
	this.x = v1.x - v2.x;
	this.y = v1.y - v2.y;
	this.z = v1.z - v2.z;
}

/**
 * Multiply the elements of this Vec3d by v: this = this * v  �����������xyz�ֱ���ϴ����������xyz���������������
 * @throws NullPointerException if v is null
 */
public void mul3(Vec3d v) {
	this.x = this.x * v.x;
	this.y = this.y * v.y;
	this.z = this.z * v.z;
}

/**
 * Multiply the elements of v1 and v2 into this Vec3d: this = v1 * v2  ����������������xyz�ֱ���ˣ��������������
 * @throws NullPointerException if v1 or v2 are null
 */
public void mul3(Vec3d v1, Vec3d v2) {
	this.x = v1.x * v2.x;
	this.y = v1.y * v2.y;
	this.z = v1.z * v2.z;
}

/**
 * Set this Vec3d to the minimum of this and v: this = min(this, v)  �Ƚ���������ʹ���������xyz���ֱ�ȡxyz����Сֵ�����������
 * @throws NullPointerException if v is null
 */
public void min3(Vec3d v) {
	this.x = Math.min(this.x, v.x);
	this.y = Math.min(this.y, v.y);
	this.z = Math.min(this.z, v.z);
}

/**
 * Set this Vec3d to the minimum of v1 and v2: this = min(v1, v2)  �Ƚϴ��������������xyz��ȡxyz����Сֵ�����������
 * @throws NullPointerException if v is null
 */
public void min3(Vec3d v1, Vec3d v2) {
	this.x = Math.min(v1.x, v2.x);
	this.y = Math.min(v1.y, v2.y);
	this.z = Math.min(v1.z, v2.z);
}

/**
 * Set this Vec3d to the maximum of this and v: this = max(this, v)     �Ƚ���������ʹ���������xyz���ֱ�ȡxyz�����ֵ�����������
 * @throws NullPointerException if v is null
 */
public void max3(Vec3d v) {
	this.x = Math.max(this.x, v.x);
	this.y = Math.max(this.y, v.y);
	this.z = Math.max(this.z, v.z);
}

/**
 * Set this Vec3d to the maximum of v1 and v2: this = max(v1, v2)  �Ƚϴ��������������xyz��ȡxyz�����ֵ�����������
 * @throws NullPointerException if v is null
 */
public void max3(Vec3d v1, Vec3d v2) {
	this.x = Math.max(v1.x, v2.x);
	this.y = Math.max(v1.y, v2.y);
	this.z = Math.max(v1.z, v2.z);
}

/**
 * Return the 3-component dot product of v1 and v2  ���㴫�����ά����v1��v2�ĵ��
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
 * Return the 3-component dot product of this Vec3d with v  ������������ʹ�������v�ĵ��
 * @throws NullPointerException if v is null
 */
public double dot3(Vec3d v) {
	return _dot3(this, v);
}

/**
 * Return the 3-component magnitude of this Vec3d   �������������ģ
 */
public double mag3() {
	return Math.sqrt(_dot3(this, this));
}

/**
 * Return the 3-component magnitude squared of this Vec3d  ���������������������ĵ��
 */
public double magSquare3() {
	return _dot3(this, this);
}
/**
 * �Դ�����������б�׼���ķ���
 * @param v
 */
private void _norm3(Vec3d v) {
	double mag = _dot3(v, v);
	//���������Vec2d���е�nonNormalMag�������÷��������ж�ģmag�ܷ����ڱ�׼������������true��ʾ�����ø�ֵ���б�׼��
	if (nonNormalMag(mag)) {
		//mag�������ڱ�׼�����Զ���һ����׼��׼������
		this.x = 0.0d;
		this.y = 0.0d;
		this.z = 1.0d;
		return;
	}
	//������׼������
	mag = Math.sqrt(mag);
	this.x = v.x / mag;
	this.y = v.y / mag;
	this.z = v.z / mag;
}

/**
 * Normalize the first three components in-place ��ǰ���������׼��
 *
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * ���Vec�Ĵ�СΪ�㣬���߰���NaN��Inf���⽫�����еķ�������Ϊ�㣬���һ��������Ϊ1�������Զ������һ����׼������
 */
public void normalize3() {
	_norm3(this);
}

/**
 * Set the first three components to the normalized values of v   ��ǰ�����������Ϊv�Ĺ��ֵ
 *
 * If the Vec has a zero magnitude or contains NaN or Inf, this sets
 * all components but the last to zero, the last component is set to one.
 * ���Vec�Ĵ�СΪ�㣬���߰���NaN��Inf���⽫�����еķ�������Ϊ�㣬���һ��������Ϊ1�������Զ������һ����׼������
 * @throws NullPointerException if v is null
 */
public void normalize3(Vec3d v) {
	_norm3(v);
}

/**
 * Scale the first three components of this Vec: this = scale * this
 * ����������������ţ�������scale
 */
public void scale3(double scale) {
	this.x = this.x * scale;
	this.y = this.y * scale;
	this.z = this.z * scale;
}

/**
 * Scale the first three components of v into this Vec: this = scale * v
 * �Դ���������������ţ�������scale
 * @throws NullPointerException if v is null
 */
public void scale3(double scale, Vec3d v) {
	this.x = v.x * scale;
	this.y = v.y * scale;
	this.z = v.z * scale;
}

/**
 * Linearly interpolate between a, b into this Vec: this = (1 - ratio) * a + ratio * b
 * �Դ��������a������b   ����(1 - ratio) * a + ratio * b���Ա仯
 * @throws NullPointerException if a or b are null
 */
public void interpolate3(Vec3d a, Vec3d b, double ratio) {
	double temp = 1.0d - ratio;
	this.x = temp * a.x + ratio * b.x;
	this.y = temp * a.y + ratio * b.y;
	this.z = temp * a.z + ratio * b.z;
}

/**
 * Multiply v by m and store into this Vec: this = m x v  ����m������v  ����vΪ������
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
 * Like mult3 but includes an implicit w = 1 term to include the translation part of the matrix  ����m������v  ����vΪ������
 * ��mult3���ƣ����ǰ�����һ��������w = 1�������������ƽ�Ʋ���
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
 * Multiply m by v and store into this Vec: this = v x m     ����v�˾���m  ����vΪ������
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
 * ��������ʹ�����������в�ˣ�����������������
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
 * ���������v1��v2���в�ˣ�����������������
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
