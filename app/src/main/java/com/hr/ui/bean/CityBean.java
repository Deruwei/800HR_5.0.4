package com.hr.ui.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wdr on 2017/8/25.
 */

public class CityBean {

    private List<AreaBean> areaBeanList;

    public List<AreaBean> getAreaBeanList() {
        return areaBeanList;
    }

    public void setAreaBeanList(List<AreaBean> areaBeanList) {
        this.areaBeanList = areaBeanList;
    }

    public static class AreaBean {
        /**
         * 240000 : 建筑设计
         * 240101 : 总建筑师/高级建筑师/主创建筑师
         * 240102 : 建筑师/建筑设计师
         * 240112 : 古建筑设计师
         * 240103 : 方案设计师
         * 240104 : 施工图设计师
         * 240105 : 总图设计师
         * 240106 : 审图工程师
         * 240108 : 助理建筑师
         * 240109 : 绘图员
         * 240110 : 效果图/渲染/建模
         * 240111 : 平面/动画设计/多媒体制作
         * 240113 : BIM工程师
         * 241000 : 土木/工程
         * 241101 : 总工程师/高级工程师
         * 241102 : 工程总监
         * 241138 : 工程经理
         * 241104 : 结构工程师
         * 241105 : 土建工程师
         * 241106 : 测量/测绘工程师
         * 241133 : 调试员/工程师
         * 241135 : 防护/防化工程师
         * 241109 : 混凝土工程师
         * 241110 : 水利水电工程师
         * 241111 : 岩土/地质工程师
         * 241112 : 现场工程师
         * 241113 : 设备工程师
         * 241136 : 机械工程师
         * 241115 : 建筑材料工程师
         * 241118 : 测量员
         * 241122 : 调度员
         * 241123 : 采购
         * 241127 : 仪表工程师
         * 241125 : 桩基工程师
         * 241126 : 加固工程师
         * 241130 : 管道工程师
         * 241128 : 地基工程师
         * 241129 : 爆破工程师
         * 241131 : 研发工程师
         * 241137 : 钢筋翻样
         * 255000 : 工程/项目管理
         * 255101 : 建造师
         * 255102 : 项目经理
         * 255109 : 项目助理
         * 255103 : 项目管理工程师
         * 255104 : 计划控制工程师
         * 255105 : 责任工程师
         * 255106 : 咨询/评估/顾问
         * 255108 : 绿色建筑咨询工程师
         * 255107 : 工长
         * 396000 : 规划设计
         * 396101 : 规划设计师
         * 396102 : 城市/市政规划
         * 396103 : 园林/景观规划
         * 396104 : 旅游规划
         * 396105 : 农村规划
         * 396106 : 产业规划
         * 249000 : 现场专业人员
         * 249105 : 施工员
         * 249106 : 监理员/工程师
         * 249107 : 材料员
         * 249108 : 资料员
         * 249109 : 标准员
         * 249110 : 机械员
         * 249111 : 劳务员
         * 249112 : 钢筋翻样
         * 243000 : 装饰装修/室内设计
         * 243101 : 设计总监
         * 243102 : 室内设计师
         * 243117 : 深化设计师
         * 243114 : 效果图/渲染/建模
         * 243115 : 平面设计师
         * 243103 : 装饰设计师/工程师
         * 243104 : 幕墙设计师/工程师
         * 243105 : 环境设计师
         * 243106 : 空间设计师
         * 243107 : 精装设计师/工程师
         * 243108 : 配饰设计师
         * 243109 : 陈设设计师
         * 243111 : 门窗设计师/工程师
         * 243112 : 软装设计师
         * 243120 : 硬装设计师
         * 243113 : 家具设计师
         * 243116 : 绘图员
         * 243118 : 设计师助理
         * 243119 : 工程师助理
         */

