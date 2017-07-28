package com.hr.ui.bean;

/**
 * 作者：Colin
 * 日期：2016/1/13 17:34
 * 邮箱：bestxt@qq.com
 */
public class MajorSkill {
    private long id;
    /**
     * 技能ID
     */
    private String skill_id = "";
    /**
     * 技能名称
     */
    private String skilltitle = "";
    /**
     * 使用时间
     */
    private String usetime = "";

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public String getSkill_id() {
        return skill_id;
    }

    public void setSkill_id(String skill_id) {
        this.skill_id = skill_id;
    }

    public String getSkilltitle() {
        return skilltitle;
    }

    public void setSkilltitle(String skilltitle) {
        this.skilltitle = skilltitle;
    }

    public String getUsetime() {
        return usetime;
    }

    public void setUsetime(String usetime) {
        this.usetime = usetime;
    }

    /**
     * 技能水平
     */
    private String ability = "";
}
