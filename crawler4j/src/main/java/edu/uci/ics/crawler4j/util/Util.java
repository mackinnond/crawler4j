/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.util;

// TODO: Auto-generated Javadoc
/**
 * The Class Util.
 * 
 * @author Yasser Ganjisaffar <yganjisa at uci dot edu>
 */

public final class Util
{

	/**
	 * Long into byte array.
	 * 
	 * @param l
	 *            the l
	 * @param array
	 *            the array
	 * @param offset
	 *            the offset
	 */
	public static void longIntoByteArray(long l, byte[] array, int offset)
	{
		int i, shift;
		for (i = 0, shift = 56; i < 8; i++, shift -= 8)
			array[offset + i] = (byte) (0xFF & (l >> shift));
	}

	/**
	 * Long2 byte array.
	 * 
	 * @param l
	 *            the l
	 * @return the byte[]
	 */
	public static byte[] long2ByteArray(long l)
	{
		byte[] array = new byte[8];
		int i, shift;
		for (i = 0, shift = 56; i < 8; i++, shift -= 8)
		{
			array[i] = (byte) (0xFF & (l >> shift));
		}
		return array;
	}

	/**
	 * Byte array into long.
	 * 
	 * @param bytearray
	 *            the bytearray
	 * @return the long
	 */
	public static long byteArrayIntoLong(byte[] bytearray)
	{
		return byteArrayIntoLong(bytearray, 0);
	}

	/**
	 * Byte array into long.
	 * 
	 * @param bytearray
	 *            the bytearray
	 * @param offset
	 *            the offset
	 * @return the long
	 */
	public static long byteArrayIntoLong(byte[] bytearray, int offset)
	{
		long result = 0;
		for (int i = offset; i < 8 /* Bytes in long */; i++)
		{
			result = (result << 8 /* Bits in byte */) | (0xff & (byte) (bytearray[i] & 0xff));
		}
		return result;
	}

	/**
	 * Int2 byte array.
	 * 
	 * @param value
	 *            the value
	 * @return the byte[]
	 */
	public static byte[] int2ByteArray(int value)
	{
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++)
		{
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	/**
	 * Byte array2 int.
	 * 
	 * @param b
	 *            the b
	 * @return the int
	 */
	public static int byteArray2Int(byte[] b)
	{
		int value = 0;
		for (int i = 0; i < 4; i++)
		{
			int shift = (4 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

	/**
	 * Byte array2 long.
	 * 
	 * @param b
	 *            the b
	 * @return the long
	 */
	public static long byteArray2Long(byte[] b)
	{
		int value = 0;
		for (int i = 0; i < 8; i++)
		{
			int shift = (8 - 1 - i) * 8;
			value += (b[i] & 0x000000FF) << shift;
		}
		return value;
	}

}
