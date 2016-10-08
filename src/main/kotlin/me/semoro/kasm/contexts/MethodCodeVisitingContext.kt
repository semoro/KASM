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

class MethodCodeVisitingContext(annotationVisitingLazy: Lazy<AnnotationVisitingContext>) {
    lateinit var visitor: MethodVisitor
    val annotationVisiting by annotationVisitingLazy

    inline fun AnnotationVisitor?.visitWithCallable(callable: AnnotationVisitingContext.() -> Unit): AnnotationVisitor? {
        return this?.apply {
            annotationVisiting.visitor = this
            annotationVisiting.callable()
            visitEnd()
        }
    }

    fun visitFrame(type: Int, nLocal: Int, local: Array<Any>, nStack: Int,
                   stack: Array<Any>) {
        visitor.visitFrame(type, nLocal, local, nStack, stack)
    }

    fun visitInsn(opcode: Int) {
        visitor.visitInsn(opcode)
    }

    fun visitIntInsn(opcode: Int, operand: Int) {
        visitor.visitIntInsn(opcode, operand)
    }

    fun visitVarInsn(opcode: Int, variable: Int) {
        visitor.visitVarInsn(opcode, variable)
    }

    fun visitTypeInsn(opcode: Int, type: String) {
        visitor.visitTypeInsn(opcode, type)
    }

    fun visitFieldInsn(opcode: Int, owner: String, name: String,
                       desc: String) {
        visitor.visitFieldInsn(opcode, owner, name, desc)
    }


    fun visitMethodInsn(opcode: Int, owner: String, name: String,
                        desc: String, itf: Boolean) {
        visitor.visitMethodInsn(opcode, owner, name, desc, itf)
    }

    fun visitInvokeDynamicInsn(name: String, desc: String, bsm: Handle,
                               vararg bsmArgs: Any) {
        visitor.visitInvokeDynamicInsn(name, desc, bsm)
    }

    fun visitJumpInsn(opcode: Int, label: Label) {
        visitor.visitJumpInsn(opcode, label)
    }

    fun visitLabel(label: Label) {
        visitor.visitLabel(label)
    }

    fun visitLdcInsn(cst: Any) {
        visitor.visitLdcInsn(cst)
    }

    fun visitIincInsn(variable: Int, increment: Int) {
        visitor.visitIincInsn(variable, increment)
    }

    fun visitTableSwitchInsn(min: Int, max: Int, dflt: Label,
                             vararg labels: Label) {
        visitor.visitTableSwitchInsn(min, max, dflt)
    }

    fun visitLookupSwitchInsn(dflt: Label, keys: IntArray, labels: Array<Label>) {
        visitor.visitLookupSwitchInsn(dflt, keys, labels)
    }

    fun visitMultiANewArrayInsn(desc: String, dims: Int) {
        visitor.visitMultiANewArrayInsn(desc, dims)
    }

    fun visitInsnAnnotation(typeRef: Int,
                            typePath: TypePath, desc: String, visible: Boolean, callable: AnnotationVisitingContext.() -> Unit): AnnotationVisitor?
            = visitor.visitInsnAnnotation(typeRef, typePath, desc, visible).visitWithCallable(callable)


    fun visitTryCatchBlock(start: Label, end: Label, handler: Label,
                           type: String) {
        visitor.visitTryCatchBlock(start, end, handler, type)
    }

    fun visitTryCatchAnnotation(typeRef: Int,
                                typePath: TypePath, desc: String, visible: Boolean, callable: AnnotationVisitingContext.() -> Unit): AnnotationVisitor?
            = visitor.visitTryCatchAnnotation(typeRef, typePath, desc, visible).visitWithCallable(callable)


    fun visitLocalVariable(name: String, desc: String, signature: String,
                           start: Label, end: Label, index: Int) {
        visitor.visitLocalVariable(name, desc, signature, start, end, index)
    }

    fun visitLocalVariableAnnotation(typeRef: Int,
                                     typePath: TypePath, start: Array<Label>, end: Array<Label>, index: IntArray,
                                     desc: String, visible: Boolean, callable: AnnotationVisitingContext.() -> Unit): AnnotationVisitor?
            = visitor.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible).visitWithCallable(callable)


    fun visitLineNumber(line: Int, start: Label) {
        visitor.visitLineNumber(line, start)
    }

    fun visitMaxs(maxStack: Int, maxLocals: Int) {
        visitor.visitMaxs(maxStack, maxLocals)
    }

}