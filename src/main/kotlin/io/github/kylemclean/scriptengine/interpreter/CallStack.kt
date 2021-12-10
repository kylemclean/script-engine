package io.github.kylemclean.scriptengine.interpreter

import java.util.*

class CallStack {
    private val frames: Stack<StackFrame> = Stack()

    fun push(stackFrame: StackFrame) {
        frames.push((stackFrame))
    }

    fun peek(): StackFrame? {
        return try {
            frames.peek()
        } catch (e: EmptyStackException) {
            null
        }
    }

    fun pop(): StackFrame {
        return frames.pop()
    }
}