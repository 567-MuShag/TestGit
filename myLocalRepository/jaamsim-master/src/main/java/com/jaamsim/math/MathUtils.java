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

import java.util.List;

import com.jaamsim.render.RenderUtils;

/**
 * Some handy static methods to make life easier else where
 * @author Matt Chudleigh
 *
 */
public class MathUtils {
	//����һ����С����
	final static double EPSILON = 0.000000001; // one billionth


public static final boolean isSmall(double a) {
	//aС�ں�С������ʾ�غ�
	return a < EPSILON;
}

public static boolean near(double a, double b) {
	//ȡ������С�����Ĳ�ľ���ֵ������isSmall������������ʾ�Ƿ��غ�
	double diff = Math.abs(a - b);
	return isSmall(diff);
}

/**
 * Checks for line segment overlap
 * ����߶��ص�
 * @param a0
 * @param a1
 * @param b0
 * @param b1
 */
public static boolean segOverlap(double a0, double a1, double b0, double b1) {
	if (a0 == b0) return true;

	if (a0 < b0) {
		return b0 <= a1;
	}
	return a0 <= b1;
}

/**
 * Checks for line segment overlap, with a fudge factor
 * ����߶��ص�����һ��ģ������
 * @param a0
 * @param a1
 * @param b0
 * @param b1
 * @param fudge - The fudge factor to allow
 */
public static boolean segOverlap(double a0, double a1, double b0, double b1, double fudge) {
	if (a0 == b0) return true;

	if (a0 < b0) {
		return b0 <= a1 + fudge;
	}
	return a0 <= b1 + fudge;
}

/**
 * Perform a bounds check on val, returns something in the range [min, max]
 * ��valִ�б߽��飬���ط�Χ[min, max]
 * @param val
 * @param min
 * @param max
 * @return
 */
public static double bound(double val, double min, double max) {
	//assert(min <= max);

	if (val < min) { return min; }
	if (val > max) { return max; }
	return val;
}

/**
 * Return a matrix that rotates points and projects them onto the ray's view plane.
 * IE: the new coordinate system has the ray pointing in the +Z direction from the origin.
 * This is useful for ray-line collisions and ray-point collisions
 * ����һ�������������������ǽ�����ת�����µ�����ϵ��������
 * ����һ��������ת�㲢������Ͷ�䵽���ߵ���ͼƽ���ϡ�
 * Ҳ����˵���µ�����ϵ�����ߴ�ԭ��ָ��+Z����
 * ����ڹ�������ײ�͹��ߵ���ײ�ǳ�����
 * @return
 */
public static Mat4d RaySpace(Ray r) {

	// Create a new orthonormal basis starting with the y-axis, if the ray is
	// nearly parallel to Y, build our new basis from X instead
	//����һ���µı�׼��������Y�Ὺʼ��������߼���ƽ����Y����ô��X��ʼ�����µĻ�
	Vec3d t = new Vec3d(0.0d, 1.0d, 0.0d);
	double dist = Math.abs(t.dot3(r.getDirRef()));
	if (MathUtils.near(dist, 1.0d))
		t.set3(1.0d, 0.0d, 0.0d);

	Mat4d ret = new Mat4d();

	// Calculate a new basis to populate the rows of the return matrix  ����һ���µĻ�����䷵�ؾ������
	t.cross3(r.getDirRef(), t);
	t.normalize3();
	ret.d00 = t.x; ret.d01 = t.y; ret.d02 = t.z;

	t.cross3(r.getDirRef(), t);
	t.normalize3();
	ret.d10 = t.x; ret.d11 = t.y; ret.d12 = t.z;

	t.set3(r.getDirRef());
	ret.d20 = t.x; ret.d21 = t.y; ret.d22 = t.z;

	// Now use this rotation matrix to calculate the rotated translation part   �����������ת������������תƽ�Ʋ���
	t.mult3(ret, r.getStartRef());
	ret.d03 = -t.x; ret.d13 = -t.y; ret.d23 = -t.z;

	return ret;
}

/**
 * Returns a Transform representing a rotation around a non-origin point
 * ����һ����ʾ�Ʒ�ԭ����ת�ı任
 * @param rot - the rotation (in world coordinates) to apply   ҪӦ�õ���ת(����������Ϊ��λ)
 * @param point - the point to rotate around   ��ת�ĵ�
 * @return
 */
public static Mat4d rotateAroundPoint(Quaternion rot, Vec3d point) {
	Vec3d negPoint = new Vec3d(point);
	negPoint.scale3(-1);

	Transform ret = new Transform(point, rot, 1);
	ret.merge(ret, new Transform(negPoint));

	return ret.getMat4dRef();
}

public static double collisionDistPoly(Ray r, Vec3d[] points) {
	if (points.length < 3) {
		return -1; // Should this be an error?
	}
	// Check that this is actually inside the polygon, this assumes the points are co-planar
	//���һ�����ǲ����ڶ�����ڣ�������Щ���ǹ���
	Plane p = new Plane(points[0], points[1], points[2]);
	double dist = p.collisionDist(r);

	if (dist < 0) { return dist; } // Behind the start of the ray  �ڹ��߿�ʼ�ĵط�

	// This is the potential collision point, if it's inside the polygon
	//����Ǳ�ڵ���ײ�㣬������ڶ������
	Vec3d collisionPoint = r.getPointAtDist(dist);

	Vec3d a = new Vec3d();
	Vec3d b = new Vec3d();
	Vec3d cross = new Vec3d();
	boolean firstPos = false;

	for (int i = 0; i < points.length; ++i) {
		// Check that the collision point is on the same winding side of all the
		//�����ײ���Ƿ������������ͬһ�࣬�����ͬһ�࣬��˵����ײ��������
		Vec3d p0 = points[i];
		Vec3d p1 = points[(i + 1) % points.length];
		a.sub3(p0, collisionPoint);
		b.sub3(p1, p0);
		cross.cross3(a, b);

		double triple = cross.dot3(r.getDirRef());
		// This point is inside the polygon if all triple products have the same sign
		//������е����ػ�������ͬ�ķ��ţ���ô������ڶ������
		if (i == 0) {
			// First iteration sets the sign   ��һ�ε������÷���
			firstPos = triple > 0;
		}

		if (firstPos != (triple > 0)) {
			return -1;
		}
	}
	return dist; // This must be valid then���������Ч��

}

public static double collisionDistPoly(Ray r, List<Vec3d> points) {
	if (points.size() < 3) {
		return -1; // Should this be an error?
	}
	// Check that this is actually inside the polygon, this assumes the points are co-planar
	//���һ�����ǲ����ڶ�����ڣ�������Щ���ǹ���
	Plane p = new Plane();
	p.set(points.get(0), points.get(1), points.get(2));
	double dist = p.collisionDist(r);

	if (dist < 0) { return dist; } // Behind the start of the ray   �ڹ��߿�ʼ�ĵط�

	// This is the potential collision point, if it's inside the polygon
	//����Ǳ�ڵ���ײ�㣬������ڶ������
	Vec3d collisionPoint = r.getPointAtDist(dist);

	Vec3d a = new Vec3d();
	Vec3d b = new Vec3d();
	Vec3d cross = new Vec3d();
	boolean firstPos = false;

	for (int i = 0; i < points.size(); ++i) {
		// Check that the collision point is on the same winding side of all the
		//�����ײ���Ƿ������������ͬһ��
		Vec3d p0 = points.get(i);
		Vec3d p1 = points.get((i + 1) % points.size());
		a.sub3(p0, collisionPoint);
		b.sub3(p1, p0);
		cross.cross3(a, b);

		double triple = cross.dot3(r.getDirRef());
		// This point is inside the polygon if all triple products have the same sign
		//������е�ֵ������ͬ�ķ��ţ���ô������ڶ������
		if (i == 0) {
			// First iteration sets the sign��һ�ε������÷���
			firstPos = triple > 0;
		}

		if (firstPos != (triple > 0)) {
			return -1;
		}
	}
	return dist; // This must be valid then

}

/**
 * Determine line collision  ȷ������ײ
 * @param rayMat - the rayspace matrix   rayspace����
 * @param lines - pairs of vertices, each pair defining a line segment (this is not a line strip or line loop)  ����ԣ�ÿ�Զ���һ���߶�(�ⲻ��һ���ߴ�����ѭ��)
 * @param collisionAngle - the angle of the collision cone in radians   �Ի��ȱ�ʾ����ײ׶�ĽǶ�
 * @return
 */
public static double collisionDistLines(Mat4d rayMat, Vec4d[] lines, double collisionAngle) {
	double shortDist = Double.POSITIVE_INFINITY;

	for (int i = 0; i < lines.length; i+=2) {
		Vec4d nearPoint = RenderUtils.rayClosePoint(rayMat, lines[i], lines[i+1]);

		double angle = RenderUtils.angleToRay(rayMat, nearPoint);
		if (angle < 0) {
			continue;
		}

		Vec4d raySpaceNear = new Vec4d(0.0d, 0.0d, 0.0d, 1.0d);
		raySpaceNear.mult4(rayMat, nearPoint);

		if (angle < collisionAngle && raySpaceNear.z < shortDist) {
			shortDist = raySpaceNear.z;
		}
	}

	// Short dist is the shortest collision distance   ����Խ�̣���ײ����Խ��
	if (shortDist == Double.POSITIVE_INFINITY) {
		return -1; // No collision  û����ײ
	}
	return shortDist;

}

/**
 * Get the point where 3 planes intesect, or null if any are parallel
 * �õ�����ƽ���ཻ�ĵ㣬�����ƽ�еģ���Ϊ��
 * @param p0
 * @param p1
 * @param p2
 * @return
 */
public static Vec3d collidePlanes(Plane p0, Plane p1, Plane p2) {
	Ray r = p0.collide(p1);
	if (r == null)
		return null;

		double dist = p2.collisionDist(r);
	if (Double.isInfinite(dist))
		return null;

	Vec3d ret = new Vec3d(r.getDirRef());
	ret.scale3(dist);
	ret.add3(r.getStartRef());
	return ret;
}

public static Plane getMidpointPlane(Vec3d p0, Vec3d p1) {
	Vec3d mid = new Vec3d(p0);
	mid.add3(p1);
	mid.scale3(0.5);

	Vec4d normal =  new Vec4d(p1, 0);
	normal.sub3(p0);
	normal.normalize3();

	double dist = normal.dot3(mid);

	return new Plane(normal, dist);
}

} // class
