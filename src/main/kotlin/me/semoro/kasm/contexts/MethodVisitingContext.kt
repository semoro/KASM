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

import org.objectweb.asm.*

class MethodVisitingContext(annotationVisitingLazy: Lazy<AnnotationVisitingContext>, methodCodeVisitingContextLazy: Lazy<MethodCodeVisitingContext>) {
    val annotationVisiting by annotationVisitingLazy
    val codeVisiting by methodCodeVisitingContextLazy

    inline fun AnnotationVisitor?.visitWithCallable(callable: AnnotationVisitingContext.() -> Unit): AnnotationVisitor? {
        return this?.apply {
            annotationVisiting.visitor = this
            annotationVisiting.callable()
            visitEnd()
        }
    }

    fun visitAttribute(attr: Attribute) {
        visitor.visitAttribute(attr)
    }

    inline fun visitAttribute(attributeBuilder: () -> Attribute) = visitAttribute(attributeBuilder())

    inline fun visitAnnotation(desc: String, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitor.visitAnnotation(desc, visible).visitWithCallable(callable)

    inline fun visitAnnotation(type: Type, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitAnnotation(type.descriptor, visible, callable)

    fun visitParameter(access: Int, name: String) {
        visitor.visitParameter(name, access)
    }

    inline fun visitAnnotationDefault(callable: AnnotationVisitingContext.() -> Unit)
            = visitor.visitAnnotationDefault().visitWithCallable(callable)

    inline fun visitParameterAnnotation(index: Int, desc: String, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitor.visitParameterAnnotation(index, desc, visible).visitWithCallable(callable)

    inline fun visitParameterAnnotation(index: Int, type: Type, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitParameterAnnotation(index, type.descriptor, visible, callable)

    inline fun visitTypeAnnotation(typeRef: Int, typePath: TypePath? = null, desc: String, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitor.visitTypeAnnotation(typeRef, typePath, desc, visible).visitWithCallable(callable)

    inline fun visitTypeAnnotation(typeRef: Int, typePath: TypePath? = null, type: Type, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitTypeAnnotation(typeRef, typePath, type.descriptor, visible, callable)

    inline fun visitCode(callable: MethodCodeVisitingContext.() -> Unit) {
        visitor.visitCode()
        codeVisiting.visitor = visitor
        codeVisiting.callable()
    }

    lateinit var visitor: MethodVisitor
}