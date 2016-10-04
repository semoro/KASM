/*
 * Copyright 2016 Semoro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.semoro.kasm

import org.junit.ComparisonFailure
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.util.CheckClassAdapter
import org.objectweb.asm.util.TraceClassVisitor
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintWriter

abstract class AbstractASMTest {

    private fun readTestData(path: String): String {
        val file = File(path)
        return file.bufferedReader().readText()
    }

    fun assertBytecodeEquals(classWriter: ClassWriter, representationLocation: String) {
        val cc = CheckClassAdapter(classWriter)
        val baos = ByteArrayOutputStream()
        val tv = TraceClassVisitor(cc, PrintWriter(baos))

        val cr = ClassReader(classWriter.toByteArray())
        cr.accept(tv, 0)
        baos.flush()

        val expected = readTestData(representationLocation)
        val actual = String(baos.toByteArray())

        if (expected != actual) {
            throw BytecodeStringNotEqualException("Bytecode different from $representationLocation", expected, actual)
        }
    }

    class BytecodeStringNotEqualException(message: String, expected: String, actual: String) : ComparisonFailure(message, expected, actual) {

    }
}