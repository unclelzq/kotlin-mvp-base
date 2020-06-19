package com.app.lzq.testbase.navigation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.app.lzq.testbase.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vc.base.BaseAct
import kotlinx.android.synthetic.main.activity_navigation.*
import kotlinx.coroutines.*

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase.navigation
 * @ClassName:      NavigationActivity
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/2/20 9:48
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/2/20 9:48
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class  NavigationActivity:BaseAct(){

    private val f1 by lazy{var f=Fragment1()
        var bundle=Bundle()
        bundle.putString("title","首页")
        f.arguments=bundle
        return@lazy  f
    }
    private val f2 by lazy{var f=Fragment2()
        var bundle=Bundle()
        bundle.putString("title","产品")
        f.arguments=bundle
        return@lazy  f
    }
    private val f3 by lazy{var f=Fragment3()
        var bundle=Bundle()
    bundle.putString("title","我的")
    f.arguments=bundle
    return@lazy  f}
    private val f4 by lazy{var f=Fragment4()
        var bundle=Bundle()
    bundle.putString("title","设置")
    f.arguments=bundle
    return@lazy  f
    }
    private val fragments by lazy{ arrayOf(f1,f2,f3,f4)}
    private var lastFragment=0
    override fun initLayout()= R.layout.activity_navigation

    override fun initViews(savedInstanceState: Bundle?) {
        supportFragmentManager.beginTransaction()
                .add(R.id.framelayout,f1)
                .show(f1)
                .commitAllowingStateLoss()
        navigation.itemIconTintList=null//取消点击后，图标的原始颜色不显示
        GlobalScope.launch(Dispatchers.Main) {
            var task1= withContext(Dispatchers.Default){
                delay(2000)
                "task1执行完成，task2开始执行"

            }
            var task2= withContext(Dispatchers.Default){
                delay(2000)
                "task2执行完成了"

            }


        }
    }
    override fun initEvent() {
        navigation.setOnNavigationItemSelectedListener(object: BottomNavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
               when(item.itemId){
                   R.id.item_1->{
                       if (lastFragment!=0){
                           switchFragment(lastFragment,0)
                           lastFragment=0
                       }
                       return  true
                   }
                   R.id.item_2->{
                       if (lastFragment!=1){
                           switchFragment(lastFragment,1)
                           lastFragment=1
                       }
                       return  true
                   }
                   R.id.item_3->{
                       if (lastFragment!=2){
                           switchFragment(lastFragment,2)
                           lastFragment=2
                       }
                       return  true
                   }
                   R.id.item_4->{
                       if (lastFragment!=3){
                           switchFragment(lastFragment,3)
                           lastFragment=3
                       }
                       return  true
                   }
               }
                return  false

            }

        })
    }

    private  fun switchFragment(lastFragment:Int,index:Int){
        var transaction=supportFragmentManager.beginTransaction()
            transaction.hide(fragments[lastFragment])
        if (!fragments[index].isAdded){
            transaction.add(R.id.framelayout,fragments[index])
        }
        transaction.show(fragments[index]).commitAllowingStateLoss()
    }

    private  var mIsClicked=false

    override fun finish() {
        Log.e("TAG","$mIsClicked")
        if (mIsClicked){
            super.finish()
        }else{
            mIsClicked = true

          GlobalScope.launch{   toast("再按一次退出程序")
             kotlinx.coroutines.delay(2000)
              mIsClicked=false

          }

        }
    }

    override fun overridePendingTransition(enterAnim: Int, exitAnim: Int) {
        super.overridePendingTransition(enterAnim, exitAnim)
    }


}