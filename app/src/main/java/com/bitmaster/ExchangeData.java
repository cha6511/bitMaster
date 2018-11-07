package com.bitmaster;

public class ExchangeData {
    private String img_url;
    private String url;
    private String name;
    private boolean new_browser_yn;

    public boolean isNew_browser_yn() {
        return new_browser_yn;
    }

    public void setNew_browser_yn(boolean new_browser_yn) {
        this.new_browser_yn = new_browser_yn;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExchangeData(String name, String img_url, String url, boolean new_browser_yn) {
        this.name = name;
        this.img_url = img_url;
        this.url = url;
        this.new_browser_yn = new_browser_yn;
    }

    public ExchangeData() {
    }
}
