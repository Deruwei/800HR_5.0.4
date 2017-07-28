package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 17:00
 * 邮箱：bestxt@qq.com
 */
public class LanguageAbility {
    private Long id;
    /**
     * 语言能力ID
     */
    private String language_id = "";
    /**
     * 语言名称ID
     */
    private String langname = "";
    /**
     * 读写能力ID
     */
    private String read_level = "";
    /**
     * 听说能力ID
     */
    private String speak_level = "";

    public String getSpeak_level() {
        return speak_level;
    }

    public void setSpeak_level(String speak_level) {
        this.speak_level = speak_level;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getLangname() {
        return langname;
    }

    public void setLangname(String langname) {
        this.langname = langname;
    }

    public String getRead_level() {
        return read_level;
    }

    public void setRead_level(String read_level) {
        this.read_level = read_level;
    }
}
