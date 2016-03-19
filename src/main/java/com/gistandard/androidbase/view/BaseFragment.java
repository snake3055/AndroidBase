package com.gistandard.androidbase.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gistandard.androidbase.http.BaseResponse;
import com.gistandard.androidbase.http.ResponseCode;
import com.gistandard.androidbase.http.IResponseListener;
import com.gistandard.androidbase.utils.ToastUtils;

/**
 * Description:Fragment基类，该App种所有Fragment需要统一继承该基类
 * Name:         BaseFragment
 * Author:       zhangjingming
 * Date:         2015-12-28
 */

public abstract class BaseFragment extends Fragment implements IResponseListener {

    // 默认日志Tag
    protected static String LOG_TAG = "BaseFragment";


    // 当前fragment是否已经被销毁，已经销毁则不处理异步请求
    protected boolean isFragmentDestroy = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        LOG_TAG = getClass().getSimpleName();
        isFragmentDestroy = false;

        View view = inflater.inflate(getLayoutResId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 分别完成初始化子控件、设置监听、加载数据操作，顺序不可调换
        if (null != view) {
            initView(view);
            initListener(view);
            initData(view);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFragmentDestroy = true;
        getBaseActivity().dismissWaitingDlg();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /**
     * 获取layout资源ID，非外部调用方法，子类返回layout资源ID
     *
     * @return layout ID
     */
    abstract protected int getLayoutResId();

    /**
     * 初始化子控件，非外部调用方法，子类实现子控件的初始化
     *
     * @param view 父视图
     */
    abstract protected void initView(View view);

    /**
     * 初始化控件监听方法，非外部调用方法，子类实现子控件的监听函数
     *
     * @param view 父视图
     */
    abstract protected void initListener(View view);

    /**
     * 初始化数据方法，非外部调用方法，子类实现子控件的原始数据加载或获取网络数据等操作
     *
     * @param view 父视图
     */
    abstract protected void initData(View view);

    /**
     * 服务端返回响应码成功回调接口
     *
     * @param @param response 服务端返回结果
     * @return void
     */
    @Override
    public void onTaskSuccess(BaseResponse response) {
        if (!isFragmentDestroy)
            getBaseActivity().dismissWaitingDlg();
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
        if (!isFragmentDestroy) {
            ToastUtils.toastShort(ResponseCode.getErrorMessage(getActivity(), responseCode, responseMsg));
            getBaseActivity().dismissWaitingDlg();
        }
    }

    /**
     * 获取基类Activity对象
     * @return BaseActivity
     */
    protected BaseActivity getBaseActivity() {
        return (BaseActivity)super.getActivity();
    }

    /**
     * 刷新当前fragment，用户外部调用界面刷新，子类实现刷新方法
     */
    public void refresh() {}
}
