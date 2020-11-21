package github.noahzu

import android.content.Context
import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import github.noahzu.utils.ShaderHelper
import github.noahzu.utils.TextResourceReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class AirHockeyRender(val context: Context) : GLSurfaceView.Renderer {

    companion object {
        private val POSITION_COMPONENT_COUNT = 2
        private val IS_DEBUG = true
        private val BYTES_PER_FLOAT = 4
    }
    private val tableVertices : FloatArray = floatArrayOf(
        //triangle1
        -0.5f,-0.5f,
        0.5f,0.5f,
        -0.5f,0.5f,
        //triangle2
        -0.5f,-0.5f,
        0.5f,-0.5f,
        0.5f,0.5f,
        //line1
        -0.5f,0f,
        0.5f,0f,
        //Mallets
        0f,-0.25f,
        0f,0.25f,
        //ball
        0f,0f
    )
    private var program : Int = 0
    private var uColorLocation = 0
    private var aPositionLocation = 0
    private val vertexData : FloatBuffer

    init {
        vertexData = ByteBuffer
            .allocateDirect(tableVertices.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData.put(tableVertices)
    }

    override fun onDrawFrame(gl: GL10?) {
        glClear(GL_COLOR_BUFFER_BIT)

        //画正方形
        glUniform4f(uColorLocation,1.0f,1.0f,1.0f,1.0f)
        glDrawArrays(GL_TRIANGLES,0,6)

        //画中间的线
        glUniform4f(uColorLocation,1.0f,0.0f,0.0f,1.0f)
        glDrawArrays(GL_LINES,6,2)

        //画点
        glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f)
        glDrawArrays(GL_POINTS,8,1)

        glUniform4f(uColorLocation,0.0f,0.0f,1.0f,1.0f)
        glDrawArrays(GL_POINTS,9,1)

        glUniform4f(uColorLocation,0.0f,0.0f,0.0f,0.0f)
        glDrawArrays(GL_POINTS,10,1)

        //画边框
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        glViewport(0,0,width,height)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        glClearColor(0.5f,0.5f,0.5f,0.5f)
        val vertextShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_vertex_shader)
        val fragmentShaderSource = TextResourceReader.readTextFileFromResource(context,R.raw.simple_fragment_shader)

        val vertexShader = ShaderHelper.compileVertexShader(vertextShaderSource)
        val fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource)

        program = ShaderHelper.linkProgram(vertexShader,fragmentShader)

        if(IS_DEBUG) {
            ShaderHelper.validateProgram(program)
        }
        glUseProgram(program)
        //获取uniform和属性位置
        uColorLocation = glGetUniformLocation(program,"u_Color")
        aPositionLocation = glGetAttribLocation(program,"a_Position")
        vertexData.position(0)
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT,false,0,vertexData)
        glEnableVertexAttribArray(aPositionLocation)
    }


}