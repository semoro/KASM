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


import org.junit.Test
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type


class SimpleTest : AbstractASMTest() {

    @Test
    fun testClassSimpleVisiting() {
        val cw = classWriter {
            visitClass(ACC_PUBLIC, "Test") {
                visitSource("Test.java")
            }
        }
        assertBytecodeEquals(cw, "testData/simpleClass/Class.dump")
    }

    @Test
    fun testClassSimpleAnnotationVisiting() {
        val cw = classWriter {
            visitClass(ACC_PUBLIC, "Test") {
                visitSource("Test.java")
                visitAnnotation("LSomeAnnotation;") {

                }
            }
        }
        assertBytecodeEquals(cw, "testData/simpleClass/Annotation.dump")
    }

    @Test
    fun testClassWithFieldVisiting() {
        val cw = classWriter {
            visitClass(ACC_PUBLIC, "Test") {
                visitSource("Test.java")
                visitField(ACC_PUBLIC + ACC_STATIC, "test", Type.DOUBLE_TYPE, value = 0.0) {

                }
            }
        }
        assertBytecodeEquals(cw, "testData/simpleClass/StaticField.dump")
    }

    @Test
    fun testMethods() {
        val cw = classWriter(flags = ClassWriter.COMPUTE_MAXS + ClassWriter.COMPUTE_FRAMES) {
            visitClass(ACC_PUBLIC, "Test") {
                visitSource("Test.java")
                visitMethod(ACC_PUBLIC, "test", Type.getMethodType(Type.VOID_TYPE)) {
                    visitor.visitInsn(RETURN)
                }

                visitMethod(ACC_PUBLIC, "test2", Type.getMethodType(Type.VOID_TYPE, Type.BOOLEAN_TYPE)) {
                    visitParameter(0, "var1")
                    visitCode {

                    }
                    visitor.visitInsn(RETURN)
                }
            }
        }
        assertBytecodeEquals(cw, "testData/simpleClass/Method.dump")
    }
}