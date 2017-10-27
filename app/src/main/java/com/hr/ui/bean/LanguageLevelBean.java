package com.hr.ui.bean;

import java.util.List;

/**
 * Created by wdr on 2017/10/27.
 */

public class LanguageLevelBean {

    /**
     * error_code : 0
     * language_list : [{"langname":"11","user_id":"9327095","read_level":"4","speak_level":"4","grade_exam":[{"score":"6000","id":"1109"}]}]
     * _run_time : 0.004086971282959
     */

    private int error_code;
    private String _run_time;
    private List<LanguageListBean> language_list;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String get_run_time() {
        return _run_time;
    }

    public void set_run_time(String _run_time) {
        this._run_time = _run_time;
    }

    public List<LanguageListBean> getLanguage_list() {
        return language_list;
    }

    public void setLanguage_list(List<LanguageListBean> language_list) {
        this.language_list = language_list;
    }

    public static class LanguageListBean {
        /**
         * langname : 11
         * user_id : 9327095
         * read_level : 4
         * speak_level : 4
         * grade_exam : [{"score":"6000","id":"1109"}]
         */

        private String langname;
        private String user_id;
        private String read_level;
        private String speak_level;
        private List<GradeExamBean> grade_exam;

        public String getLangname() {
            return langname;
        }

        public void setLangname(String langname) {
            this.langname = langname;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getRead_level() {
            return read_level;
        }

        public void setRead_level(String read_level) {
            this.read_level = read_level;
        }

        public String getSpeak_level() {
            return speak_level;
        }

        public void setSpeak_level(String speak_level) {
            this.speak_level = speak_level;
        }

        public List<GradeExamBean> getGrade_exam() {
            return grade_exam;
        }

        public void setGrade_exam(List<GradeExamBean> grade_exam) {
            this.grade_exam = grade_exam;
        }

        public static class GradeExamBean {
            /**
             * score : 6000
             * id : 1109
             */

            private String score;
            private String id;

            public String getScore() {
                return score;
            }

            public void setScore(String score) {
                this.score = score;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            @Override
            public String toString() {
                return "GradeExamBean{" +
                        "score='" + score + '\'' +
                        ", id='" + id + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "LanguageListBean{" +
                    "langname='" + langname + '\'' +
                    ", user_id='" + user_id + '\'' +
                    ", read_level='" + read_level + '\'' +
                    ", speak_level='" + speak_level + '\'' +
                    ", grade_exam=" + grade_exam +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LanguageLevelBean{" +
                "error_code=" + error_code +
                ", _run_time='" + _run_time + '\'' +
                ", language_list=" + language_list +
                '}';
    }
}
