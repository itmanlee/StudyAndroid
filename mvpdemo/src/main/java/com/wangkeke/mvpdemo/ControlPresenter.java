package com.wangkeke.mvpdemo;

public class ControlPresenter {

    private ModelViewTest modelView;

    private IControlActivityVIew controlActivityView;

    public ControlPresenter(ModelViewTest modelView, IControlActivityVIew controlActivityView) {
        this.modelView = modelView;
        this.controlActivityView = controlActivityView;
    }

    public void changeViewTextColor(int color){
        modelView.setCustomFontColor(color);
    }

    public void viewChangeActivityToToast(String message){
        controlActivityView.showToast(message);
    }
}
