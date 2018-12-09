package com.wangkeke.goomvpdemo;

import com.wangkeke.goomvpdemo.base.BaseModel;
import com.wangkeke.goomvpdemo.base.BasePresenter;
import com.wangkeke.goomvpdemo.base.BaseView;

public interface MainContract {

    interface View extends BaseView{

        void showLoadingDialoig();

        void dismissLoadingDialog();

        void showData(String data);

        void failed(String errorMsg);
    }

    interface Presenter extends BasePresenter{
        void sendRequest(int start,int count);

        void onDestory();
    }

    interface MainModel extends BaseModel{

        void getMainData(int start,int count,Result callback);

        interface Result{
            void successResponse(String response);
            void failedResponse(String errorMsg);
        }
    }

}
