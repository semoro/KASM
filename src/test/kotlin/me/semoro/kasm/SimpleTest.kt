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
import org.objectweb.asm.Opcodes.ACC_PUBLIC


class SimpleTest : AbstractASMTest() {

    @Test
    fun classVisiting() {
        val cw = classWriter {
            visitClass(ACC_PUBLIC, "Test") {
                visitSource("Test.java")
            }
        }

        assertBytecodeEquals(cw, "testData/simpleClass/ClassVisiting.dump")
    }
}