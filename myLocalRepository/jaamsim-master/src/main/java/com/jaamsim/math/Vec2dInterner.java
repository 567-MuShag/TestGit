/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Vec2dInterner is a container type used to 'intern' Vec2d instances hopefully saving space on repeating entries
 * Vec2dInterner是一种容器类型，用于“实习”Vec2d实例，希望在重复条目时节省空间
 * @author matt.chudleigh
 *
 */
public class Vec2dInterner {

	private int nextIndex = 0;
	private ArrayList<Vec2d> orderedValues = new ArrayList<>();

	private static class VecWrapper {
		public Vec2d val;
		public int index;
		public VecWrapper(Vec2d v) {
			val = v;
		}

		@Override
		public boolean equals(Object o) {
			VecWrapper vw = (VecWrapper)o;
			//调用Vec2d类中的equals2方法比较两个向量是否相同
			return val.equals2(vw.val);
		}

		@Override
		public int hashCode() {
			int hash = 0;
			hash ^= Double.valueOf(val.x).hashCode();
			hash ^= Double.valueOf(val.y).hashCode() * 3;
			return hash;
		}
	}

	private HashMap<VecWrapper, VecWrapper> map = new HashMap<>();

	/**
	 * intern will return a pointer to a Vec2d (which may differ from input 'v') that is mathematically equal but
	 * may be a shared object. Any value returned by intern should be defensively copied before being modified
	 * intern将返回一个指向Vec2d(可能与输入“v”不同)的指针，该指针在数学上是相等的，但可能是一个共享对象。实习生返回的任何值在修改之前都应该被防御性地复制
	 * @return
	 */
	public Vec2d intern(Vec2d v) {
		VecWrapper wrapped = new VecWrapper(v);
		//map集合中根据键获取值，不存在的键返回null
		VecWrapper interned = map.get(wrapped);
		if (interned != null) {
			return interned.val;
		}

		// This wrapped value will be stored这个包装好的值将会被存储
		wrapped.index = nextIndex++;
		orderedValues.add(v);
		map.put(wrapped, wrapped);
		return v;
	}

	public Vec2d getValueForIndex(int i) {
		return orderedValues.get(i);
	}

	public int getIndexForValue(Vec2d v) {
		VecWrapper wrapped = new VecWrapper(v);
		return map.get(wrapped).index;
	}

	public int getMaxIndex() {
		return orderedValues.size();
	}

}
