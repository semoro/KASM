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


import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter


class ClassVisitorContext<out TVisitor : ClassVisitor>(val visitor: TVisitor) {

    class AnnotationVisitingContext() {
        lateinit var visitor: AnnotationVisitor
    }

    inner class ClassVisitingContext() {

        fun visitSource(name: String, debug: String? = null) {
            visitor.visitSource(name, debug)
        }

        fun visitAnnotation(desc: String, visible: Boolean = true,
                            callable: AnnotationVisitingContext.() -> Unit): AnnotationVisitor? {
            val annotationVisitor = visitor.visitAnnotation(desc, visible)
            annotationVisitor?.let {
                annotationVisiting.visitor = it
                annotationVisiting.callable()
                it.visitEnd()
            }
            return annotationVisitor
        }
    }

    val annotationVisiting by lazy { AnnotationVisitingContext() }
    val classVisiting by lazy { ClassVisitingContext() }
    fun visitClass(access: Int,
                   name: String,
                   superClass: String? = "java/lang/Object",
                   interfaces: Array<String>? = null,
                   signature: String? = null,
                   version: Int = defaultClassVersion,
                   contextCallable: ClassVisitingContext.() -> Unit) {
        visitor.visit(version, access, name, signature, superClass, interfaces)
        classVisiting.contextCallable()
        visitor.visitEnd()
    }

    companion object {
        var defaultClassVersion = 52
    }
}

typealias ClassVisitorContextCallable<T> = ClassVisitorContext<T>.() -> Unit



fun classWriter(classWriter: ClassWriter, callable: ClassVisitorContextCallable<ClassWriter>): ClassWriter {
    val context = ClassVisitorContext(classWriter)
    context.callable()
    return classWriter
}

fun classWriter(flags: Int = 0, callable: ClassVisitorContextCallable<ClassWriter>): ClassWriter {
    val writer = ClassWriter(flags)
    classWriter(writer, callable)
    return writer
}

fun classWriter(classReader: ClassReader, flags: Int = 0, callable: ClassVisitorContextCallable<ClassWriter>): ClassWriter {
    val writer = ClassWriter(classReader, flags)
    classWriter(writer, callable)
    return writer
}


