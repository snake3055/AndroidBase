package com.gistandard.androidbase.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.gistandard.androidbase.R;
import com.gistandard.androidbase.event.ExitAppEvent;
import com.gistandard.androidbase.http.BaseResponse;
import com.gistandard.androidbase.http.BaseTask;
import com.gistandard.androidbase.http.IResponseListener;
import com.gistandard.androidbase.http.ResponseCode;
import com.gistandard.androidbase.utils.LogCat;
import com.gistandard.androidbase.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;


/**
 * Description: Activity基类，该App中所有其他Activity必须继承该类
 * Name:         BaseActivity
 * Author:       zhangjingming
 * Date:         2015-12-22
 */

public abstract class BaseActivity extends AppCompatActivity implements IResponseListener {

    // 默认日志Tag
    protected static String LOG_TAG = BaseActivity.class.getSimpleName();

    // 网络请求等待对话框，只用于网络等待状态显示
    // 可配置是否可取消请求功能，如果可取消，则在对话框消失时取消当前网络请求
    protected Dialog waitingDlg = null;

    //上下文
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        context = BaseActivity.this;
        LOG_TAG = getClass().getSimpleName();
        EventBus.getDefault().register(exitAppListener);

        // 4.4以上设备适用，4.4以下使用系统默认颜色
        if (Build.VERSION.SDK_INT >= 19) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //            //透明导航栏
            //            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

        // 设置完layout后，初始化控件之前调用
        onViewInit();

        // 分别完成初始化子控件、设置监听、加载数据操作，顺序不可调换
        initView();
        initListener();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dismissWaitingDlg();
        EventBus.getDefault().unregister(exitAppListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        MobclickAgent.onPageStart(getClass().getSimpleName());
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        MobclickAgent.onPageEnd(getClass().getSimpleName());
    }

    /**
     * 视图创建方法，由子类activity实现特定的视图加载
     */
    public abstract void onViewInit();

    /**
     * 获取layout资源ID，非外部调用方法，子类返回layout资源ID
     *
     * @return layout ID
     */
    abstract protected int getLayoutResId();

    /**
     * 初始化子控件，非外部调用方法，子类实现子控件的初始化
     */
    abstract protected void initView();

    /**
     * 初始化控件监听方法，非外部调用方法，子类实现子控件的监听函数
     */
    abstract protected void initListener();

    /**
     * 初始化数据方法，非外部调用方法，子类实现子控件的原始数据加载或获取网络数据等操作
     */
    abstract protected void initData();

    /**
     * 显示等待对话框
     *
     * @param cancelable 是否可取消
     */
    public void showWaitingDlg(final boolean cancelable) {
        if (null == waitingDlg) {
            // 创建等待对话框
            waitingDlg = new ProgressDialog(this);
        } else {
            // 已经显示则不再做处理
            if (waitingDlg.isShowing())
                return;
        }

        waitingDlg.setCancelable(cancelable);
        waitingDlg.show();
    }

    /**
     * 隐藏等待对话框
     */
    public void dismissWaitingDlg() {
        if (null == waitingDlg || isFinishing())
            return;

        waitingDlg.dismiss();
    }

    /**
     * 发送可取消网络请求，弹出等待框
     *
     * @param task 网络请求
     */
    public void excuteTask(BaseTask task) {
        this.excuteTask(task, true);
    }

    /**
     * 发送网络请求，弹出等待框
     *
     * @param task       网络请求
     * @param cancelable 是否可取消
     */
    public void excuteTask(BaseTask task, boolean cancelable) {
        if (null == task || isFinishing())
            return;

        showWaitingDlg(cancelable);
        task.setCancelable(cancelable);
        task.excute(this);
    }

    /**
     * 取消网络请求
     *
     * @param task 网络请求
     */
    public void cancelTask(BaseTask task) {
        if (null == task || !task.isCancelable())
            return;

        task.cancel();
        dismissWaitingDlg();
    }

    /**
     * 服务端返回响应码成功回调接口
     *
     * @param @param response 服务端返回结果
     * @return void
     */
    @Override
    public void onTaskSuccess(BaseResponse response) {
        if (!isFinishing())
            dismissWaitingDlg();
    }

    /**
     * 服务端返回错误回调接口
     *
     * @param requestId    请求ID
     * @param responseCode 错误码
     * @param responseMsg  错误信息
     */
    @Override
    public void onTaskError(long requestId, int responseCode, String responseMsg) {
        if (!isFinishing()) {
            ToastUtils.toastShort(getErrorMessage(this, responseCode, responseMsg));
            dismissWaitingDlg();
        }
    }

    /**
     * 退出程序时finish activity
     */
    private Object exitAppListener = new Object() {

        public void onEvent(ExitAppEvent exitAppEvent) {
            finish();
        }
    };

    /**
     * 获取系统错误信息
     * @param context 上下文
     * @param responseCode 响应码
     * @param responseMsg 响应消息
     * @return 错误信息
     */
    private String getErrorMessage(Context context, int responseCode, String responseMsg) {
        if (!TextUtils.isEmpty(responseMsg) || ResponseCode.RESPONSE_CODE_SUCCESS == responseCode)
            return responseMsg;

        if (null == context) {
            LogCat.e("ResponseCode", "==== Context invalid ====");
            return "";
        }

        switch (responseCode) {
            case ResponseCode.RESPONSE_ERROR_NETWORK:
                return getString(R.string.error_network);

            case ResponseCode.RESPONSE_ERROR_PARSE:
                return getString(R.string.error_parse);

            case ResponseCode.RESPONSE_ERROR_SERVER:
                return getString(R.string.error_server);

            case ResponseCode.RESPONSE_ERROR_DEFAULT:
            default:
                return getString(R.string.error_system);
        }
    }
}