        @SerializedName("240000")
        private String _$240000;
        @SerializedName("240101")
        private String _$240101;
        @SerializedName("240102")
        private String _$240102;
        @SerializedName("240112")
        private String _$240112;
        @SerializedName("240103")
        private String _$240103;
        @SerializedName("240104")
        private String _$240104;
        @SerializedName("240105")
        private String _$240105;
        @SerializedName("240106")
        private String _$240106;
        @SerializedName("240108")
        private String _$240108;
        @SerializedName("240109")
        private String _$240109;
        @SerializedName("240110")
        private String _$240110;
        @SerializedName("240111")
        private String _$240111;
        @SerializedName("240113")
        private String _$240113;
        @SerializedName("241000")
        private String _$241000;
        @SerializedName("241101")
        private String _$241101;
        @SerializedName("241102")
        private String _$241102;
        @SerializedName("241138")
        private String _$241138;
        @SerializedName("241104")
        private String _$241104;
        @SerializedName("241105")
        private String _$241105;
        @SerializedName("241106")
        private String _$241106;
        @SerializedName("241133")
        private String _$241133;
        @SerializedName("241135")
        private String _$241135;
        @SerializedName("241109")
        private String _$241109;
        @SerializedName("241110")
        private String _$241110;
        @SerializedName("241111")
        private String _$241111;
        @SerializedName("241112")
        private String _$241112;
        @SerializedName("241113")
        private String _$241113;
        @SerializedName("241136")
        private String _$241136;
        @SerializedName("241115")
        private String _$241115;
        @SerializedName("241118")
        private String _$241118;
        @SerializedName("241122")
        private String _$241122;
        @SerializedName("241123")
        private String _$241123;
        @SerializedName("241127")
        private String _$241127;
        @SerializedName("241125")
        private String _$241125;
        @SerializedName("241126")
        private String _$241126;
        @SerializedName("241130")
        private String _$241130;
        @SerializedName("241128")
        private String _$241128;
        @SerializedName("241129")
        private String _$241129;
        @SerializedName("241131")
        private String _$241131;
        @SerializedName("241137")
        private String _$241137;
        @SerializedName("255000")
        private String _$255000;
        @SerializedName("255101")
        private String _$255101;
        @SerializedName("255102")
        private String _$255102;
        @SerializedName("255109")
        private String _$255109;
        @SerializedName("255103")
        private String _$255103;
        @SerializedName("255104")
        private String _$255104;
        @SerializedName("255105")
        private String _$255105;
        @SerializedName("255106")
        private String _$255106;
        @SerializedName("255108")
        private String _$255108;
        @SerializedName("255107")
        private String _$255107;
        @SerializedName("396000")
        private String _$396000;
        @SerializedName("396101")
        private String _$396101;
        @SerializedName("396102")
        private String _$396102;
        @SerializedName("396103")
        private String _$396103;
        @SerializedName("396104")
        private String _$396104;
        @SerializedName("396105")
        private String _$396105;
        @SerializedName("396106")
        private String _$396106;
        @SerializedName("249000")
        private String _$249000;
        @SerializedName("249105")
        private String _$249105;
        @SerializedName("249106")
        private String _$249106;
        @SerializedName("249107")
        private String _$249107;
        @SerializedName("249108")
        private String _$249108;
        @SerializedName("249109")
        private String _$249109;
        @SerializedName("249110")
        private String _$249110;
        @SerializedName("249111")
        private String _$249111;
        @SerializedName("249112")
        private String _$249112;
        @SerializedName("243000")
        private String _$243000;
        @SerializedName("243101")
        private String _$243101;
        @SerializedName("243102")
        private String _$243102;
        @SerializedName("243117")
        private String _$243117;
        @SerializedName("243114")
        private String _$243114;
        @SerializedName("243115")
        private String _$243115;
        @SerializedName("243103")
        private String _$243103;
        @SerializedName("243104")
        private String _$243104;
        @SerializedName("243105")
        private String _$243105;
        @SerializedName("243106")
        private String _$243106;
        @SerializedName("243107")
        private String _$243107;
        @SerializedName("243108")
        private String _$243108;
        @SerializedName("243109")
        private String _$243109;
        @SerializedName("243111")
        private String _$243111;
        @SerializedName("243112")
        private String _$243112;
        @SerializedName("243120")
        private String _$243120;
        @SerializedName("243113")
        private String _$243113;
        @SerializedName("243116")
        private String _$243116;
        @SerializedName("243118")
        private String _$243118;
        @SerializedName("243119")
        private String _$243119;

