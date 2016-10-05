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


import org.objectweb.asm.*


class ClassVisitorContext<out TVisitor : ClassVisitor>(val visitor: TVisitor) {

    val annotationVisiting by lazy { AnnotationVisitingContext() }
    val classVisiting by lazy { ClassVisitingContext() }
    val fieldVisiting by lazy { FieldVisitingContext() }


    interface AnnotationVisitingProvider<out TVisitor : ClassVisitor> {
        fun visitAnnotation(desc: String,
                            visible: Boolean = true,
                            callable: ClassVisitorContext<TVisitor>.AnnotationVisitingContext.() -> Unit): AnnotationVisitor?

        fun visitAnnotation(type: Type,
                            visible: Boolean = true,
                            callable: ClassVisitorContext<TVisitor>.AnnotationVisitingContext.() -> Unit): AnnotationVisitor?
                = visitAnnotation(type.descriptor, visible, callable)
    }

    interface FieldVisitingProvider<out TVisitor : ClassVisitor> {
        fun visitField(access: Int,
                       name: String,
                       desc: String,
                       signature: String? = null,
                       value: Any? = null,
                       callable: ClassVisitorContext<TVisitor>.FieldVisitingContext.() -> Unit): FieldVisitor?

        fun visitField(access: Int,
                       name: String,
                       type: Type,
                       signature: String? = null,
                       value: Any? = null,
                       callable: ClassVisitorContext<TVisitor>.FieldVisitingContext.() -> Unit): FieldVisitor?
                = visitField(access, name, type.descriptor, signature, value, callable)
    }

    inner class AnnotationVisitingContext() {
        lateinit var visitor: AnnotationVisitor
    }

    inner class FieldVisitingContext() : AnnotationVisitingProvider<TVisitor> {
        override fun visitAnnotation(desc: String, visible: Boolean, callable: AnnotationVisitingContext.() -> Unit)
                = visitor.visitAnnotation(desc, visible)?.
                apply {
                    annotationVisiting.visitor = this
                    annotationVisiting.callable()
                    visitEnd()
                }

        lateinit var visitor: FieldVisitor
    }


    inner class ClassVisitingContext() : AnnotationVisitingProvider<TVisitor>, FieldVisitingProvider<TVisitor> {

        fun visitSource(name: String, debug: String? = null) {
            visitor.visitSource(name, debug)
        }

        override fun visitAnnotation(desc: String, visible: Boolean, callable: AnnotationVisitingContext.() -> Unit)
                = visitor.visitAnnotation(desc, visible)?.
                apply {
                    annotationVisiting.visitor = this
                    annotationVisiting.callable()
                    visitEnd()
                }


        override fun visitField(access: Int, name: String, desc: String, signature: String?, value: Any?, callable: FieldVisitingContext.() -> Unit)
                = visitor.visitField(access, name, desc, signature, value)?.
                apply {
                    fieldVisiting.visitor = this
                    fieldVisiting.callable()
                    visitEnd()
                }

    }


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


