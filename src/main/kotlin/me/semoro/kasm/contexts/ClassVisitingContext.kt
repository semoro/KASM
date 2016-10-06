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

package me.semoro.kasm.contexts


import org.objectweb.asm.Attribute
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Type

class ClassVisitingContext<out TVisitor : ClassVisitor>(val visitor: TVisitor,
                                                        annotationVisitingContextLazy: Lazy<AnnotationVisitingContext>,
                                                        fieldVisitingContextLazy: Lazy<FieldVisitingContext>,
                                                        methodVisitingContextLazy: Lazy<MethodVisitingContext>) {

    val annotationVisitingContext  by annotationVisitingContextLazy
    val fieldVisitingContext  by fieldVisitingContextLazy
    val methodVisitingContext by methodVisitingContextLazy

    fun visitSource(name: String, debug: String? = null) {
        visitor.visitSource(name, debug)
    }

    fun visitAttribute(attr: Attribute) {
        visitor.visitAttribute(attr)
    }

    inline fun visitAttribute(attributeBuilder: () -> Attribute) = visitAttribute(attributeBuilder())

    inline fun visitAnnotation(desc: String, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitor.visitAnnotation(desc, visible)?.
            apply {
                annotationVisitingContext.visitor = this
                annotationVisitingContext.callable()
                visitEnd()
            }

    inline fun visitAnnotation(type: Type, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitAnnotation(type.descriptor, visible, callable)


    inline fun visitField(access: Int,
                          name: String,
                          desc: String,
                          signature: String? = null,
                          value: Any? = null,
                          callable: FieldVisitingContext.() -> Unit)
            = visitor.visitField(access, name, desc, signature, value)?.
            apply {
                fieldVisitingContext.visitor = this
                fieldVisitingContext.callable()
                visitEnd()
            }

    inline fun visitField(access: Int,
                          name: String,
                          type: Type,
                          signature: String? = null,
                          value: Any? = null,
                          callable: FieldVisitingContext.() -> Unit)
            = visitField(access, name, type.descriptor, signature, value, callable)


    inline fun visitMethod(
            access: Int,
            name: String,
            desc: String,
            signature: String? = null,
            exceptions: Array<String>? = null,
            callable: MethodVisitingContext.() -> Unit)
            = visitor.visitMethod(access, name, desc, signature, exceptions)?.
            apply {
                methodVisitingContext.visitor = this
                methodVisitingContext.callable()
                visitEnd()
            }

    inline fun visitMethod(
            access: Int,
            name: String,
            type: Type,
            signature: String? = null,
            exceptions: Array<String>? = null,
            callable: MethodVisitingContext.() -> Unit)
            = visitMethod(access, name, type.descriptor, signature, exceptions, callable)
}