        public String get_$240000() {
            return _$240000;
        }

        public void set_$240000(String _$240000) {
            this._$240000 = _$240000;
        }

        public String get_$240101() {
            return _$240101;
        }

        public void set_$240101(String _$240101) {
            this._$240101 = _$240101;
        }

        public String get_$240102() {
            return _$240102;
        }

        public void set_$240102(String _$240102) {
            this._$240102 = _$240102;
        }

        public String get_$240112() {
            return _$240112;
        }

        public void set_$240112(String _$240112) {
            this._$240112 = _$240112;
        }

        public String get_$240103() {
            return _$240103;
        }

        public void set_$240103(String _$240103) {
            this._$240103 = _$240103;
        }

        public String get_$240104() {
            return _$240104;
        }

        public void set_$240104(String _$240104) {
            this._$240104 = _$240104;
        }

        public String get_$240105() {
            return _$240105;
        }

        public void set_$240105(String _$240105) {
            this._$240105 = _$240105;
        }

        public String get_$240106() {
            return _$240106;
        }

        public void set_$240106(String _$240106) {
            this._$240106 = _$240106;
        }

        public String get_$240108() {
            return _$240108;
        }

        public void set_$240108(String _$240108) {
            this._$240108 = _$240108;
        }

        public String get_$240109() {
            return _$240109;
        }

        public void set_$240109(String _$240109) {
            this._$240109 = _$240109;
        }

        public String get_$240110() {
            return _$240110;
        }

        public void set_$240110(String _$240110) {
            this._$240110 = _$240110;
        }

        public String get_$240111() {
            return _$240111;
        }

        public void set_$240111(String _$240111) {
            this._$240111 = _$240111;
        }

        public String get_$240113() {
            return _$240113;
        }

        public void set_$240113(String _$240113) {
            this._$240113 = _$240113;
        }

        public String get_$241000() {
            return _$241000;
        }

        public void set_$241000(String _$241000) {
            this._$241000 = _$241000;
        }

        public String get_$241101() {
            return _$241101;
        }

        public void set_$241101(String _$241101) {
            this._$241101 = _$241101;
        }

        public String get_$241102() {
            return _$241102;
        }

        public void set_$241102(String _$241102) {
            this._$241102 = _$241102;
        }

        public String get_$241138() {
            return _$241138;
        }

        public void set_$241138(String _$241138) {
            this._$241138 = _$241138;
        }

        public String get_$241104() {
            return _$241104;
        }

        public void set_$241104(String _$241104) {
            this._$241104 = _$241104;
        }

        public String get_$241105() {
            return _$241105;
        }

        public void set_$241105(String _$241105) {
            this._$241105 = _$241105;
        }

        public String get_$241106() {
            return _$241106;
        }

        public void set_$241106(String _$241106) {
            this._$241106 = _$241106;
        }

        public String get_$241133() {
            return _$241133;
        }

        public void set_$241133(String _$241133) {
            this._$241133 = _$241133;
        }

        public String get_$241135() {
            return _$241135;
        }

        public void set_$241135(String _$241135) {
            this._$241135 = _$241135;
        }

        public String get_$241109() {
            return _$241109;
        }

        public void set_$241109(String _$241109) {
            this._$241109 = _$241109;
        }

        public String get_$241110() {
            return _$241110;
        }

        public void set_$241110(String _$241110) {
            this._$241110 = _$241110;
        }

        public String get_$241111() {
            return _$241111;
        }

        public void set_$241111(String _$241111) {
            this._$241111 = _$241111;
        }

        public String get_$241112() {
            return _$241112;
        }

        public void set_$241112(String _$241112) {
            this._$241112 = _$241112;
        }

        public String get_$241113() {
            return _$241113;
        }

        public void set_$241113(String _$241113) {
            this._$241113 = _$241113;
        }

        public String get_$241136() {
            return _$241136;
        }

        public void set_$241136(String _$241136) {
            this._$241136 = _$241136;
        }

