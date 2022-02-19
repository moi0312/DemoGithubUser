package com.edlo.mydemoapp.ui.base

import android.content.Intent
import android.os.Bundle
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.FragmentManager
import com.edlo.mydemoapp.R
import com.edlo.mydemoapp.util.Consts

open class AppActivity : AppCompatActivity() {

    protected fun pushActivity(nextActivity: Class<out AppActivity>,
                               bundleMap: HashMap<String, Any>? = null,
                               flags: Int = 0, requestCode: Int = 0,
                               @AnimRes animIn: Int = ANIM_SLIDE_LEFT_IN,
                               @AnimRes animOut: Int = ANIM_EMPTY) {

        val intent = Intent(this, nextActivity)
        val bundle = Bundle()
        if (bundleMap != null && bundleMap.size > 0) {
            bundle.putSerializable(Consts.INTENT_MAP_DATA, bundleMap)
            intent.putExtras(bundle)
        }
        intent.flags = flags
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        if (requestCode != 0) {
            ActivityCompat.startActivityForResult(
                this, intent, requestCode, ActivityOptionsCompat.makeCustomAnimation(
                    this, animIn, animOut).toBundle()
            )
        } else {
            ActivityCompat.startActivity(
                this, intent, ActivityOptionsCompat.makeCustomAnimation(
                    this, animIn, animOut).toBundle()
            )
        }
    }

    protected fun pushFragment(fragClass: Class<out AppFragment>, @IdRes viewId: Int = R.id.container,
             @AnimRes animIn: Int = ANIM_SLIDE_LEFT_IN, @AnimRes animOut: Int = ANIM_FADE_OUT,
             addToBackStack: Boolean = true) {
        changeFragment(fragClass, viewId, animIn, animOut, addToBackStack, false)
    }

    protected fun changeFragment(fragClass: Class<out AppFragment>, @IdRes viewId: Int = R.id.container,
            @AnimRes animIn: Int = ANIM_FADE_IN, @AnimRes animOut: Int = ANIM_SLIDE_LEFT_OUT,
            addToBackStack: Boolean = false, clearBackStack: Boolean = true) {

        val fragmentManager = this.supportFragmentManager
        var fragment: AppFragment? =
            fragmentManager.findFragmentByTag(fragClass.canonicalName) as AppFragment?
        if (null == fragment) {
            fragment = try {
                fragClass.newInstance()
            } catch (e: Exception) {
                e.printStackTrace()
                return
            }
        }
        try {
            if (clearBackStack) {
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            }
            fragment?.let { frag ->
                if (addToBackStack) {
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(animIn, animOut,
                            ANIM_SLIDE_RIGHT_IN, ANIM_SLIDE_RIGHT_OUT)
                        .replace(viewId, frag, fragClass.canonicalName)
                        .addToBackStack(fragClass.canonicalName)
                        .commitAllowingStateLoss()
                } else {
                    fragmentManager.beginTransaction()
                        .setCustomAnimations(animIn, animOut)
                        .replace(viewId, frag, fragClass.canonicalName)
                        .commitAllowingStateLoss()
                }
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }


    companion object {
        val ANIM_EMPTY: Int = R.anim.empty
        val ANIM_FADE_IN: Int = R.anim.fade_in
        val ANIM_FADE_OUT: Int = R.anim.fade_out
        val ANIM_SLIDE_LEFT_IN: Int = R.anim.slide_left_in
        val ANIM_SLIDE_LEFT_OUT: Int = R.anim.slide_left_out
        val ANIM_SLIDE_DOWN_IN: Int = R.anim.slide_down_in
        val ANIM_SLIDE_DOWN_OUT: Int = R.anim.slide_down_out
        val ANIM_SLIDE_RIGHT_IN: Int = R.anim.slide_right_in
        val ANIM_SLIDE_RIGHT_OUT: Int = R.anim.slide_right_out
    }
}