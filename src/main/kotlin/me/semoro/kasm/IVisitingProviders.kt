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

interface IAnnotationVisitingProvider<out TVisitor : ClassVisitor> {
    fun visitAnnotation(desc: String,
                        visible: Boolean = true,
                        callable: ClassVisitorContext<TVisitor>.AnnotationVisitingContext.() -> Unit): AnnotationVisitor?

    fun visitAnnotation(type: Type,
                        visible: Boolean = true,
                        callable: ClassVisitorContext<TVisitor>.AnnotationVisitingContext.() -> Unit): AnnotationVisitor?
            = visitAnnotation(type.descriptor, visible, callable)
}

interface IFieldVisitingProvider<out TVisitor : ClassVisitor> {
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

interface IAttributeVisitingProvider<out TVisitor : ClassVisitor> {
    fun visitAttribute(attr: Attribute)

    fun visitAttribute(attributeBuilder: () -> Attribute) {
        visitAttribute(attributeBuilder())
    }
}

interface IMethodVisitingProvider<out TVisitor : ClassVisitor> {
    fun visitMethod(
            access: Int,
            name: String,
            desc: String,
            signature: String? = null,
            exceptions: Array<String>? = null,
            callable: ClassVisitorContext<TVisitor>.MethodVisitingContext.() -> Unit
    ): MethodVisitor?

    fun visitMethod(
            access: Int,
            name: String,
            type: Type,
            signature: String? = null,
            exceptions: Array<String>? = null,
            callable: ClassVisitorContext<TVisitor>.MethodVisitingContext.() -> Unit
    ) = visitMethod(access, name, type.descriptor, signature, exceptions, callable)
}