        public String get_$241115() {
            return _$241115;
        }

        public void set_$241115(String _$241115) {
            this._$241115 = _$241115;
        }

        public String get_$241118() {
            return _$241118;
        }

        public void set_$241118(String _$241118) {
            this._$241118 = _$241118;
        }

        public String get_$241122() {
            return _$241122;
        }

        public void set_$241122(String _$241122) {
            this._$241122 = _$241122;
        }

        public String get_$241123() {
            return _$241123;
        }

        public void set_$241123(String _$241123) {
            this._$241123 = _$241123;
        }

        public String get_$241127() {
            return _$241127;
        }

        public void set_$241127(String _$241127) {
            this._$241127 = _$241127;
        }

        public String get_$241125() {
            return _$241125;
        }

        public void set_$241125(String _$241125) {
            this._$241125 = _$241125;
        }

        public String get_$241126() {
            return _$241126;
        }

        public void set_$241126(String _$241126) {
            this._$241126 = _$241126;
        }

        public String get_$241130() {
            return _$241130;
        }

        public void set_$241130(String _$241130) {
            this._$241130 = _$241130;
        }

        public String get_$241128() {
            return _$241128;
        }

        public void set_$241128(String _$241128) {
            this._$241128 = _$241128;
        }

        public String get_$241129() {
            return _$241129;
        }

        public void set_$241129(String _$241129) {
            this._$241129 = _$241129;
        }

        public String get_$241131() {
            return _$241131;
        }

        public void set_$241131(String _$241131) {
            this._$241131 = _$241131;
        }

        public String get_$241137() {
            return _$241137;
        }

        public void set_$241137(String _$241137) {
            this._$241137 = _$241137;
        }

        public String get_$255000() {
            return _$255000;
        }

        public void set_$255000(String _$255000) {
            this._$255000 = _$255000;
        }

        public String get_$255101() {
            return _$255101;
        }

        public void set_$255101(String _$255101) {
            this._$255101 = _$255101;
        }

        public String get_$255102() {
            return _$255102;
        }

        public void set_$255102(String _$255102) {
            this._$255102 = _$255102;
        }

        public String get_$255109() {
            return _$255109;
        }

        public void set_$255109(String _$255109) {
            this._$255109 = _$255109;
        }

        public String get_$255103() {
            return _$255103;
        }

        public void set_$255103(String _$255103) {
            this._$255103 = _$255103;
        }

        public String get_$255104() {
            return _$255104;
        }

        public void set_$255104(String _$255104) {
            this._$255104 = _$255104;
        }

        public String get_$255105() {
            return _$255105;
        }

        public void set_$255105(String _$255105) {
            this._$255105 = _$255105;
        }

        public String get_$255106() {
            return _$255106;
        }

        public void set_$255106(String _$255106) {
            this._$255106 = _$255106;
        }

        public String get_$255108() {
            return _$255108;
        }

        public void set_$255108(String _$255108) {
            this._$255108 = _$255108;
        }

        public String get_$255107() {
            return _$255107;
        }

        public void set_$255107(String _$255107) {
            this._$255107 = _$255107;
        }

        public String get_$396000() {
            return _$396000;
        }

        public void set_$396000(String _$396000) {
            this._$396000 = _$396000;
        }

        public String get_$396101() {
            return _$396101;
        }

        public void set_$396101(String _$396101) {
            this._$396101 = _$396101;
        }

        public String get_$396102() {
            return _$396102;
        }

        public void set_$396102(String _$396102) {
            this._$396102 = _$396102;
        }

        public String get_$396103() {
            return _$396103;
        }

        public void set_$396103(String _$396103) {
            this._$396103 = _$396103;
        }

        public String get_$396104() {
            return _$396104;
        }

        public void set_$396104(String _$396104) {
            this._$396104 = _$396104;
        }

        public String get_$396105() {
            return _$396105;
        }

        public void set_$396105(String _$396105) {
            this._$396105 = _$396105;
        }

        public String get_$396106() {
            return _$396106;
        }

        public void set_$396106(String _$396106) {
            this._$396106 = _$396106;
        }

