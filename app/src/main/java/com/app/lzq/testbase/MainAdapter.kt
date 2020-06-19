package com.app.lzq.testbase

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.view_item_main.view.*

/**
 *
 * @ProjectName:    TestBase
 * @Package:        com.app.lzq.testbase
 * @ClassName:      MainAdapter
 * @Description:     java类作用描述
 * @Author:        刘智强
 * @CreateDate:     2019/1/18 15:25
 * @UpdateUser:     更新者
 * @UpdateDate:     2019/1/18 15:25
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class MainAdapter(private  val datas:MutableList<String>) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>(){
    var clickFun:(data:String,pos:Int)->Unit= { s: String, i: Int -> }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    =MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.view_item_main,parent,false),clickFun)
    override fun getItemCount()=datas.size
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        ( holder as MyViewHolder).setData(datas[position])
    }
      class MyViewHolder(view:View, private val clickFun: (data: String, pos: Int) -> Unit): androidx.recyclerview.widget.RecyclerView.ViewHolder(view){
          fun setData(data:String){
            itemView.tvName.text=data
            itemView.setOnClickListener{clickFun(data,position)}
        }
    }

}