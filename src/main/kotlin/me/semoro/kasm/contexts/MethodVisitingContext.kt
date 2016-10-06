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
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type

class MethodVisitingContext(annotationVisitingLazy: Lazy<AnnotationVisitingContext>) {
    val annotationVisiting by annotationVisitingLazy

    fun visitAttribute(attr: Attribute) {
        visitor.visitAttribute(attr)
    }

    inline fun visitAttribute(attributeBuilder: () -> Attribute) = visitAttribute(attributeBuilder())

    inline fun visitAnnotation(desc: String, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitor.visitAnnotation(desc, visible)?.
            apply {
                annotationVisiting.visitor = this
                annotationVisiting.callable()
                visitEnd()
            }

    inline fun visitAnnotation(type: Type, visible: Boolean = true, callable: AnnotationVisitingContext.() -> Unit)
            = visitAnnotation(type.descriptor, visible, callable)


    lateinit var visitor: MethodVisitor
}