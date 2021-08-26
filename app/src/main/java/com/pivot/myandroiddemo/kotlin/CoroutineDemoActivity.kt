package com.pivot.myandroiddemo.kotlin

import android.os.Bundle
import android.util.Log
import com.pivot.myandroiddemo.R
import com.pivot.myandroiddemo.base.ActivityParam
import com.pivot.myandroiddemo.base.BaseActivity
import kotlinx.android.synthetic.main.activity_coroutine_demo.*
import kotlinx.android.synthetic.main.activity_kotlin_main.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@ActivityParam(isShowReturn = true, isShowToolBar = true)
class CoroutineDemoActivity : BaseActivity(), CoroutineScope {
    lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_demo)
        setToolbarTitle("协程")
        toolBarTitleView.setTextColor(resources.getColor(R.color.white))
        setToolBarBackgroundColor(resources.getColor(R.color.colorPrimary))
        
        job = Job()
        
        btn_run_blocking.setOnClickListener {
            Log.e("CoroutineDemoActivity", "1主线程id：${mainLooper.thread.id}开始")
            test1()
            Log.e("CoroutineDemoActivity", "1主线程id：${mainLooper.thread.id}结束")
        }
        btn_launch.setOnClickListener {
            Log.e("CoroutineDemoActivity", "2主线程id：${mainLooper.thread.id}开始")
            test2()
            Log.e("CoroutineDemoActivity", "2主线程id：${mainLooper.thread.id}结束")
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()         
        // 当 Activity 销毁的时候取消该 Scope 管理的 job。这样在该 Scope 内创建的子 Coroutine 都会被自动的取消。         
         job.cancel()     
    }

    //四种开启协程的方式
    //.launch - 创建协程
    //.async - 创建带返回值的协程，返回的是 Deferred，launch的返回结果是Job类型，而async的返回结果是Deferred类型。 从类的层次结构上看，Deferred是Job的子接口；从功能上来看，Deferred就是带返回结果的Job，在执行完毕通过 await() 函数获取返回值
    //.withContext - 不创建新的协程，必须运行在挂起函数或者协程代码块中，而且withContext这种方式是串行的，并且逐一返回结果，这是因为withContext是个 suspend 函数，当运行到withContext时所在的协程就会挂起，直到withContext执行完成后再执行下面的方法。所以withContext可以用在一个请求结果依赖另一个请求结果的这种情况
    //.runBlocking - 通常适用于单元测试的场景，而业务开发中不会用到这个函数, 不是 GlobalScope 的 API，可以独立使用，区别是 runBlocking 里面的 delay 会阻塞线程，而 launch 创建的不会
    private fun test1(){
        runBlocking(Dispatchers.Default) {
            test4()
            repeat(3) {
                Log.e("CoroutineDemoActivity", "runBlocking协程执行$it 线程id：${Thread.currentThread().id}")
                delay(1000)
            }
        }
    }
    private fun test2(){
//        CoroutineStart - 启动模式，默认是DEAFAULT，也就是创建就启动
//        DEFAULT - 默认模式，立即执行协程体，随时可以取消
//        ATOMIC - 立即执行协程体，但在开始运行协程体之前无法取消
//        UNDISPATCHED - 立即在当前线程执行协程体，直到第一个 suspend函数调用，这里可以理解为耗时函数
//        LAZY - 懒加载模式，你需要它的时候，再调用job.start()启动
//          job.start() - 启动协程，除了 lazy 模式，协程都不需要手动启动
//          job.join() - 等待协程执行完毕
//          job.cancel() - 取消一个协程
//          job.cancelAndJoin() - 等待协程执行完毕然后再取消
        val job = GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT) {
            test3()
            repeat(3) {
                Log.e("CoroutineDemoActivity", "launch协程执行$it 线程id：${Thread.currentThread().id}")
                delay(1000)
            }
        }
    }
    private suspend fun test3(){
        val deferred = GlobalScope.async<Any> {
            repeat(3) {
                Log.e("CoroutineDemoActivity", "async协程执行$it 线程id：${Thread.currentThread().id}")
                delay(1000)
            }
            return@async "s"
        }
        Log.e("CoroutineDemoActivity", "async协程执行结果${deferred.await()}")
        val defed = coroutineScope<Int> {
            repeat(3) {
                Log.e("CoroutineDemoActivity", "async协程执行$it 线程id：${Thread.currentThread().id}")
                delay(1000)
            }
            return@coroutineScope 1
        }
        Log.e("CoroutineDemoActivity", "async协程执行结果${defed}")
    }
    private suspend fun test4() = withContext(Dispatchers.IO){
        repeat(3) {
            Log.e("CoroutineDemoActivity", "withContext协程执行$it 线程id：${Thread.currentThread().id}")
            delay(1000)
        }
    }
}