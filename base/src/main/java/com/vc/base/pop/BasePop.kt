package com.vc.base.pop

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.view.ViewTreeObserver
import android.app.Activity
import android.content.res.Resources
import android.graphics.Rect
import android.util.Log
import com.vc.base.BaseAct
import com.vc.base.net.BaseNetItf
import com.vc.base.util.ARouterUtil
import io.reactivex.disposables.Disposable


/**
 * Created by 35784 on 2018/4/24.
 */

abstract class BasePop constructor(private val mContext: BaseAct,
                                   private var mWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                                   private var mHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
                                   mAnimRsc: Int = 0) : PopupWindow(mContext), BaseNetItf {

    private val mContentView: View

    var isShowDim: Boolean = true //弹出pop时，背景是否变暗
    var mDimValue: Float = 0.7F//背景变暗时透明度
    var mDimView: ViewGroup? = null//背景变暗得viewGroup区域
    var mDimColor: Int = Color.BLACK//背景颜色
    var mDimX: Int = 0//背景x轴偏移量
    var mDimY: Int = 0//背景y轴偏移量

    var mAnchorView: View? = null //相对容器
    private var mVerticalGravity: VerticalGravity = VerticalGravity.BELOW
    private var mHorizontalGravity: HorizontalGravity = HorizontalGravity.ALIGN_RIGHT
    private var mOffsetX: Int = 0
    private var mOffsetY: Int = 0

    //是否只是获取宽高
    //getViewTreeObserver监听时
    private var isOnlyGetWH = true

    init {
        this.mContentView = if (this.initPopupLayout() != 0) LayoutInflater.from(mContext).inflate(this.initPopupLayout(), null) else throw IllegalArgumentException("The popup view is null")

        width = if (mWidth != 0) mWidth else ViewGroup.LayoutParams.WRAP_CONTENT
        height = if (mHeight != 0) mHeight else ViewGroup.LayoutParams.WRAP_CONTENT

        this.contentView = mContentView

        if (mAnimRsc != 0)
            this.animationStyle = mAnimRsc

        this.isFocusable = true
        this.isOutsideTouchable = true
        this.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        this.initCreateView(mContext)//只能初始化view 不能进行数据绑定
    }

    //必须得
    abstract fun initPopupLayout(): Int

    abstract fun initCreateView(mContext: BaseAct)

    abstract fun viewDrawCompleted() //可做高度获取和其他view设置

    //属性设置
    inline fun <reified T : BasePop> setmDimView(dimView: ViewGroup): T {
        this.mDimView = dimView
        return this as T
    }

    inline fun <reified T : BasePop> setDimColor(dimColor: Int): T {
        this.mDimColor = dimColor
        return this as T
    }

    inline fun <reified T : BasePop> setisBackgroundDim(isShowDim: Boolean): T {
        this.isShowDim = isShowDim
        return this as T
    }

    inline fun <reified T : BasePop> setmDimValue(dimValue: Float): T {
        this.mDimValue = dimValue
        return this as T
    }

    inline fun <reified T : BasePop> setAnchorView(anchorView: View): T {
        this.mAnchorView = anchorView
        return this as T
    }

    inline fun <reified T : BasePop> setmDimX(dimX: Int): T {
        this.mDimX = dimX
        return this as T
    }

    inline fun <reified T : BasePop> setDimY(dimY: Int): T {
        this.mDimY = dimY
        return this as T
    }

    inline fun <reified T : BasePop> setmOutsideTouchable(isDismiss: Boolean): T {
        this.isOutsideTouchable = isDismiss
        return this as T
    }

    inline fun <reified T : BasePop> setisFocusable(isFocusable: Boolean): T {
        this.isFocusable = isFocusable
        return this as T
    }

    /**
     * 相对屏幕显示
     */
    override fun showAtLocation(parent: View, offsetX: Int, offsetY: Int, gravity: Int) {
        handleBackgroundDim()
        mAnchorView = parent
        mOffsetX = offsetX
        mOffsetY = offsetY
        isOnlyGetWH = true
        addGlobalLayoutListener(contentView)
        super.showAtLocation(parent, gravity, mOffsetX, mOffsetY)
    }

    /**
     * 相对anchor view显示
     *  默认底部右对齐
     */
    fun showAtAnchorView() {
        mAnchorView?.let {
            showAtAnchorView(it, mVerticalGravity, mHorizontalGravity)
        }
    }

    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     * @param anchor
     * @param vertGravity 垂直方向的对齐方式
     * @param horizGravity 水平方向的对齐方式
     */
    fun showAtAnchorView(anchor: View, vertGravity: VerticalGravity, horizGravity: HorizontalGravity) {
        showAtAnchorView(anchor, vertGravity, horizGravity, 0, 0)
    }

    /**
     * 相对anchor view显示，适用 宽高不为match_parent
     * @param anchor
     * @param vertGravity  垂直方向的对齐方式
     * @param horizGravity 水平方向的对齐方式
     * @param x            水平方向的偏移
     * @param y            垂直方向的偏移
     */
    fun showAtAnchorView(anchor: View, vertGravity: VerticalGravity, horizGravity: HorizontalGravity, x: Int, y: Int) {
        mAnchorView = anchor
        mOffsetX = x
        mOffsetY = y
        mVerticalGravity = vertGravity
        mHorizontalGravity = horizGravity
        isOnlyGetWH = false
        //处理背景变暗
        handleBackgroundDim()
        val contentView = contentView
        addGlobalLayoutListener(contentView)
        contentView.measure(0, View.MeasureSpec.UNSPECIFIED)
        val measuredW = contentView.measuredWidth
        val measuredH = contentView.measuredHeight
        showAsDropDown(anchor, calculateX(anchor, horizGravity, measuredW, x), calculateY(anchor, vertGravity, measuredH, y))
    }

    /**
     * 根据水平gravity计算x偏移
     *
     * @param anchor
     * @param horizGravity
     * @param measuredW
     * @param x
     * @return
     */
    private fun calculateX(anchor: View, horizGravity: HorizontalGravity, measuredW: Int, x: Int): Int {
        var newX = x;
        when (horizGravity) {
            HorizontalGravity.LEFT ->
                //anchor view左侧
                newX -= measuredW
            HorizontalGravity.ALIGN_RIGHT ->
                //与anchor view右边对齐
                newX -= measuredW - anchor.width
            HorizontalGravity.CENTER ->
                //anchor view水平居中
                newX += anchor.width / 2 - measuredW / 2
            HorizontalGravity.ALIGN_LEFT -> {
            }
            HorizontalGravity.RIGHT ->
                //anchor view右侧
                newX += anchor.width
        }//与anchor view左边对齐
        // Default position.
        return newX
    }

    /**
     * 根据垂直gravity计算y偏移
     *
     * @param anchor
     * @param vertGravity
     * @param measuredH
     * @param y
     * @return
     */
    private fun calculateY(anchor: View, vertGravity: VerticalGravity, measuredH: Int, y: Int): Int {
        var newY = y
        when (vertGravity) {
            VerticalGravity.ABOVE ->
                //anchor view之上
                newY -= measuredH + anchor.height
            VerticalGravity.ALIGN_BOTTOM ->
                //anchor view底部对齐
                newY -= measuredH
            VerticalGravity.CENTER ->
                //anchor view垂直居中
                newY -= anchor.height / 2 + measuredH / 2
            VerticalGravity.ALIGN_TOP ->
                //anchor view顶部对齐
                newY -= anchor.height
            VerticalGravity.BELOW -> {
            }
        }//anchor view之下
        // Default position.
        return newY
    }

    /**
     * 更新PopupWindow位置，校验PopupWindow位置
     * 修复高度或者宽度写死时或者内部有ScrollView时，弹出的位置不准确问题
     *
     * @param width
     * @param height
     * @param anchor
     * @param vertGravity
     * @param horizGravity
     * @param x
     * @param y
     */
    private fun updateLocation(width: Int, height: Int, anchor: View, vertGravity: VerticalGravity, horizGravity: HorizontalGravity, x: Int, y: Int) {
        var newx = x
        var newy = y
        newx = calculateX(anchor, horizGravity, width, newx)
        newy = calculateY(anchor, vertGravity, height, newy)
        update(anchor, newx, newy, width, height)
    }

    //监听器，用于PopupWindow弹出时获取准确的宽高
    private val mOnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        viewDrawCompleted()
        mWidth = contentView.width
        mHeight = contentView.height
        //只获取宽高时，不执行更新操作
        if (isOnlyGetWH) {
            removeGlobalLayoutListener()
            return@OnGlobalLayoutListener
        }
        mAnchorView?.let {
            updateLocation(mWidth, mHeight, it, mVerticalGravity, mHorizontalGravity, mOffsetX, mOffsetY)
        }
        removeGlobalLayoutListener()
    }

    private fun addGlobalLayoutListener(contentView: View) {
        contentView.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    private fun removeGlobalLayoutListener() {
        contentView != null
        contentView.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    /**
     * 处理背景变暗
     * https://blog.nex3z.com/2016/12/04/%E5%BC%B9%E5%87%BApopupwindow%E5%90%8E%E8%AE%A9%E8%83%8C%E6%99%AF%E5%8F%98%E6%9A%97%E7%9A%84%E6%96%B9%E6%B3%95/
     */
    private fun handleBackgroundDim() {
        if (isShowDim) {
            mDimView?.let {
                applyDim(it)
                return@handleBackgroundDim
            }
            applyDim(mContext)
        }
    }

    private fun applyDim(activity: Activity) {
        val rect = Rect()
        val parent = activity.window.decorView as ViewGroup
        parent.getWindowVisibleDisplayFrame(rect)//这里注意状态栏
        //activity跟布局
//        val parent1 =  parent.getChildAt(0)
        val dim = ColorDrawable(mDimColor)
        dim.setBounds(mDimX, mDimY + rect.top, parent.width, parent.height)//这里top是计算了 状态栏和外边距得
        dim.alpha = (255 * mDimValue).toInt()
        val overlay = parent.overlay
        overlay.add(dim)
    }

    private fun applyDim(dimView: ViewGroup) {
        val dim = ColorDrawable(mDimColor)
        dim.setBounds(mDimX, mDimY, dimView.width, dimView.height)
        dim.alpha = (255 * mDimValue).toInt()
        val overlay = dimView.overlay
        overlay.add(dim)
    }

    /**
     * 清除背景变暗
     */
    private fun clearBackgroundDim() {
        if (isShowDim) {
            mDimView?.let {
                clearDim(it)
                return@clearBackgroundDim
            }
            clearDim(mContext)
        }
    }

    private fun clearDim(activity: Activity) {
        val parent = activity.window.decorView.rootView as ViewGroup
        //activity跟布局
        //        ViewGroup parent = (ViewGroup) parent1.getChildAt(0);
        val overlay = parent.overlay
        overlay.clear()
    }

    private fun clearDim(dimView: ViewGroup) {
        val overlay = dimView.overlay
        overlay.clear()
    }

    override fun dismiss() {
        removeGlobalLayoutListener()
        //清除背景变暗
        clearBackgroundDim()
        super.dismiss()
    }

    override var pstAddDisposable: (Disposable) -> Unit = {}

    override var netLoadingDlgDismiss: () -> Unit = {}

    override fun showNetworkException() {
        mContext.showNetworkException()
    }

    override fun showException(errorMsg: String) {
        mContext.showException(errorMsg)
    }

    override fun showDataException(errorDataMsg: String) {
        mContext.showDataException(errorDataMsg)
    }

    override fun showLoadingComplete() {
        mContext.showLoadingComplete()
    }

    override fun showLoadingDlg() {
        mContext.showLoadingDlg()
    }

    override fun dismissLoadingDlg() {
        mContext.dismissLoadingDlg()
    }

    override fun getStringRsc(rsc: Int, vararg formatArgs: Any): String = mContext.getString(rsc, formatArgs)

    override fun getRec(): Resources = mContext.resources

    override fun userLoginInvalid() {
        ARouterUtil.userLoginInvalid()
    }
}

enum class VerticalGravity { CENTER, ABOVE, BELOW, ALIGN_TOP, ALIGN_BOTTOM }

enum class HorizontalGravity { CENTER, LEFT, RIGHT, ALIGN_LEFT, ALIGN_RIGHT }