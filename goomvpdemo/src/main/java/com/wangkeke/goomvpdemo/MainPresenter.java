package com.wangkeke.goomvpdemo;

import java.lang.ref.WeakReference;

public class MainPresenter implements MainContract.Presenter{

    private WeakReference<MainContract.View> wrView;

    private MainModel mainModel;


    public MainPresenter(MainContract.View mainView) {
        wrView = new WeakReference<>(mainView);
        this.mainModel = new MainModel();
    }

    @Override
    public void sendRequest(int start,int count) {
        wrView.get().showLoadingDialoig();
        mainModel.getMainData(start, count, new MainContract.MainModel.Result() {
            @Override
            public void successResponse(String response) {
                wrView.get().dismissLoadingDialog();
                wrView.get().showData(response);
            }

            @Override
            public void failedResponse(String errorMsg) {
                wrView.get().dismissLoadingDialog();
                wrView.get().failed(errorMsg);
            }
        });
    }

    @Override
    public void onDestory() {

    }

    @Override
    public void start() {

    }
}
