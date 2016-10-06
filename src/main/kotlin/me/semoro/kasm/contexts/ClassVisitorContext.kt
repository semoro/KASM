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


import org.objectweb.asm.ClassVisitor

class ClassVisitorContext<out TVisitor : ClassVisitor>(val visitor: TVisitor) {

    private val annotationVisiting = lazy { AnnotationVisitingContext() }
    private val fieldVisiting = lazy { FieldVisitingContext(annotationVisiting) }
    private val methodVisiting = lazy { MethodVisitingContext(annotationVisiting) }
    private val classVisiting = lazy { ClassVisitingContext(visitor, annotationVisiting, fieldVisiting, methodVisiting) }
    val classVisitingContext by classVisiting

    inline fun visitClass(access: Int,
                          name: String,
                          superClass: String? = "java/lang/Object",
                          interfaces: Array<String>? = null,
                          signature: String? = null,
                          version: Int = defaultClassVersion,
                          contextCallable: ClassVisitingContext<TVisitor>.() -> Unit) {
        visitor.visit(version, access, name, signature, superClass, interfaces)
        classVisitingContext.contextCallable()
        visitor.visitEnd()
    }

    companion object {
        var defaultClassVersion = 52
    }
}