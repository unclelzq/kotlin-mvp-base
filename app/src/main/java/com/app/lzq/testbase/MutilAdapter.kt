//package com.app.lzq.testbase
//
//import android.content.Context
//import android.support.v7.widget.RecyclerView
//import android.view.View
//import android.view.ViewGroup
//import kotlinx.android.synthetic.main.adapter_item_header.view.*
//import kotlinx.android.synthetic.main.adapter_item_type1.view.*
//import retrofit2.http.HEAD
//
///**
// *
// * @ProjectName:    TestBase
// * @Package:        com.app.lzq.testbase
// * @ClassName:      MutilAdapter
// * @Description:     java类作用描述
// * @Author:        刘智强
// * @CreateDate:     2019/1/25 17:54
// * @UpdateUser:     更新者
// * @UpdateDate:     2019/1/25 17:54
// * @UpdateRemark:   更新说明
// * @Version:        1.0
// */
//
//class  MutilAdapter<T>(var context: Context, private var datas:MutableList<BaseData<T>>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
//
//  companion object {
//      private  const  val HEADER=1
//      private  const  val CONTENT=2
//  }
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        when(viewType){
//            HEADER->{
//                return  HeaderViewHolder<T>(View.inflate(context,R.layout.adapter_item_header,parent))
//            }
//        }
//        return  ContentViewHolder<T>(View.inflate(context,R.layout.adapter_item_type1,parent))
//    }
//
//    override fun getItemCount()=datas.size
//    override fun getItemViewType(position: Int): Int {
//        return if (position==0){
//            HEADER
//        }else{
//            CONTENT
//        }
//        return super.getItemViewType(position)
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//
//        when(getItemViewType(position)){
//           HEADER ->{
//                (holder ).setData(datas[position])
//
//            }
//            is ContentViewHolder<*>->{
//
//            }
//        }
//
//    }
//    class HeaderViewHolder<T>(item:View):RecyclerView.ViewHolder(item){
//       public fun setData(data: BaseData<T>){
//            itemView.tvHeader.text= data.data.toString()
//        }
//
//    }
//    class ContentViewHolder<T>(item:View):RecyclerView.ViewHolder(item){
//        fun setData(data: BaseData<T>){
//            itemView.tvContent.text= data.data.toString()
//        }
//    }
//}