package com.bitmaster.EventBus;


public class Events {

    public static final String GO_WEB_VIEW = "GO_WEB_VIEW";
    public static final String GO_NEW_BROWSER = "GO_NEW_BROWSER";


    public static class Msg{
        private boolean isNewBrowser;
        private String url;
        public Msg(boolean isNewBrowser, String url){
            this.isNewBrowser = isNewBrowser;
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
        public boolean isNew(){
            return this.isNewBrowser;
        }
    }


    public static class ControlWebView{
        private String url;
        public ControlWebView(String url){
            this.url = url;
        }
        public String getUrl(){
            return this.url;
        }
    }


    public static class BackToMain{
        private String msg;
        public BackToMain(String msg){
            this.msg = msg;
        }

        public String getMsg() {
            return this.msg;
        }
    }

    public static class Refresh{
        public Refresh(){}
    }
}
