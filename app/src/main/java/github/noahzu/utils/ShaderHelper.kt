package github.noahzu.utils

import android.opengl.GLES20.*
import android.util.Log

object ShaderHelper {

    const val TAG = "ShaderHelper"

    fun compileVertexShader(shaderSource: String): Int = compileShader(GL_VERTEX_SHADER, shaderSource)

    fun compileFragmentShader(shaderSource: String): Int = compileShader(GL_FRAGMENT_SHADER, shaderSource)

    fun linkProgram(vertexShaderId : Int,fragmentShaderId : Int) : Int{
        val programId = glCreateProgram()
        if(programId == 0) {
            return 0
        }

        glAttachShader(programId,vertexShaderId)
        glAttachShader(programId,fragmentShaderId)
        glLinkProgram(programId)
        val linkStatus = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS,linkStatus,0)

        if(linkStatus[0] == 0) {
            glDeleteProgram(programId)
            return 0
        }
        return programId
    }

    fun validateProgram(programObjectId : Int) : Boolean {
        glValidateProgram(programObjectId)

        val validateStatus = IntArray(1)
        glGetProgramiv(programObjectId, GL_VALIDATE_STATUS,validateStatus,0)
        return validateStatus[0] != 0
    }

    private fun compileShader(type: Int, shaderSource: String): Int {
        val shaderObjectId = glCreateShader(type)
        if (shaderObjectId == 0) return 0

        //上传着色器代码
        glShaderSource(shaderObjectId, shaderSource)
        //编译着色器
        glCompileShader(shaderObjectId)
        //取出编译结果
        val result = IntArray(1)
        glGetShaderiv(shaderObjectId, GL_COMPILE_STATUS, result, 0)
        if (result[0] == 0) {
            Log.d(TAG, "error:${glGetShaderInfoLog(shaderObjectId)}")
            glDeleteShader(shaderObjectId)
            return 0
        }

        return shaderObjectId
    }


}