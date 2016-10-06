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


import me.semoro.kasm.contexts.ClassVisitorContext
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

fun classWriter(classWriter: ClassWriter, callable: ClassVisitorContext<ClassWriter>.() -> Unit): ClassWriter {
    val context = ClassVisitorContext(classWriter)
    context.callable()
    return classWriter
}

fun classWriter(flags: Int = 0, callable: ClassVisitorContext<ClassWriter>.() -> Unit): ClassWriter {
    val writer = ClassWriter(flags)
    classWriter(writer, callable)
    return writer
}

fun classWriter(classReader: ClassReader, flags: Int = 0, callable: ClassVisitorContext<ClassWriter>.() -> Unit): ClassWriter {
    val writer = ClassWriter(classReader, flags)
    classWriter(writer, callable)
    return writer
}