        public String get_$249000() {
            return _$249000;
        }

        public void set_$249000(String _$249000) {
            this._$249000 = _$249000;
        }

        public String get_$249105() {
            return _$249105;
        }

        public void set_$249105(String _$249105) {
            this._$249105 = _$249105;
        }

        public String get_$249106() {
            return _$249106;
        }

        public void set_$249106(String _$249106) {
            this._$249106 = _$249106;
        }

        public String get_$249107() {
            return _$249107;
        }

        public void set_$249107(String _$249107) {
            this._$249107 = _$249107;
        }

        public String get_$249108() {
            return _$249108;
        }

        public void set_$249108(String _$249108) {
            this._$249108 = _$249108;
        }

        public String get_$249109() {
            return _$249109;
        }

        public void set_$249109(String _$249109) {
            this._$249109 = _$249109;
        }

        public String get_$249110() {
            return _$249110;
        }

        public void set_$249110(String _$249110) {
            this._$249110 = _$249110;
        }

        public String get_$249111() {
            return _$249111;
        }

        public void set_$249111(String _$249111) {
            this._$249111 = _$249111;
        }

        public String get_$249112() {
            return _$249112;
        }

        public void set_$249112(String _$249112) {
            this._$249112 = _$249112;
        }

        public String get_$243000() {
            return _$243000;
        }

        public void set_$243000(String _$243000) {
            this._$243000 = _$243000;
        }

        public String get_$243101() {
            return _$243101;
        }

        public void set_$243101(String _$243101) {
            this._$243101 = _$243101;
        }

        public String get_$243102() {
            return _$243102;
        }

        public void set_$243102(String _$243102) {
            this._$243102 = _$243102;
        }

        public String get_$243117() {
            return _$243117;
        }

        public void set_$243117(String _$243117) {
            this._$243117 = _$243117;
        }

        public String get_$243114() {
            return _$243114;
        }

        public void set_$243114(String _$243114) {
            this._$243114 = _$243114;
        }

        public String get_$243115() {
            return _$243115;
        }

        public void set_$243115(String _$243115) {
            this._$243115 = _$243115;
        }

        public String get_$243103() {
            return _$243103;
        }

        public void set_$243103(String _$243103) {
            this._$243103 = _$243103;
        }

        public String get_$243104() {
            return _$243104;
        }

        public void set_$243104(String _$243104) {
            this._$243104 = _$243104;
        }

        public String get_$243105() {
            return _$243105;
        }

        public void set_$243105(String _$243105) {
            this._$243105 = _$243105;
        }

        public String get_$243106() {
            return _$243106;
        }

        public void set_$243106(String _$243106) {
            this._$243106 = _$243106;
        }

        public String get_$243107() {
            return _$243107;
        }

        public void set_$243107(String _$243107) {
            this._$243107 = _$243107;
        }

        public String get_$243108() {
            return _$243108;
        }

        public void set_$243108(String _$243108) {
            this._$243108 = _$243108;
        }

        public String get_$243109() {
            return _$243109;
        }

        public void set_$243109(String _$243109) {
            this._$243109 = _$243109;
        }

        public String get_$243111() {
            return _$243111;
        }

        public void set_$243111(String _$243111) {
            this._$243111 = _$243111;
        }

        public String get_$243112() {
            return _$243112;
        }

        public void set_$243112(String _$243112) {
            this._$243112 = _$243112;
        }

        public String get_$243120() {
            return _$243120;
        }

        public void set_$243120(String _$243120) {
            this._$243120 = _$243120;
        }

        public String get_$243113() {
            return _$243113;
        }

        public void set_$243113(String _$243113) {
            this._$243113 = _$243113;
        }

        public String get_$243116() {
            return _$243116;
        }

        public void set_$243116(String _$243116) {
            this._$243116 = _$243116;
        }

        public String get_$243118() {
            return _$243118;
        }

        public void set_$243118(String _$243118) {
            this._$243118 = _$243118;
        }

        public String get_$243119() {
            return _$243119;
        }

        public void set_$243119(String _$243119) {
            this._$243119 = _$243119;
        }
    }
}
