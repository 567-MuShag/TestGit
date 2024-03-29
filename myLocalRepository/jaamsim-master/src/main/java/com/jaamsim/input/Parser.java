/*
 * JaamSim Discrete Event Simulation
 * Copyright (C) 2013 Ausenco Engineering Canada Inc.
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
package com.jaamsim.input;

import java.util.ArrayList;

public class Parser {

/**
 * Tokenize the given record and append to the given list of tokens
 * 标记给定的记录并将其附加到给定的标记列表中
 *
 * Valid delimiter characters are space, tab and comma.
 * 有效的分隔符是空格、制表符和逗号。
 *
 * @param tokens list of String tokens to append to要附加到的字符串令牌列表
 * @param rec record to tokenize and append 要标记和附加的rec记录
 * @param stripComments if true, do not append any commented tokens 如果为真，不要附加任何注释的标记
 */
public static final void tokenize(ArrayList<String> tokens, String rec, boolean stripComments) {
	// Records can be divided into two pieces, the contents portion and possibly 
	// a commented portion, the division point is the first " character, if no
	// quoting in a record, the entire line is contents for tokenizing
	//记录可以分为两部分，内容部分和可能的注释部分，分隔点是第一个“字符，如果没有引用的记录，整个行是内容的记号
	int tokStart = -1;
	int quoteStart = -1;
	int cIndex = -1;
	int endOfRec = rec.length();
	for (int i = 0; i < rec.length(); i++) {
		char c = rec.charAt(i);
		if (c == '\'') {
			// end the current token  结束当前token
			if (tokStart != -1) {
				if (i - tokStart > 0) tokens.add(rec.substring(tokStart, i));
				tokStart = -1;
			}

			// Set the quoting state
			if (quoteStart != -1) {
				tokens.add(rec.substring(quoteStart + 1, i));
				quoteStart = -1;
			}
			else {
				quoteStart = i;
			}
			continue;
		}

		// we are currently quoted, skip
		if (quoteStart > -1)
			continue;

		// handle delimiter chars  /t横向制表
		if (c == '{' || c == '}' || c == ' ' || c == '\t') {
			if (tokStart != -1 && i - tokStart > 0) {
				tokens.add(rec.substring(tokStart, i));
				tokStart = -1;
			}

			if (c == '{')
				tokens.add("{");

			if (c == '}')
				tokens.add("}");

			continue;
		}

		// start a comment
		if (c == '#') {
			cIndex = i;
			endOfRec = i;
			break;
		}
		// start a new token
		if (tokStart == -1) tokStart = i;
	}

	// clean up the final trailing token
	if (tokStart != -1)
		tokens.add(rec.substring(tokStart, endOfRec));

	if (quoteStart != -1)
		tokens.add(rec.substring(quoteStart + 1, endOfRec));

	// add comments if they exist including the leading # to denote it as commented
	if (!stripComments && cIndex > -1)
		tokens.add(rec.substring(cIndex, rec.length()));
}

public static final boolean needsQuoting(CharSequence s) {
	for (int i = 0; i < s.length(); ++i) {
		char c = s.charAt(i);
		if (c == ' ' || c == '\t' || c == '{' || c == '}' || c == '"' || c == '#')
			return true;
	}
	return false;
}

public static final boolean isQuoted(CharSequence s) {
	if (s.length() < 2) return false;
	if (s.charAt(0) != '\'') return false;
	if (s.charAt(s.length() - 1) != '\'') return false;

	return true;
}

public static final String addQuotes(String str) {
	return addEnclosure("'", str, "'");
}

public static final String addQuotesIfNeeded(String str) {
	if (needsQuoting(str) && !isQuoted(str))
		return addQuotes(str);
	return str;
}

public static final String addEnclosure(String prefix, String str, String suffix) {
	StringBuilder sb = new StringBuilder();
	if (!str.startsWith(prefix))
		sb.append(prefix);
	sb.append(str);
	if (!str.endsWith(suffix))
		sb.append(suffix);
	return sb.toString();
}

public static final String removeEnclosure(String prefix, String str, String suffix) {
	if (!str.startsWith(prefix) && !str.endsWith(suffix))
		return str;
	int beginIndex = 0;
	int endIndex = str.length();
	if (str.startsWith(prefix))
		beginIndex = prefix.length();
	if (str.endsWith(suffix))
		endIndex -= suffix.length();
	return str.substring(beginIndex, endIndex);
}

}
