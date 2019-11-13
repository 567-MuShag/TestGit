/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2012 Ausenco Engineering Canada Inc.
 * Copyright (C) 2018 JaamSim Software Inc.
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

import com.jaamsim.input.ColourInput;

/**
 * A data structure to hold RGBA color information.
 * 一种用于保存RGBA颜色信息的数据结构。用四个参数表示颜色
 */
public class Color4d {

public double r;
public double g;
public double b;
public double a;

public Color4d() {
	r = 0.0d; g = 0.0d; b = 0.0d; a = 1.0d;
}

public Color4d(double r, double g, double b) {
	this.r = r; this.g = g; this.b = b; this.a = 1.0d;
}

public Color4d(double r, double g, double b, double a) {
	this.r = r; this.g = g; this.b = b; this.a = a;
}

public Color4d(Color4d col) {
	this.r = col.r; this.g = col.g; this.b = col.b; this.a = col.a;
}

public Color4d(int red, int green, int blue, int alpha) {
	r = red/255.0d; g = green/255.0d; b = blue/255.0d; a = alpha/255.0d;
}

public float[] toFloats() {
	float[] ret = new float[4];
	ret[0] = (float)r; ret[1] = (float)g; ret[2] = (float)b; ret[3] = (float)a;
	return ret;
}

/**
 * Tests the first four components are exactly equal.测试前四个组件完全相同。
 *
 * This returns true if the r,g,b,a components compare as equal using the ==
 * operator.  Note that NaN will always return false, and -0.0 and 0.0
 * will compare as equal.
 * 如果r、g、b、a组件使用==操作符进行相等比较，则返回true。请注意，NaN总是返回false，并且-0和0.0比较起来是相等的。
 * @throws NullPointerException if col is null
 */
public boolean equals4(Color4d c) {
	return r == c.r && g == c.g && b == c.b && a == c.a;
}

/**
 * Checks if all vector components are within EPSILON of each other
 * 检查是否所有的向量分量都在彼此的范围内
 * @param other - the other vector
 * @return
 */
@Override
public boolean equals(Object o)
{
	if (!(o instanceof Color4d))
		return false;

	Color4d c = (Color4d)o;

	return r == c.r && g == c.g && b == c.b && a == c.a;
}

@Override
public int hashCode() {
	return Double.valueOf(r).hashCode() +
	       Double.valueOf(g).hashCode() * 7 +
	       Double.valueOf(b).hashCode() * 79 +
	       Double.valueOf(a).hashCode() * 1239;
}

@Override
public String toString() {
	return ColourInput.toString(this);
}

}
