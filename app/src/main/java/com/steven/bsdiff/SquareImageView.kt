package com.steven.bsdiff

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * 宽高相等的ImageView
 *
 * @author Steven Duan
 * @since 2018/11/26
 * @version 1.0
 */
class SquareImageView : AppCompatImageView {

  constructor(context: Context) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, widthMeasureSpec)
  }

}