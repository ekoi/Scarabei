/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.jfixby.cmns.adopted.gdx.json;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.badlogic.gdx.utils.Array;

/**
 * Builder style API for emitting JSON.
 * 
 * @author Nathan Sweet
 */
public class JsonWriter extends Writer {
    final Writer writer;
    private final Array<JsonObject> stack = new Array();
    private JsonObject current;
    private boolean named;
    private OutputType outputType = OutputType.json;
    private boolean quoteLongValues = false;

    public JsonWriter(Writer writer) {
	this.writer = writer;
    }

    public Writer getWriter() {
	return writer;
    }

    /** Sets the type of JSON output. Default is {@link OutputType#minimal}. */
    public void setOutputType(OutputType outputType) {
	this.outputType = outputType;
    }

    /**
     * When true, quotes long, double, BigInteger, BigDecimal types to prevent
     * truncation in languages like JavaScript and PHP. This is not necessary
     * when using libgdx, which handles these types without truncation. Default
     * is false.
     */
    public void setQuoteLongValues(boolean quoteLongValues) {
	this.quoteLongValues = quoteLongValues;
    }

    public JsonWriter name(String name) throws IOException {
	if (current == null || current.array)
	    throw new IllegalStateException("Current item must be an object.");
	if (!current.needsComma)
	    current.needsComma = true;
	else
	    writer.write(',');
	writer.write(outputType.quoteName(name));
	writer.write(':');
	named = true;
	return this;
    }

    public JsonWriter object() throws IOException {
	requireCommaOrName();
	stack.add(current = new JsonObject(this, false));
	return this;
    }

    public JsonWriter array() throws IOException {
	requireCommaOrName();
	stack.add(current = new JsonObject(this, true));
	return this;
    }

    public JsonWriter value(Object value) throws IOException {
	if (quoteLongValues && (value instanceof Long || value instanceof Double || value instanceof BigDecimal
		|| value instanceof BigInteger)) {
	    value = value.toString();
	} else if (value instanceof Number) {
	    Number number = (Number) value;
	    long longValue = number.longValue();
	    if (number.doubleValue() == longValue)
		value = longValue;
	}
	requireCommaOrName();
	writer.write(outputType.quoteValue(value));
	return this;
    }

    /** Writes the specified JSON value, without quoting or escaping. */
    public JsonWriter json(String json) throws IOException {
	requireCommaOrName();
	writer.write(json);
	return this;
    }

    private void requireCommaOrName() throws IOException {
	if (current == null)
	    return;
	if (current.array) {
	    if (!current.needsComma)
		current.needsComma = true;
	    else
		writer.write(',');
	} else {
	    if (!named)
		throw new IllegalStateException("Name must be set.");
	    named = false;
	}
    }

    public JsonWriter object(String name) throws IOException {
	return name(name).object();
    }

    public JsonWriter array(String name) throws IOException {
	return name(name).array();
    }

    public JsonWriter set(String name, Object value) throws IOException {
	return name(name).value(value);
    }

    /** Writes the specified JSON value, without quoting or escaping. */
    public JsonWriter json(String name, String json) throws IOException {
	return name(name).json(json);
    }

    public JsonWriter pop() throws IOException {
	if (named)
	    throw new IllegalStateException("Expected an object, array, or value since a name was set.");
	stack.pop().close();
	current = stack.size == 0 ? null : stack.peek();
	return this;
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
	writer.write(cbuf, off, len);
    }

    public void flush() throws IOException {
	writer.flush();
    }

    public void close() throws IOException {
	while (stack.size > 0)
	    pop();
	writer.close();
    }
}
